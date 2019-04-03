package com.exc.repository.order;

import com.exc.domain.enumeration.OrderType;
import com.exc.domain.order.OrderPair;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the OrderPair entity.
 */
@SuppressWarnings("unused")
//@Repository
@NoRepositoryBean
public interface OrderPairRepository<T extends OrderPair> extends JpaRepository<T, Long> {
/*    @Query("select distinct order_pair from T order_pair left join fetch order_pair.executions")
    List<T> findAllWithEagerRelationships();

    @Query("select order_pair from T order_pair left join fetch order_pair.executions where order_pair.id =:id")
    T findOneWithEagerRelationships(@Param("id") Long id);*/

    List<T> findByTypeAndRateLessThanEqualAndUserIdNot(OrderType type, BigDecimal rate, Long userId);

    List<T> findByTypeAndRateGreaterThanEqualAndUserIdNot(OrderType type, BigDecimal rate, Long userId);

    @Override
    List<T> findAll();

    @Override
    List<T> findAll(Sort sort);

    @Override
    T getOne(Long aLong);

    @Override
    Optional<T> findById(Long aLong);

    @Override
    void flush();

    @Override
    <S extends T> S saveAndFlush(S entity);

    @Override
    void deleteInBatch(Iterable<T> entities);

    @Override
    void deleteAllInBatch();



    @Override
    <S extends T> List<S> findAll(Example<S> example);

    @Override
    <S extends T> List<S> findAll(Example<S> example, Sort sort);

    @Override
    Page<T> findAll(Pageable pageable);

    @Override
    <S extends T> S save(S entity);


    @Override
    void delete(T entity);


    @Override
    void deleteAll();


    @Override
    <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

    @Override
    <S extends T> long count(Example<S> example);

    @Override
    <S extends T> boolean exists(Example<S> example);

    List<T> findAllByUserId(Long id);

    Page<T> findAllByUserId(Long id, Pageable pageable);

    T findByExecutionsId(Long exId);


}
