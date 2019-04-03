package com.exc.service;

import com.exc.domain.CurrencyName;
import com.exc.domain.CurrencyPair;
import com.exc.domain.EntityFactory;
import com.exc.domain.enumeration.OrderStatusType;
import com.exc.domain.enumeration.OrderType;
import com.exc.domain.order.OrderPair;
import com.exc.repository.CurrencyPairRepository;
import com.exc.repository.order.OrderPairRepository;
import com.exc.repository.order.OrderPairRepositoryFactory;
import com.exc.service.dto.OrderPairDTO;
import com.exc.service.dto.remote.KeysRequestDTO;
import com.exc.service.dto.remote.KeysResponseDTO;
import com.exc.service.errors.*;
import com.exc.service.mapper.order.OrderPairEntityMapper;
import com.exc.service.mapper.order.OrderPairMapperFactory;
import com.exc.service.remote.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.swing.text.html.Option;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.*;


/**
 * Service Implementation for managing OrderPair.
 */
@Service
@Transactional
public class OrderPairService {

    private final Logger log = LoggerFactory.getLogger(OrderPairService.class);

    private OrderPairMapperFactory orderPairMapperFactory;
    private TradesService tradesService;
    private CurrencyPairRepository currencyPairRepository;
    private OrderPairFeeService orderPairFeeService;
    private OrderPairRepositoryFactory orderPairRepositoryFactory;
    private EntityFactory currencyEntityFactory;
    private UserService userService;

    public OrderPairService(UserService userService, OrderPairMapperFactory orderPairMapperFactory, CurrencyPairRepository currencyPairRepository, OrderPairRepositoryFactory orderPairRepositoryFactory, EntityFactory currencyEntityFactory) {
        this.orderPairMapperFactory = orderPairMapperFactory;
        this.currencyPairRepository = currencyPairRepository;
        this.orderPairRepositoryFactory = orderPairRepositoryFactory;
        this.currencyEntityFactory = currencyEntityFactory;
        this.userService = userService;
    }

    @Autowired
    public void setTradesService(@Lazy TradesService tradesService) {
        this.tradesService = tradesService;
    }

    @Autowired
    public void setOrderPairFeeService(@Lazy OrderPairFeeService orderPairFeeService) {
        this.orderPairFeeService = orderPairFeeService;
    }


    /**
     * @param orderPairDTO
     * @param isExecute    if true, order will be executed
     * @param userId       extracted from JWT
     * @return
     */

    public OrderPairDTO save(OrderPairDTO orderPairDTO, boolean isExecute, Long userId) {
        log.debug("Request to save OrderPair : {}", orderPairDTO);
        //TODO: validate balance&user
        //make sure that trade pair exists
        if (orderPairDTO.getPairId() == null ||
            !Optional.ofNullable(currencyPairRepository.findById(orderPairDTO.getPairId())).isPresent()) {
            throw new FailedCurrencyPairException("Cant find currency Pair in db");
        }

        if (orderPairDTO.getType() == null || orderPairDTO.getValue() == null || orderPairDTO.getRate() == null) {
            throw new FailedValidateOrderInput("Order rate/value/type must be present");
        }
        if (userId == null) {
            throw new UserNotFound("" + orderPairDTO.getUserId());
        }


        OrderPairDTO res = null;
        if (!isExecute) {
            orderPairDTO.setCreateDate(ZonedDateTime.now());
            orderPairDTO.setStatus(OrderStatusType.NEW);
            orderPairDTO.setId(null);
            res = processInternal(orderPairDTO, null, userId);
        } else {
            res = execute(orderPairDTO.getId(), orderPairDTO.getPairId(), userId);
        }

        return res;
    }


    /**
     * Save an orderPair.
     *
     * @param orderPairDTO the entity to save
     * @return the persisted entity
     */

