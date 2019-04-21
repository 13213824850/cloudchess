package com.chess.client;

import com.chess.rankhis.api.GameListApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "CHESS-RANKHIS-SERVICE")
public interface GameListClient extends GameListApi {
}
