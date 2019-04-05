package com.chess.client;

import com.chess.rankhis.api.HistoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "CHESS-RANKHIS-SERVICE")
public interface HistoryClient extends HistoryApi {
}