    private OrderPairDTO processInternal(OrderPairDTO orderPairDTO, @Nullable OrderPairDTO toExecute, Long userId) {
        log.debug("Request to process OrderPair : {}", orderPairDTO);

        //make sure that trade pair exists
        CurrencyPair pair = currencyPairRepository.findById(orderPairDTO.getPairId()).orElse(null);

        CurrencyName buy = pair.getBuy().getCurrencyName(), sell = pair.getSell().getCurrencyName();

        orderPairDTO.setUserId(userId);
        OrderPairEntityMapper mapper = orderPairMapperFactory.getMapper(buy, sell, OrderStatusType.NEW);

        OrderPair orderPair = mapper.toEntity(orderPairDTO);
        orderPair.setPair(pair);

        orderPair = orderPairRepositoryFactory.getOrderPairRepo(buy, sell, OrderStatusType.NEW).save(orderPair);

        //withdraw order fee
        Map<Long, KeysResponseDTO> pKeys = userService.getKeys(new KeysRequestDTO(new HashSet<>(Arrays.asList(orderPair.getUserId())), buy, sell));
        orderPairFeeService.withdrawOrderFee(orderPair, OrderStatusType.NEW, pKeys.get(orderPair.getUserId()).getPrivateKey());

        Optional<OrderPair> toEx = null;
        if (toExecute != null) {
            toEx = orderPairRepositoryFactory.getOrderPairRepo(buy, sell, OrderStatusType.OPEN).findById(toExecute.getId());
        } else {
            toEx = Optional.empty();
        }
        try {
            tradesService.processNewOrder(orderPair, toEx.orElse(null));
        } catch (InterruptedException ex) {
            log.error(ex.getMessage(), ex);
        }


        return mapper.toDto(orderPair);
    }


    /**
     * clone existing order, and execute it as direct execution
     *
     * @param orderId
     * @param pairId
     * @param userId  will use this userName
     * @return
     */

    private OrderPairDTO execute(Long orderId, Long pairId, Long userId) {
        CurrencyPair pair = currencyPairRepository.getOne(pairId);
        CurrencyName buy = pair.getBuy().getCurrencyName(), sell = pair.getSell().getCurrencyName();
        Optional<OrderPair> toExecute = orderPairRepositoryFactory.getOrderPairRepo(buy, sell, OrderStatusType.OPEN).findById(orderId);
        OrderPairEntityMapper mapper = orderPairMapperFactory.getMapper(buy, sell, OrderStatusType.NEW);
        if (!toExecute.isPresent() || toExecute.get().getExecutions().size() > 0) {
            log.error("orderPair->'executions' must be empty, orderId/pairId : {}/{} ", orderId, pairId);
            throw new OrderPairExecutionException("orderPair->'executions' must be empty");
        }

        OrderPairDTO newOrder = mapper.toDto(toExecute.get());
        newOrder.setUserId(null);
        newOrder.setId(null);
        newOrder.setCreateDate(ZonedDateTime.now());
        newOrder.setStatus(OrderStatusType.NEW);

        if (toExecute.get().getType() == OrderType.SELL) {
            newOrder.setType(OrderType.BUY);
        } else newOrder.setType(OrderType.SELL);

        return processInternal(newOrder, mapper.toDto(toExecute.get()), userId);

    }

