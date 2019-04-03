package com.chess.filter;

import com.chess.auth.enty.UserInfoToken;
import com.chess.auth.utils.JwtUtils;
import com.chess.properties.FilterProperties;
import com.chess.properties.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Set;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 6:11 2019/3/15 0015
 * @Modifide by:
 */
@Component
@Slf4j
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtProperties props;

    @Autowired
    private FilterProperties filterProps;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("经过网关");
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String path = uri.getPath();
        if(isAllowPath(path)){
            //放行
            return chain.filter(exchange);
        }
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        String token = null;
        List<HttpCookie> httpCookies = cookies.get(props.getCookieName());
        if(httpCookies!=null && httpCookies.size()!=0){
            token = httpCookies.get(0).getValue();
        }
        ServerHttpResponse response = exchange.getResponse();
        if(token == null){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //验证身份
        UserInfoToken userInfoToken = null;
        //解析token
        log.info("token{}", token);
        try {
            userInfoToken = JwtUtils.getUserInfoToken(props.getPublicKey(), token);
        }catch (Exception e){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            log.info("用户未登录");
            return response.setComplete();

        }
        String name = userInfoToken.getName();
        ServerHttpRequest host = request.mutate().header("username", name).header("Host",uri.getAuthority()).build();
        ServerWebExchange build = exchange.mutate().request(host).build();
        return chain.filter(build);
    }


    //判断uri是否需要放行
    public boolean isAllowPath(String uri){
        List<String> lists = filterProps.getAllowPaths();
        int len = lists.size();
        for(int i = 0; i < len; i++){
            log.info("uri{}, allowUri{}",uri,lists.get(i));
            if(StringUtils.isNotBlank(uri) && uri.contains(lists.get(i))){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
