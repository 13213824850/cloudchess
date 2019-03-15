package com.chess.common.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionCode implements Serializable {
    @NotBlank(message = "激活码不正确")
    private String code;
    @NotBlank(message = "用户名不能为空")
    private String userName;
}
