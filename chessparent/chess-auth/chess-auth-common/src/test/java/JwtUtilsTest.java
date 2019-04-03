import com.chess.auth.enty.UserInfoToken;
import com.chess.auth.utils.JwtUtils;
import com.chess.auth.utils.RsaUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpCookie;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.xml.crypto.Data;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.*;

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
        s = JwtUtils.generateToken(new UserInfoToken(20L, "Jack"), 100, privateKey);
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
        MultiValueMap<Integer, List> mm = new LinkedMultiValueMap<Integer, List>();
        mm.add(1, new ArrayList(1));
        List<List> lists = mm.get(1);

        System.out.println(lists);

    }

    @Test
    public void getUserInfo1() throws CloneNotSupportedException {
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put(1,null);
        System.out.println(objectObjectHashMap.get(1));

    }
    @Test
    public  void testDate() throws InterruptedException {
        System.out.println();
        long time = new Date().getTime();
        Thread.sleep(1000);
        long l = System.currentTimeMillis() - time;
        System.out.println(l);

    }
}
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
class St{
    private int a;
}