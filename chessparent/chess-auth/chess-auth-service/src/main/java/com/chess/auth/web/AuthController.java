package com.chess.auth.web;

import com.chess.auth.enty.UserInfoToken;
import com.chess.auth.properties.JwtProperties;
import com.chess.auth.service.AuthService;
import com.chess.auth.utils.JwtUtils;
import com.chess.auth.utils.RsaUtils;
import com.chess.common.enumcodes.ExceptionEnum;
import com.chess.common.exception.ChessException;
import com.chess.common.util.CookieUtils;
import com.chess.common.util.Msg;
import com.chess.user.vo.UserLogin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.*;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 3:08 2019/3/11 0011
 * @Modifide by:
 */
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @Autowired
    JwtProperties props;
    @Autowired
    private AuthService authService;

    //登录授权
    @PostMapping(value = "accredit")
    public Msg login(@Validated  UserLogin userLogin, HttpServletResponse response, HttpServletRequest request){
        String token = authService.login(userLogin);
        //写入cookie
        if(StringUtils.isBlank(token)){
             throw new ChessException(ExceptionEnum.CREATETOKENFAIL);
        }
        CookieUtils.newBuilder(response).httpOnly().maxAge(props.getCookieMaxAge()).request(request)
                .build(props.getCookieName(), token);
        return Msg.success();
    }

    //验证
    @GetMapping("verify")
    public Msg verifyUser(@CookieValue("CHESS_TOKEN")String token, HttpServletRequest request, HttpServletResponse response){
        UserInfoToken userInfoToken = JwtUtils.getUserInfoToken(props.getPublicKey(), token);
        String newToken = JwtUtils.generateToken(userInfoToken,  props.getExpire(),props.getPrivateKey());
        //将新的Token写入cookie中，并设置httpOnly
        CookieUtils.newBuilder(response).httpOnly().maxAge(props.getCookieMaxAge()).request(request).build(props.getCookieName(), newToken);
        return Msg.success();
    }

    //注销
    @GetMapping("logout")
    public Msg logOut(@CookieValue("CHESS_TOKEN")String token, HttpServletRequest request, HttpServletResponse response){
            if(StringUtils.isNotBlank(token)){
                //过期时间设置为0
                CookieUtils.newBuilder(response).httpOnly().maxAge(0).request(request)
                        .build(props.getCookieName(), token);
            }
            return Msg.success();
    }

}
