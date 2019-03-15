package com.chess.auth.properties;

import com.chess.auth.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 2:23 2019/3/11 0011
 * @Modifide by:
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "chess.jwt")
public class JwtProperties {
    private String secret;

    private String pubKeyPath;

    private String priKeyPath;

    private Integer expire;

    private String cookieName;

    private PrivateKey privateKey;

    private PublicKey publicKey;

    private int cookieMaxAge;
    @PostConstruct
    public void init(){
        //判断公钥、私钥是否存在
        File publicKey = new File(pubKeyPath);
        File privateKey = new File(priKeyPath);
        if(!publicKey.exists() || !privateKey.exists()){
            //生成秘钥
            try {
                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
            } catch (Exception e) {
                log.error("初始化秘钥失败");
                e.printStackTrace();
            }
        }
        try {
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        }catch (Exception e){
            log.error("获取秘钥失败{},{}",e.getCause(),e.getMessage());
        }
    }
}
