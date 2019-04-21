package com.chess.auth.fallback;

import com.chess.auth.client.UserClient;
import com.chess.common.util.Msg;
import com.chess.user.pojo.UserInfo;
import com.chess.user.vo.UserLogin;
import org.springframework.stereotype.Component;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 5:20 2019/3/13 0013
 * @Modifide by:
 */
@Component
public class AuthFallBack implements UserClient {


    @Override
    public UserInfo queryUserInfo(String userName, String password) {
        return null;
    }

    @Override
    public Msg getUserpro(String userName) {
        return null;
    }

    @Override
    public UserInfo getUserInfoByName(String userName) {
        return null;
    }
}
