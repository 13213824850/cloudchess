package com.chess.client;

import com.chess.rankhis.api.RankApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "CHESS-RANKHIS-SERVICE")
public interface RankClient extends RankApi {
}
