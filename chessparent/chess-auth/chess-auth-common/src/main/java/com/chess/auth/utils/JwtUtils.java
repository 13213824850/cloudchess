package com.chess.auth.utils;

import com.chess.auth.enty.JwtConstants;
import com.chess.auth.enty.UserInfoToken;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 2:33 2019/3/11 0011
 * @Modifide by:
 */
@Slf4j
public class JwtUtils {

    /**
     * 生成token
     *
     * @Auther:huang yuan li
     * @Description:
     * @date: Create in ${TIME} ${DATE}
     */
    public static String generateToken(UserInfoToken userInfoToken, int expireMinutes,
                                       PrivateKey privateKey) {
        log.info("时间{}，生成的过期时间{}",DateTime.now(),DateTime.now().plusMillis(expireMinutes).toDate());
        return Jwts.builder()
                .claim(JwtConstants.JWT_KEY_ID, userInfoToken.getId())
                .claim(JwtConstants.JWT_KEY_USER_NAME, userInfoToken.getName())
                .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    //解析token
    public static Jws<Claims> parseToken(PublicKey publicKey, String token) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    public static Jws<Claims> parseToken(byte[] publicKey, String token) throws Exception {
        return Jwts.parser().setSigningKey(RsaUtils.getPublicKey(publicKey)).parseClaimsJws(token);
    }

    //解析token 用户信息
    public static UserInfoToken getUserInfoToken(PublicKey publicKey, String token) {
        UserInfoToken userInfoToken = null;

        Jws<Claims> claimsJws = parseToken(publicKey, token);
        Claims body = claimsJws.getBody();
        log.info("解析过期时间{}",body.getExpiration());
        userInfoToken = new UserInfoToken(ObjectUtils.toLong(body.get(JwtConstants.JWT_KEY_ID)),
                ObjectUtils.toString(body.get(JwtConstants.JWT_KEY_USER_NAME)));

        return userInfoToken;

    }

    public static UserInfoToken getUserInfoToken(byte[] publicKey, String token) throws Exception {
        Jws<Claims> claimsJws = parseToken(publicKey, token);
        Claims body = claimsJws.getBody();
        return new UserInfoToken(ObjectUtils.toLong(JwtConstants.JWT_KEY_ID),
                ObjectUtils.toString(JwtConstants.JWT_KEY_USER_NAME));

    }
}
