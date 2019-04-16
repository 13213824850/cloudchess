package com.chess.client;

import com.chess.common.util.Msg;
import com.chess.rankhis.api.HistoryApi;
import com.chess.rankhis.enty.GameRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "CHESS-RANKHIS-SERVICE")
public interface HistoryClient extends HistoryApi{

}
