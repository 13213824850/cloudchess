package com.chess.client;

import com.chess.user.api.FriendApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 上午 10:39 2019/3/30 0030
 * @Modifide by:
 */
@FeignClient(value = "CHESS-USER-SERVICE")
public interface FriendClient extends FriendApi {
}