    /**
     * update status(migrate between tables), and return new id.
     *
     * @param orderId Order that contains executions
     * @param pairId
     * @param newType
     * @return map oldId -> newId, should be used to upgrade external ID from transactions DB
     */
    public Map<Long, Long> migrate(Long orderId, Long pairId, OrderStatusType newType) {
        CurrencyPair pair = currencyPairRepository.getOne(pairId);
        CurrencyName buy = pair.getBuy().getCurrencyName(), sell = pair.getSell().getCurrencyName();
        Map<Long, Long> result = new HashMap<>();

        OrderPairRepository processRepo = orderPairRepositoryFactory.getOrderPairRepo(buy, sell, OrderStatusType.IN_PROCESS);
        OrderPairRepository exRepo = orderPairRepositoryFactory.getOrderPairRepo(buy, sell, OrderStatusType.EXECUTED);

        Optional<OrderPair> op = processRepo.findById(orderId);
        OrderPair orderPair = op.get();
        List<OrderPair> newExecutions = new ArrayList<>();

        if (orderPair.getExecutions() != null && orderPair.getExecutions().size() > 0) {
            List<OrderPair> oldExs = new ArrayList<>(orderPair.getExecutions());
            for (OrderPair oex : oldExs) {
                OrderPair newex = currencyEntityFactory.makeOrder(buy, sell, newType, oex);
                newex = exRepo.save(newex);
                newExecutions.add(newex);
                result.put(oex.getId(), newex.getId());
                orderPair.removeExecution(oex);
            }
            //processRepo.save(orderPair);
           /* for (OrderPair oex : oldExs) {
                processRepo.delete(oex);
            }*/
        } else {
            log.error("Order migration failed. Executions empty for order {}, pair {}", orderId, pairId);
            throw new OrderPairExecutionException("Order migration failed.");
        }

        processRepo.delete(orderPair);

        OrderPair newOrderPair = currencyEntityFactory.makeOrder(buy, sell, newType, orderPair);

        newOrderPair.setExecutedDate(ZonedDateTime.now());
        newOrderPair = exRepo.save(newOrderPair);
        for (OrderPair exOp : newExecutions) {
            newOrderPair.addExecution(exOp);
        }
        newOrderPair = exRepo.save(newOrderPair);
        result.put(orderId, newOrderPair.getId());

        log.info("Order migrated to the PROCESSED table, old id: {}, new {}", orderId, newOrderPair.getId());

        return result;
    }

    /**
     * split partial executed order for 2, one of them would still be a part with main order, second will be reopen.
     *
     * @param mainId
     * @param exId
     * @param pairId
     * @param diff
     * @return new OrderId to be processed
     * @throws OrderPairExecutionException
     */

    public Long decouplePartialOrder(Long mainId, Long exId, Long pairId, BigInteger diff) throws OrderPairExecutionException {

        CurrencyPair pair = currencyPairRepository.getOne(pairId);
        CurrencyName buy = pair.getBuy().getCurrencyName(), sell = pair.getSell().getCurrencyName();
        OrderPairRepository openRepo = orderPairRepositoryFactory.getOrderPairRepo(buy, sell, OrderStatusType.OPEN);
        Optional<OrderPair> main = openRepo.findById(mainId);
        Optional<OrderPair> ex = openRepo.findById(exId);

        if (!main.isPresent() || !main.isPresent() || ex.get().getValue().compareTo(diff) <= 0) {
            log.error("Cannot decouple order {} from main {} with pair {} newValue {} ", exId, mainId, pairId, diff.toString());
            throw new OrderPairExecutionException("Cant decouple order!");
        }

        OrderPair decoupled = currencyEntityFactory.makeOrder(buy, sell, OrderStatusType.NEW, ex.get());
        decoupled.setValue(ex.get().getValue().subtract(diff));

        decoupled = openRepo.save(decoupled);

        ex.get().setValue(diff);
        openRepo.save(ex.get());
        return decoupled.getId();
    }


    /**
     * Support testing only method. Used only during unit tests
     *
     * @param id
     * @param pairId
     * @param exId
     */
    public void addExecution(Long id, Long pairId, Long exId) {
        CurrencyPair pair = currencyPairRepository.getOne(pairId);
        CurrencyName buy = pair.getBuy().getCurrencyName(), sell = pair.getSell().getCurrencyName();
        OrderPairRepository repository = orderPairRepositoryFactory.getOrderPairRepo(buy, sell, OrderStatusType.NEW);

        OrderPair orderPair = repository.getOne(id), ex = repository.getOne(exId);
        orderPair.addExecution(ex);
        repository.save(orderPair);
    }

