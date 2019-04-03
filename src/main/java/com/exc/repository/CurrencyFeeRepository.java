package com.exc.repository;

import com.exc.domain.CurrencyFee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CurrencyFee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyFeeRepository extends JpaRepository<CurrencyFee, Long> {

}
