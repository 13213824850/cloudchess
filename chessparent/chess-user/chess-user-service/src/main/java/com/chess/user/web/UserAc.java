package com.chess.user.web;

import com.alibaba.fastjson.JSON;
import com.chess.common.util.Msg;
import com.chess.common.vo.ActionCode;
import com.chess.user.service.UserInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: hyl
 * @description:
 * @date: 14:56 2019/4/20
 **/
@Controller
public class UserAc {

    @Autowired
    UserInfoService userInfoService;
    @ApiOperation(value="激活用户",notes="邮箱链接")
    @GetMapping("/activeAcount")
    public String activeAccount(@ModelAttribute @RequestBody @Validated ActionCode ac) {
        Msg msg = userInfoService.activeAccount(ac);
        return "redirect:http://localhost:8080/#/userAc?"+200;

    }
}
