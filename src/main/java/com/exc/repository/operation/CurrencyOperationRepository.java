package com.exc.repository.operation;

import com.exc.domain.operation.CurrencyOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


/**
 * Spring Data JPA repository for the CurrencyOperation entity.
 */
@SuppressWarnings("unused")
//@Repository
@NoRepositoryBean
public interface CurrencyOperationRepository<T extends CurrencyOperation> extends JpaRepository<T, Long> {
    @Override
    <S extends T> S save(S entity);

    Page<T> findAllByUserId(Long userId, Pageable pageable);

    @Override
    T getOne(Long aLong);
}
