package com.chess.rankhis.client;

import com.chess.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "CHESS-USER-SERVICE")
public interface UserClient extends UserApi {
}
