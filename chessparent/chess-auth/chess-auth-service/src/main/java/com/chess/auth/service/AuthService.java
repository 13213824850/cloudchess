package com.chess.auth.service;

import com.chess.auth.client.FriendClient;
import com.chess.auth.client.UserClient;
import com.chess.auth.enty.UserInfoToken;
import com.chess.auth.properties.JwtProperties;
import com.chess.auth.utils.JwtUtils;
import com.chess.common.enumcodes.ExceptionEnum;
import com.chess.common.exception.ChessException;
import com.chess.common.util.Msg;
import com.chess.user.api.UserApi;
import com.chess.user.pojo.UserInfo;
import com.chess.user.vo.UserLogin;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 3:12 2019/3/11 0011
 * @Modifide by:
 */
@Service
@Slf4j
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {
    @Autowired
    private UserClient userClient;
    @Autowired
    private FriendClient friendClient;
    @Autowired
    private JwtProperties props;
    public String  login(UserLogin userLogin){
        UserInfo userInfo = userClient.queryUserInfo(userLogin.getUserName(),userLogin.getPassword());
        if(StringUtils.isBlank(userInfo.getUserName())){
            throw new ChessException(ExceptionEnum.NOTFINDUSER);
        }
        log.info("userinfo{}",userInfo);
        //生成token
        UserInfoToken userInfoToken = new UserInfoToken(userInfo.getUserId(), userInfo.getUserName());
        log.info("prikey{}",props.getPrivateKey());
        String token = JwtUtils.generateToken(userInfoToken, props.getExpire(), props.getPrivateKey());
        return token;

    }
}
