package com.chess.user.api;

import com.chess.user.pojo.UserInfo;
import com.chess.user.vo.UserLogin;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 3:25 2019/3/11 0011
 * @Modifide by:
 */
public interface UserApi {
    @PostMapping(value = "/query")
    public UserInfo queryUserInfo(@RequestParam("userName")String userName, @RequestParam("password")
            String password);
}
