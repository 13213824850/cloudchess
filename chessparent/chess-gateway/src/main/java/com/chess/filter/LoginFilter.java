package com.chess.filter;

import com.alibaba.fastjson.JSON;
import com.chess.auth.utils.JwtUtils;
import com.chess.common.util.CookieUtils;
import com.chess.common.util.Msg;
import com.chess.properties.FilterProperties;
import com.chess.properties.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.RequestContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 5:36 2019/3/12 0012
 * @Modifide by:
 */
@Component
@Slf4j
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter{

    @Autowired
    private JwtProperties props;

    @Autowired
    private FilterProperties filterProps;
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String requestURI = request.getRequestURI();
        return isAllowPath(requestURI);
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String token = CookieUtils.getCookieValue(request, props.getCookieName());
        //解析token
        log.info("token{}", token);
        JwtUtils.getUserInfoToken(props.getPublicKey(), token);
        try {
        }catch (Exception e){
            log.info("用户验证失败");
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(403);
            Msg msg = new Msg();
            msg.setCode(403);
            msg.setMessage("用户未登录");
            context.setResponseBody(JSON.toJSONString(msg));
        }

        return null;
    }
    //判断uri是否需要拦截
    public boolean isAllowPath(String uri){
        List<String> lists = filterProps.getAllowPaths();
        int len = lists.size();
        for(int i = 0; i < len; i++){
            log.info("uri{}, allowUri{}",uri,lists.get(i));
            if(StringUtils.isNotBlank(uri) && uri.contains(lists.get(i))){
                return false;
            }
        }
        return true;
    }
}
