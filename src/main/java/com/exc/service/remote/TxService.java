package com.exc.service.remote;

import com.exc.client.AuthorizedFeignClient;
import com.exc.config.ServiceName;
import com.exc.domain.CurrencyName;
import com.exc.service.dto.BalanceStatusDTO;
import com.exc.service.dto.PreparedExDTO;
import com.exc.service.dto.remote.WithdrawRequestDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigInteger;
import java.util.List;

@AuthorizedFeignClient(name = ServiceName.TRANSACTIONS)
public interface TxService {
    /**
     * request current balance in chain, also fresh fees
     * @param cur
     * @return
     */
/*    @RequestMapping(value = "/api/internal/get-balance/{cur}", method = RequestMethod.POST)
    BalanceStatusDTO getBalanceStatus(@PathVariable("cur") CurrencyName cur);*/

    /**
     *  withdraw from user's wallet
     * @param withdrawRequestDTO
     * @return
     */
    @RequestMapping(value = "/api/internal/withdraw-balance", method = RequestMethod.POST)
    Long withdraw(WithdrawRequestDTO withdrawRequestDTO);

    /**
     *  withdraw order fee
     * @param withdrawRequestDTO
     * @return
     */
    @RequestMapping(value = "/api/internal/withdraw-order-fee", method = RequestMethod.POST)
    Long withdrawOrderFee(WithdrawRequestDTO withdrawRequestDTO);

    /**
     * request to send txs to the blockchain, and monitor it, after do async callback with result.
     * Current service no longer respond
     * @param preparedTransactions
     * @return
     */
    @RequestMapping(value = "/api/internal/process", method = RequestMethod.POST)
    Boolean processOrderTransactions(List<PreparedExDTO> preparedTransactions);

}
