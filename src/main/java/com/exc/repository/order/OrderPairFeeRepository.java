package com.exc.repository.order;

import com.exc.domain.OrderPairFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the OrderPairFee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderPairFeeRepository extends JpaRepository<OrderPairFee, Long> {

}
