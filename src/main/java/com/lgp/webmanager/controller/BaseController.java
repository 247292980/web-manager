package com.lgp.webmanager.controller;

import com.lgp.webmanager.util.ConstUtil;
import com.lgp.webmanager.domain.User;
import com.lgp.webmanager.domain.enums.ExceptionMsg;
import com.lgp.webmanager.domain.result.Response;
import com.lgp.webmanager.util.Des3EncryptionUtil;
import com.lgp.webmanager.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.lgp.webmanager.util.ConstUtil.LOGIN_SESSION_KEY;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 基础控制器
 **/
@Controller
public class BaseController {

    protected org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Response result(ExceptionMsg msg) {
        return new Response(msg);
    }

    protected Response result() {
        return new Response();
    }

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected HttpSession getSession() {
        return this.getRequest().getSession();
    }

    protected User getUser() {
        return (User) this.getSession().getAttribute(LOGIN_SESSION_KEY);
    }

    protected Long getUserId() {
        Long id = 0L;
        User user = getUser();
        if (user != null) {
            id = user.getId();
        }
        return id;
    }

    protected String getUserName() {
        String userName = "网页收藏夹";
        User user = getUser();
        if (user != null) {
            userName = user.getUserName();
        }
        return userName;
    }

    protected String getUserIp() {
        String value = this.getRequest().getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(value) && !"unknown".equalsIgnoreCase(value)) {
            return value;
        } else {
            return this.getRequest().getRemoteAddr();
        }
    }

    protected String getPwd(String password) {
        try {
            String pwd = MD5Util.encrypt(password + ConstUtil.PASSWORD_KEY);
            return pwd;
        } catch (Exception e) {
            logger.error("密码加密异常：", e);
        }
        return null;
    }

    protected String cookieSign(String value) {
        try {
            value = value + ConstUtil.PASSWORD_KEY;
            String sign = Des3EncryptionUtil.encode(ConstUtil.DES3_KEY, value);
            return sign;
        } catch (Exception e) {
            logger.error("cookie签名异常：", e);
        }
        return null;
    }
}
