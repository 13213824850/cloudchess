package com.chess.auth.enty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bystander
 * @date 2018/9/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoToken {
    private Long id;
    private String name;
}
