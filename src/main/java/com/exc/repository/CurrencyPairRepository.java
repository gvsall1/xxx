package com.exc.repository;

import com.exc.domain.CurrencyName;
import com.exc.domain.CurrencyPair;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CurrencyPair entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyPairRepository extends JpaRepository<CurrencyPair, Long> {
    /**
     * will find currency pair by currency names
     * @param buy
     * @param sell
     * @return
     */
    CurrencyPair findByBuyCurrencyNameAndSellCurrencyName(CurrencyName buy, CurrencyName sell);

}
