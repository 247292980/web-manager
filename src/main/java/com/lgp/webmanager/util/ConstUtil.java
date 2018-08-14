package com.lgp.webmanager.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 *
 * 全局变量工具类
 **/
@Component
public class ConstUtil {

    public static String BASE_PATH;

    public static String LOGIN_SESSION_KEY = "LoginSession";

    public static String PASSWORD_KEY = "@#$%^&*()OPG#$%^&*(HG";

    public static String DES3_KEY = "9964DYByKL967c3308imytCB";

    public static String DEFAULT_LOGO = "img/logo.png";

    public static String USER_AGENT = "Mozilla";

    public static String LAST_REFERER = "LAST_REFERER";
    //一个月
    public static int COOKIE_TIMEOUT = 30 * 24 * 60 * 60;

    public static String SECURITY_FILTER = "SecurityFilter";


    @Autowired(required = true)
    public void setBasePath(@Value("${favorites.base.path}") String basePath) {
        BASE_PATH = basePath;
    }


}
