package com.lgp.webmanager.controller;

import com.lgp.webmanager.comm.aop.Log;
import com.lgp.webmanager.domain.Config;
import com.lgp.webmanager.domain.Favorites;
import com.lgp.webmanager.domain.User;
import com.lgp.webmanager.domain.enums.ExceptionMsg;
import com.lgp.webmanager.domain.result.Response;
import com.lgp.webmanager.domain.result.ResponseData;
import com.lgp.webmanager.repository.ConfigRepository;
import com.lgp.webmanager.repository.FavoritesRepository;
import com.lgp.webmanager.repository.FollowRepository;
import com.lgp.webmanager.repository.UserRepository;
import com.lgp.webmanager.service.ConfigService;
import com.lgp.webmanager.service.FavoritesService;
import com.lgp.webmanager.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * <p>
 * user控制器
 **/
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private UserRepository userRepository;
    @Resource
    private ConfigService configService;
    @Resource
    private FavoritesService favoritesService;
    @Resource
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String mailFrom;
    @Value("${spring.mail.subject.forgotpassword}")
    private String mailSubject;
    @Value("${spring.mail.content.forgotpassword}")
    private String mailContent;
    @Value("${forgotpassword.url}")
    private String forgotpasswordUrl;
    @Value("${static.url}")
    private String staticUrl;
    @Value("${file.profilepictures.url}")
    private String fileProfilepicturesUrl;
    @Value("${file.backgroundpictures.url}")
    private String fileBackgroundpicturesUrl;
    @Autowired
    private ConfigRepository configRepository;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private FavoritesRepository favoritesRepository;

    /**
     * 登陆
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @Log(description = "登陆")
    public ResponseData login(User user, HttpServletResponse response) {
        try {
            User loginUser = userRepository.findByUserNameOrEmail(user.getUserName(), user.getEmail());
            if (null == loginUser) {
                return new ResponseData(ExceptionMsg.LoginNameNotExists);
            } else if (!loginUser.getPassWord().equals(getPwd(user.getPassWord()))) {
                return new ResponseData(ExceptionMsg.LoginNameOrPassWordError);
            }
            Cookie cookie = new Cookie(ConstUtil.LOGIN_SESSION_KEY, cookieSign(loginUser.getId().toString()));
            cookie.setMaxAge(ConstUtil.COOKIE_TIMEOUT);
            cookie.setPath("/");
            response.addCookie(cookie);
            super.getSession().setAttribute(ConstUtil.LOGIN_SESSION_KEY, loginUser);
            String preUrl = "/";
            if (null != super.getSession().getAttribute(ConstUtil.LAST_REFERER)) {
                preUrl = String.valueOf(getSession().getAttribute(ConstUtil.LAST_REFERER));
                if (preUrl.indexOf("/collect?") < 0) {
                    preUrl = "/";
                }
            }
            return new ResponseData(ExceptionMsg.SUCCESS, preUrl);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("login failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    /**
     * 注册
     */
    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    @Log(description = "注册")
    public Response create(User user) {
        try {
            User registerUser = userRepository.findByEmail(user.getEmail());
            if (null != registerUser) {
                return result(ExceptionMsg.EmailUsed);
            }
            User userNameUser = userRepository.findByUserName(user.getUserName());
            if (null != userNameUser) {
                return result(ExceptionMsg.UserNameUsed);
            }
            user.setPassWord(getPwd(user.getPassWord()));
            user.setCreateTime(DateUtil.getCurrentTime());
            user.setLastModifyTime(DateUtil.getCurrentTime());
            user.setProfilePicture("img/favicon.png");
            userRepository.save(user);
            // 添加默认收藏夹
            Favorites favorites = favoritesService.saveFavorites(user.getId(), 0L, "未读列表");
            // 添加默认属性设置
            configService.saveConfig(user.getId(), String.valueOf(favorites.getId()));
            super.getSession().setAttribute(ConstUtil.LOGIN_SESSION_KEY, user);
            return new ResponseData(ExceptionMsg.SUCCESS, user.getUserName());
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("create user failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
    }

    /**
     * 获取收藏夹
     */
    @RequestMapping(value = "/getFavorites", method = RequestMethod.POST)
    @Log(description = "获取收藏夹")
    public List<Favorites> getFavorites() {
        List<Favorites> favorites = null;
        try {
            favorites = favoritesRepository.findByUserId(getUserId());
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("getFavorites failed, ", e);
        }
        return favorites;
    }

    /**
     * 获取属性设置
     */
    @RequestMapping(value = "/getConfig", method = RequestMethod.POST)
    @Log(description = "获取属性设置")
    public Config getConfig() {
        Config config = new Config();
        try {
            config = configRepository.findByUserId(getUserId());
        } catch (Exception e) {
            logger.error("异常：", e);
        }
        return config;
    }


    /**
     * 获取关注列表
     */
    @RequestMapping(value = "/getFollows")
    @Log(description = "获取关注列表")
    public List<String> getFollows() {
        List<String> followList = followRepository.findByUserId(getUserId());
        return followList;
    }

    /**
     * 忘记密码-发送重置邮件
     */
    @RequestMapping(value = "/sendForgotPasswordEmail", method = RequestMethod.POST)
    @Log(description = "发送忘记密码邮件")
    public Response sendForgotPasswordEmail(String email) {
        try {
            User registerUser = userRepository.findByEmail(email);
            if (null == registerUser) {
                return result(ExceptionMsg.EmailNotRegister);
            }
            // 密钥，一开始是空的
            String secretKey = UUID.randomUUID().toString();
            // 30分钟后过期
            Timestamp outDate = new Timestamp(System.currentTimeMillis() + 30 * 60 * 1000);
            long date = outDate.getTime() / 1000 * 1000;
            userRepository.setOutDateAndValidataCode(outDate + "", secretKey, email);
            //数字签名,規則就是这样
            String key = email + "$" + date + "$" + secretKey;
            String digitalSignature = MD5Util.encrypt(key);
            String resetPassHref = forgotpasswordUrl + "?sid=" + digitalSignature + "&email=" + email;
            String emailContent = StringUtil.getMessage(mailContent, resetPassHref);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(mailFrom);
            helper.setTo(email);
            //主题
            helper.setSubject(mailSubject);
            helper.setText(emailContent, true);
            mailSender.send(mimeMessage);
            return new ResponseData(ExceptionMsg.SUCCESS, email);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("sendForgotPasswordEmail failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
    }

    /**
     * 忘记密码-设置新密码
     */
    @RequestMapping(value = "/setNewPassword", method = RequestMethod.POST)
    @Log(description = "设置新密码")
    public Response setNewPassword(String newpwd, String email, String sid) {
        try {
            User user = userRepository.findByEmail(email);
            Timestamp outDate = Timestamp.valueOf(user.getOutDate());
            //表示已经过期
            if (outDate.getTime() <= System.currentTimeMillis()) {
                return result(ExceptionMsg.LinkOutdated);
            }
            //数字签名,規則就是这样
            String key = user.getEmail() + "$" + outDate.getTime() / 1000 * 1000 + "$" + user.getValidataCode();
            String digitalSignature = MD5Util.encrypt(key);
            if (!digitalSignature.equals(sid)) {
                return result(ExceptionMsg.LinkOutdated);
            }
            userRepository.setNewPassword(getPwd(newpwd), email);
            return new ResponseData(ExceptionMsg.SUCCESS, newpwd);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("setNewPassword failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @Log(description = "修改密码")
    public Response updatePassword(String oldPassword, String newPassword) {
        try {
            User user = super.getUser();
            String password = user.getPassWord();
            String newpwd = super.getPwd(newPassword);
            if (password.equals(super.getPwd(oldPassword))) {
                logger.info("UserController||updatePassword||旧密码正确 password={},oldpassword={}", password, super.getPwd(oldPassword));
                userRepository.setNewPassword(newpwd, user.getEmail());
                user.setPassWord(newpwd);
                super.getSession().setAttribute(ConstUtil.LOGIN_SESSION_KEY, user);
                return new ResponseData(ExceptionMsg.SUCCESS, newpwd);
            } else {
                return result(ExceptionMsg.PassWordError);
            }
        } catch (Exception e) {
            logger.error("updatePassword failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
    }

    /**
     * 修改个人简介
     */
    @RequestMapping(value = "/updateIntroduction", method = RequestMethod.POST)
    @Log(description = "修改个人简介")
    public ResponseData updateIntroduction(String introduction) {
        try {
            User user = super.getUser();
            userRepository.setIntroduction(introduction, user.getEmail());
            user.setIntroduction(introduction);
            super.getSession().setAttribute(ConstUtil.LOGIN_SESSION_KEY, user);
            return new ResponseData(ExceptionMsg.SUCCESS, introduction);
        } catch (Exception e) {
            logger.error("updateIntroduction failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    /**
     * 修改昵称
     */
    @RequestMapping(value = "/updateUserName", method = RequestMethod.POST)
    @Log(description = "修改昵称")
    public ResponseData updateUserName(String userName) {
        try {
            User loginUser = super.getUser();
            if (userName.equals(loginUser.getUserName())) {
                return new ResponseData(ExceptionMsg.UserNameSame);
            }
            User user = userRepository.findByUserName(userName);
            if (null != user && user.getUserName().equals(userName)) {
                return new ResponseData(ExceptionMsg.UserNameUsed);
            }
            if (userName.length() > 12) {
                return new ResponseData(ExceptionMsg.UserNameLengthLimit);
            }
            userRepository.setUserName(userName, loginUser.getEmail());
            loginUser.setUserName(userName);
            super.getSession().setAttribute(ConstUtil.LOGIN_SESSION_KEY, loginUser);
            return new ResponseData(ExceptionMsg.SUCCESS, userName);
        } catch (Exception e) {
            logger.error("updateUserName failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    /**
     * 上传头像
     */
    @RequestMapping(value = "/uploadHeadPortrait", method = RequestMethod.POST)
    public ResponseData uploadHeadPortrait(String dataUrl) {
        try {
            String filePath = staticUrl + fileProfilepicturesUrl;
            String fileName = UUID.randomUUID().toString() + ".png";
            String savePath = fileProfilepicturesUrl + fileName;
            String image = dataUrl;
            String header = "data:image";
            String[] imageArr = image.split(",");
            if (imageArr[0].contains(header)) {
                image = imageArr[1];
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] decodedBytes = decoder.decodeBuffer(image);
                FileUtil.uploadFile(decodedBytes, filePath, fileName);
                User user = super.getUser();
                userRepository.setProfilePicture(savePath, user.getId());
                user.setProfilePicture(savePath);
                super.getSession().setAttribute(ConstUtil.LOGIN_SESSION_KEY, user);
            }
            return new ResponseData(ExceptionMsg.SUCCESS, savePath);
        } catch (Exception e) {
            logger.error("upload head portrait failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    /**
     * 上传背景
     */
    @RequestMapping(value = "/uploadBackground", method = RequestMethod.POST)
    @Log(description = "上传背景")
    public ResponseData uploadBackground(String dataUrl) {
        try {
            String filePath = staticUrl + fileBackgroundpicturesUrl;
            String fileName = UUID.randomUUID().toString() + ".png";
            String savePath = fileBackgroundpicturesUrl + fileName;
            String image = dataUrl;
            String header = "data:image";
            String[] imageArr = image.split(",");
            if (imageArr[0].contains(header)) {
                image = imageArr[1];
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] decodedBytes = decoder.decodeBuffer(image);
                FileUtil.uploadFile(decodedBytes, filePath, fileName);
                User user = super.getUser();
                userRepository.setBackgroundPicture(savePath, user.getId());
                user.setBackgroundPicture(savePath);
                getSession().setAttribute(ConstUtil.LOGIN_SESSION_KEY, user);
            }
            return new ResponseData(ExceptionMsg.SUCCESS, savePath);
        } catch (Exception e) {
            logger.error("upload background picture failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }
}