    /**
     * Support testing only method. Used only during unit tests
     *
     * @param id
     * @param pairId
     */
    public void setStatusOpen(Long id, Long pairId) {
        CurrencyPair pair = currencyPairRepository.getOne(pairId);
        CurrencyName buy = pair.getBuy().getCurrencyName(), sell = pair.getSell().getCurrencyName();
        OrderPairRepository repository = orderPairRepositoryFactory.getOrderPairRepo(buy, sell, OrderStatusType.NEW);
        OrderPair orderPair = repository.getOne(id);
        orderPair.setStatus(OrderStatusType.OPEN);
        repository.save(orderPair);
    }


    /*  public OrderPairDTO update(OrderPairDTO orderPairDTO, String userName) {
          log.debug("Request to update OrderPair : {}", orderPairDTO);
          Optional<UserInfo> user = userInfoRepository.findOneByUserLogin(userName);
          if (orderPairDTO.getUserInfoId() != user.get().getId()) {
              log.error("Wrong user tried to update order {}", userName);
          } else {
              CurrencyPair pair = currencyPairRepository.findOne(orderPairDTO.getPairId());

              orderPairDTO.setModifyDate(ZonedDateTime.now());
              OrderPair orderPair = orderPairRepositoryFactory.getRepository(pair, orderPairDTO.getStatus()).findOne(orderPairDTO.getId());
              if (orderPair.getStatus() != OrderStatusType.OPEN) {
                  log.error("Wrong order status to update order {}", orderPair.getId());
              } else {
                  orderPair = orderPairMapper.toEntity(orderPairDTO);
                  orderPairFeeService.withdrawOrderFee(orderPair, OrderStatusType.MODIFIED);
                  tradesService.processNewOrder(orderPair, null);
                  orderPairDTO = orderPairMapper.toDto(orderPair);
              }

          }

          return orderPairDTO;
      }
  */
//recursion fix in map struct
    private OrderPairDTO mapExecutions(OrderPairEntityMapper mapper, OrderPair op) {
        if (op == null) {
            return null;
        }
        OrderPairDTO res = mapper.toDto(op);
        res.setExecutions(mapper.toDto(op.getExecutions()));
        return res;
    }

    /**
     * Get all the orderPairs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrderPairDTO> findAll(Pageable pageable, OrderStatusType orderStatusType, Long pairId) {
        log.debug("Request to get all OrderPairs");
        CurrencyPair pair = currencyPairRepository.getOne(pairId);
        CurrencyName buy = pair.getBuy().getCurrencyName(), sell = pair.getSell().getCurrencyName();
        OrderPairEntityMapper mapper = orderPairMapperFactory.getMapper(buy, sell, orderStatusType);
        return orderPairRepositoryFactory.getOrderPairRepo(buy, sell, orderStatusType).findAll(pageable)
            .map(op -> mapExecutions(mapper, (OrderPair) op));
    }

    /**
     * Get one orderPair by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public OrderPairDTO findOne(Long id, Long pairId, OrderStatusType orderStatusType) {
        log.debug("Request to get OrderPair : {}", id);
        CurrencyPair pair = currencyPairRepository.getOne(pairId);
        CurrencyName buy = pair.getBuy().getCurrencyName(), sell = pair.getSell().getCurrencyName();

        Optional<OrderPair> orderPair = orderPairRepositoryFactory.getOrderPairRepo(buy, sell, orderStatusType).findById(id);
        return orderPair.isPresent() ? mapExecutions(orderPairMapperFactory.getMapper(buy, sell, orderStatusType), orderPair.get()) : null;
    }


    /**
     * mark order as canceled, and move it to the processed orders table  .
     *
     * @param id
     * @param pairId
     */
    public void cancel(Long id, Long pairId, String privKey) {
        log.info("Request to cancel OrderPair : {}", id);
        CurrencyPair pair = currencyPairRepository.getOne(pairId);
        CurrencyName buy = pair.getBuy().getCurrencyName(), sell = pair.getSell().getCurrencyName();
        OrderPairRepository openRepo = orderPairRepositoryFactory.getOrderPairRepo(buy, sell, OrderStatusType.OPEN);
        OrderPair cancelOrder = openRepo.getOne(id);

        cancel(cancelOrder, OrderStatusType.CANCELLED, privKey);
    }

