package com.exc.repository;

import com.exc.domain.CryptoCurrency;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CryptoCurrency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrency, Long> {

}
