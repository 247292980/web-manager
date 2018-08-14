package com.lgp.webmanager.comm.filter;

import com.lgp.webmanager.domain.User;
import com.lgp.webmanager.repository.UserRepository;
import com.lgp.webmanager.util.Des3EncryptionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.lgp.webmanager.util.ConstUtil.*;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 安全过滤器
 */
public class SecurityFilter implements Filter {

    protected org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private static Set<String> UrlsSet = new HashSet<String>();

    @Autowired
    private UserRepository userRepository;

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        UrlsSet.add("/login");
        UrlsSet.add("/register");
        UrlsSet.add("/index");
        UrlsSet.add("/forgotPassword");
        UrlsSet.add("/newPassword");
        UrlsSet.add("/tool");
    }

    @Override
    public void doFilter(ServletRequest srequest, ServletResponse sresponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) srequest;
        String uri = request.getRequestURI();
        if (request.getSession().getAttribute(LOGIN_SESSION_KEY) == null) {
            Cookie[] cookies = request.getCookies();
            if (containsSuffix(uri) || UrlsSet.contains(uri) || containsKey(uri)) {
                logger.debug("don't check  url , " + request.getRequestURI());
                filterChain.doFilter(srequest, sresponse);
                return;
                //判断有没有cookie
            } else if (cookies != null) {
                boolean haveCookie = true;
                for (int i = 0; i < cookies.length; i++) {
                    Cookie cookie = cookies[i];
                    if (cookie.getName().equals(LOGIN_SESSION_KEY)) {
                        if (StringUtils.isNotBlank(cookie.getValue())) {
                            haveCookie = false;
                        } else {
                            break;
                        }
                        String value = this.getUserId(cookie.getValue());
                        Long userId = 0L;
                        if (userRepository == null) {
                            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                            userRepository = (UserRepository) factory.getBean("userRepository");
                        }
                        if (StringUtils.isNotBlank(value)) {
                            userId = Long.parseLong(value);
                        }
                        User user = userRepository.findOne(userId);
                        String html = "";
                        //判断有没有登录信息
                        if (null == user) {
                            html = "<script type=\"text/javascript\">window.location.href=\"_BASE_PATH_login\"</script>";
                        } else {
                            logger.info("userId :" + user.getId());
                            request.getSession().setAttribute(LOGIN_SESSION_KEY, user);
                            String referer = this.getRef(request);
                            if (referer.indexOf("/collect?") >= 0) {
                                filterChain.doFilter(srequest, sresponse);
                                return;
                            } else {
                                html = "<script type=\"text/javascript\">window.location.href=\"_BASE_PATH_\"</script>";
                            }
                        }
                        html = html.replace("_BASE_PATH_", BASE_PATH);
                        sresponse.getWriter().write(html);
                    }
                }
                if (haveCookie) {
                    //有session没有cookie就到登录页面
                    toLogin(request, sresponse);
                }
            } else {
                //没有session就到登录页面
                toLogin(request, sresponse);
            }
        } else {
            filterChain.doFilter(srequest, sresponse);
        }
    }

    /**
     * 判断后缀
     */
    private boolean containsSuffix(String url) {
        if (url.endsWith(".js")
                || url.endsWith(".css")
                || url.endsWith(".jpg")
                || url.endsWith(".gif")
                || url.endsWith(".png")
                || url.endsWith(".html")
                || url.endsWith(".eot")
                || url.endsWith(".svg")
                || url.endsWith(".ttf")
                || url.endsWith(".woff")
                || url.endsWith(".ico")
                || url.endsWith(".woff2")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断url
     */
    private boolean containsKey(String url) {
        if (url.contains("/media/")
                || url.contains("/login") || url.contains("/user/login")
                || url.contains("/register") || url.contains("/user/regist") || url.contains("/index")
                || url.contains("/forgotPassword") || url.contains("/user/sendForgotPasswordEmail")
                || url.contains("/newPassword") || url.contains("/user/setNewPassword")
                || (url.contains("/collector") && !url.contains("/collect/detail/"))
                || url.contains("/collect/standard/") || url.contains("/collect/simple/")
                || url.contains("/user") || url.contains("/favorites") || url.contains("/comment")
                || url.startsWith("/user/")
                || url.startsWith("/feedback")
                || url.startsWith("/standard/")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void destroy() {
    }

    private String codeToString(String str) {
        String strString = str;
        try {
            byte tempB[] = strString.getBytes("ISO-8859-1");
            strString = new String(tempB);
            return strString;
        } catch (Exception e) {
            return strString;
        }
    }

    private String getRef(HttpServletRequest request) {
        String referer = "";
        String param = this.codeToString(request.getQueryString());
        if (StringUtils.isNotBlank(request.getContextPath())) {
            referer = referer + request.getContextPath();
        }
        if (StringUtils.isNotBlank(request.getServletPath())) {
            referer = referer + request.getServletPath();
        }
        if (StringUtils.isNotBlank(param)) {
            referer = referer + "?" + param;
        }
        request.getSession().setAttribute(LAST_REFERER, referer);
        return referer;
    }

    private String getUserId(String value) {
        try {
            String userId = Des3EncryptionUtil.decode(DES3_KEY, value);
            userId = userId.substring(0, userId.indexOf(PASSWORD_KEY));
            return userId;
        } catch (Exception e) {
            logger.error("解析cookie异常：", e);
        }
        return null;
    }

    private void toLogin(HttpServletRequest request, ServletResponse sresponse) throws IOException {
        String referer = this.getRef(request);
        logger.debug("security filter, deney, " + request.getRequestURI());
        String html = "";
        if (referer.contains("/collect?")) {
            html = "<script type=\"text/javascript\">window.location.href=\"_BASE_PATH_login\"</script>";
        } else {
            html = "<script type=\"text/javascript\">window.location.href=\"_BASE_PATH_index\"</script>";
        }
        html = html.replace("_BASE_PATH_", BASE_PATH);
        sresponse.getWriter().write(html);
    }
}