    /**
     * cancel order, and panish user...
     *
     * @param cancelOrder
     * @param orderStatusType
     */
    private void cancel(OrderPair cancelOrder, OrderStatusType orderStatusType, String privKey) {
        CurrencyName buy = cancelOrder.getPair().getBuy().getCurrencyName(), sell = cancelOrder.getPair().getSell().getCurrencyName();
        OrderPairRepository openRepo = orderPairRepositoryFactory.getOrderPairRepo(buy, sell, OrderStatusType.OPEN);

        if (cancelOrder.getStatus() != OrderStatusType.OPEN) {
            log.error("Can't cancel not open orders");
            throw new OrderPairCancelException(String.format("Can't cancel not open orders, order/pair %n/%n", cancelOrder.getId(), cancelOrder.getPair().getId()));
        } else if (orderStatusType.equals(OrderStatusType.CANCELLED)) {

            orderPairFeeService.withdrawOrderFee(cancelOrder, OrderStatusType.CANCELLED, privKey);
            cancelOrder.setCancelDate(ZonedDateTime.now());
            //cancelOrder.setStatus(OrderStatusType.CANCELLED);
            if (cancelOrder.getExecutions().size() > 0) {
                Set<OrderPair> exs = cancelOrder.getExecutions();
                exs.stream().forEach(ex -> ex.setStatus(OrderStatusType.OPEN));
                openRepo.saveAll(exs);
                cancelOrder.executions(Collections.EMPTY_SET);
            }
            openRepo.delete(cancelOrder);
            OrderPair newOrder = currencyEntityFactory.makeOrder(buy, sell, OrderStatusType.CANCELLED, cancelOrder);
            //move to processed table
            orderPairRepositoryFactory.getOrderPairRepo(buy, sell, OrderStatusType.CANCELLED)
                .save(newOrder);

        } /*else {

            orderPairFeeService.withdrawOrderFee(cancelOrder, OrderStatusType.HALT);
            cancelOrder.setCancelDate(ZonedDateTime.now());
            cancelOrder.setStatus(orderStatusType);
            //cancelOrder.setStatus(OrderStatusType.CANCELLED);
            if (cancelOrder.getExecutions().size() > 0) {
                Set<OrderPair> exs = cancelOrder.getExecutions();
                exs.stream().forEach(ex -> {
                    ex.setStatus(OrderStatusType.HALT);
                    openRepo.save(ex);
                });
            }
            //move to processed table
            orderPairRepositoryFactory.getOrderPairRepo(buy, sell, OrderStatusType.HALT)
                .save(cancelOrder);

        }*/
    }

    /**
     * cancel all orders for specified users and pair
     *
     * @param userInfoIds
     * @param pairId
     */
    public void cancelOrders(Set<Long> userInfoIds, Long pairId, Map<Long, KeysResponseDTO> keys) {
        log.info("Request to cancel orders!");
        CurrencyPair pair = currencyPairRepository.getOne(pairId);
        OrderPairRepository openRepo = orderPairRepositoryFactory.getOrderPairRepo(pair.getBuy().getCurrencyName(), pair.getSell().getCurrencyName(), OrderStatusType.OPEN);
        List<OrderPair> allOrders = new ArrayList<>();
        for (Long userId : userInfoIds) {
            allOrders.addAll(openRepo.findAllByUserId(userId));
        }
        for (OrderPair cancelOrder : allOrders) {
            cancel(cancelOrder, OrderStatusType.CANCELLED, keys.get(cancelOrder.getUserId()).getPrivateKey());
        }

    }


}
