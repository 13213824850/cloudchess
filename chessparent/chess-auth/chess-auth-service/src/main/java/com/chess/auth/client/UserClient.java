package com.chess.auth.client;

import com.chess.auth.fallback.AuthFallBack;
import com.chess.user.api.UserApi;
import com.chess.user.pojo.UserInfo;
import com.chess.user.vo.UserLogin;
import feign.Headers;
import feign.RequestLine;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 3:13 2019/3/11 0011
 * @Modifide by:
 */
@FeignClient(value = "CHESS-USER-SERVICE", fallback = AuthFallBack.class)
public interface UserClient extends UserApi{
}
