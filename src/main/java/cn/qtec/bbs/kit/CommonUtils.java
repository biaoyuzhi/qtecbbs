package cn.qtec.bbs.kit;

import org.springframework.util.DigestUtils;

/**
 * Created by wuzh on 2019/1/30.
 */
public class CommonUtils {

    //本项目中的用户密码检验
    public static boolean passwordErrorVerify(String inputPassword,String password,String userId){
        String asHex = DigestUtils.md5DigestAsHex((inputPassword + userId).getBytes());
        if (!password.equalsIgnoreCase(asHex)) return false;
        return true;
    }

    //本项目中的密码加盐加密
    public static String passwordEncrypt(String password,String userId){
        return DigestUtils.md5DigestAsHex((password + userId).getBytes());
    }

}
