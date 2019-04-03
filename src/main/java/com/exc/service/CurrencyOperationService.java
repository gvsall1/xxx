package com.exc.service;

import com.exc.domain.CryptoCurrency;
import com.exc.domain.EntityFactory;
import com.exc.domain.enumeration.OperationType;
import com.exc.domain.operation.CurrencyOperation;
import com.exc.repository.CryptoCurrencyRepository;
import com.exc.repository.operation.CurrencyOperationRepository;
import com.exc.repository.operation.CurrencyOperationRepositoryFactory;
import com.exc.service.dto.CurrencyOperationDTO;
import com.exc.service.dto.remote.WithdrawRequestDTO;
import com.exc.service.mapper.operation.CurrencyOperationEntityMapper;
import com.exc.service.mapper.operation.CurrencyOperationMapperFactory;
import com.exc.service.remote.TxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.ZonedDateTime;


/**
 * Service Implementation for managing CurrencyOperation.
 */
@Service
@Transactional
public class CurrencyOperationService {

    private final Logger log = LoggerFactory.getLogger(CurrencyOperationService.class);

    private CurrencyOperationMapperFactory currencyOperationMapperFactory;
    private CurrencyOperationRepositoryFactory currencyOperationRepositoryFactory;
    private CryptoCurrencyRepository cryptoCurrencyRepository;
    private CurrencyFeeService currencyFeeService;
    private TxService txService;
    private EntityFactory entityFactory;

    public CurrencyOperationService(CurrencyOperationMapperFactory currencyOperationMapperFactory, CurrencyOperationRepositoryFactory currencyOperationRepositoryFactory, CryptoCurrencyRepository cryptoCurrencyRepository, CurrencyFeeService currencyFeeService, TxService txService) {
        this.currencyOperationMapperFactory = currencyOperationMapperFactory;
        this.currencyOperationRepositoryFactory = currencyOperationRepositoryFactory;
        this.cryptoCurrencyRepository = cryptoCurrencyRepository;
        this.currencyFeeService = currencyFeeService;
        this.txService = txService;
    }

    /**
     * Save&process a currencyOperation.
     *
     * @param currencyOperationDTO the entity to save
     * @return the persisted entity
     */
    public CurrencyOperationDTO save(CurrencyOperationDTO currencyOperationDTO) {
        log.debug("Request to save CurrencyOperation : {}", currencyOperationDTO);
        //todo validate

        CurrencyOperationRepository repo = currencyOperationRepositoryFactory.getRepo(currencyOperationDTO.getCurrencyName());
        CurrencyOperation currencyOperation = repo.save(
            currencyOperationMapperFactory
                .getMapper(currencyOperationDTO.getCurrencyName())
                .toEntity(currencyOperationDTO)
        );
        currencyOperation = repo.save(currencyOperation);

        return currencyOperationMapperFactory.getMapper(currencyOperationDTO.getCurrencyName()).toDto(currencyOperation);
    }


    /**
     * withdraw value rom user's wallet, also withdraw fee if value exists in db.
     *
     * @return CurrencyOperationDTO with withdraw tx.
     */

    public CurrencyOperationDTO withdraw(Long userId, Long curId, String fromKey, String toKey, BigInteger value) {
        //todo: validate something ?
        CryptoCurrency currency = cryptoCurrencyRepository.getOne(curId);
        CurrencyOperationEntityMapper mapper = currencyOperationMapperFactory
            .getMapper(currency.getCurrencyName());

        CurrencyOperationRepository repo = currencyOperationRepositoryFactory.getRepo(currency.getCurrencyName());
        CurrencyOperation currencyOperation = entityFactory.makeOp(currency.getCurrencyName());
        currencyOperation.setReceiverAddress(toKey);
        currencyOperation.setType(OperationType.WITHDRAW);
        currencyOperation.setUserId(userId);
        currencyOperation.setCurrency(currency);
        currencyOperation.setReceiverAddress(toKey);
        currencyOperation.setValue(value);
        currencyOperation.setCreateDate(ZonedDateTime.now());
        //track withdraw request
        currencyOperation = repo.save(currencyOperation);
        BigInteger feeValue = currencyFeeService.getFee(currency.getId(), currencyOperation.getType());

        WithdrawRequestDTO withdrawRequestDTO = new WithdrawRequestDTO();
        withdrawRequestDTO.setCurrencyName(currency.getCurrencyName());
        withdrawRequestDTO.setFromPrivate(fromKey);
        withdrawRequestDTO.setValue(value);
        withdrawRequestDTO.setFee(feeValue);
        // set upward track
        withdrawRequestDTO.setExternalId(currencyOperation.getId());
        //send tx to the execution
        Long txId = txService.withdraw(withdrawRequestDTO);
        // set backward track
        currencyOperation.setTx(txId);

        if (txId == null) {
            log.error("Withdraw request failed, wrong txId, user : {}", userId);
            repo.delete(currencyOperation);
            currencyOperation = null;
        }

        return mapper.toDto(currencyOperation);

    }


    /**
     * Get all the currencyOperations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CurrencyOperationDTO> findAll(Pageable pageable, Long currencyId) {
        log.debug("Request to get all CurrencyOperations");
        CryptoCurrency cur = cryptoCurrencyRepository.getOne(currencyId);
        CurrencyOperationEntityMapper mapper = currencyOperationMapperFactory.getMapper(cur.getCurrencyName());
        return currencyOperationRepositoryFactory.getRepo(cur.getCurrencyName()).findAll(pageable)
            .map(co ->
                mapper.toDto((CurrencyOperation) co)
            );
    }

    /**
     * Get one currencyOperation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CurrencyOperationDTO findOne(Long id, Long currencyId) {
        log.debug("Request to get CurrencyOperation : {}", id);
        CryptoCurrency cur = cryptoCurrencyRepository.getOne(currencyId);
        CurrencyOperation currencyOperation = currencyOperationRepositoryFactory.getRepo(cur.getCurrencyName()).getOne(id);
        return currencyOperationMapperFactory.getMapper(cur.getCurrencyName()).toDto(currencyOperation);
    }

    /**
     * Delete the currencyOperation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id, Long currencyId) {
        log.debug("Request to delete CurrencyOperation : {}", id);
        CryptoCurrency cur = cryptoCurrencyRepository.getOne(currencyId);
        CurrencyOperationRepository repo = currencyOperationRepositoryFactory.getRepo(cur.getCurrencyName());
        repo.delete(id);
    }

}
