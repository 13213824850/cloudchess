import com.chess.auth.enty.UserInfoToken;
import com.chess.auth.utils.JwtUtils;
import com.chess.auth.utils.RsaUtils;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author bystander
 * @date 2018/10/1
 */
public class JwtUtilsTest {

    private String s;
    private static final String publicKeyPath = "/chessauthkey/rsa.pub";
    private static final String privateKeyPath = "/chessauthkey/rsa.pri";

    private PrivateKey privateKey;
    private PublicKey publicKey;


    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(publicKeyPath, privateKeyPath, "234");
    }

   // @Before
    public void testGetRsa() throws Exception {
        privateKey = RsaUtils.getPrivateKey(privateKeyPath);
        publicKey = RsaUtils.getPublicKey(publicKeyPath);
    }

    @Test
    public void generateToken() {
        //生成Token
         s = JwtUtils.generateToken(new UserInfoToken(20L, "Jack"), 100,privateKey);
        System.out.println("s = " + s);
    }


    //@After
    public void parseToken() {
        String token = s;
        UserInfoToken userInfo = JwtUtils.getUserInfoToken(publicKey, token);
        System.out.println("id:" + userInfo.getId());
        System.out.println("name:" + userInfo.getName());
    }

    @Test
    public void parseToken1() {

        System.out.println(DateTime.now());
        System.out.println(DateTime.now().plusMinutes(10).toDate());
    }

    @Test
    public void getUserInfo() {
    }

    @Test
    public void getUserInfo1() {
    }
}