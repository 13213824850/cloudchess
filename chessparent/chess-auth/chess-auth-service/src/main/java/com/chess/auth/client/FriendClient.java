package com.chess.auth.client;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 3:40 2019/3/30 0030
 * @Modifide by:
 */

import com.chess.user.api.FriendApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "CHESS-USER-SERVICE")
public interface FriendClient extends FriendApi{
}
