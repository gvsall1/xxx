package com.exc.service.remote;

import com.exc.client.AuthorizedFeignClient;
import com.exc.config.ServiceName;
import com.exc.domain.CurrencyName;
import com.exc.service.dto.remote.KeysRequestDTO;
import com.exc.service.dto.remote.KeysResponseDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.Set;

@AuthorizedFeignClient(name = ServiceName.USER)
public interface UserService {
    /**
     * @param
     * @return private keys for each userId, and 2 currencies
     */
    @RequestMapping(value = "/api/internal/get-pkeys", method = RequestMethod.POST)
    Map<Long, KeysResponseDTO> getKeys(@RequestBody KeysRequestDTO keysRequestDTO);
}
