package com.lgp.webmanager.controller;

import com.lgp.webmanager.comm.aop.Log;
import com.lgp.webmanager.domain.*;
import com.lgp.webmanager.domain.enums.StatusEnum;
import com.lgp.webmanager.repository.*;
import com.lgp.webmanager.service.CollectService;
import com.lgp.webmanager.service.CollectorService;
import com.lgp.webmanager.util.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 首页控制器
 **/
@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

    @Autowired
    private FavoritesRepository favoritesRepository;
    @Autowired
    private ConfigRepository configRepository;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private CollectorService collectorService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private UserRepository userRepository;

    /**
     * 首页
     * */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @Log(description = "首页")
    public String index(Model model) {
//        IndexCollector indexCollector = collectorService.getCollectors();
//        model.addAttribute("collector", indexCollector);
        User user = super.getUser();
        if (null != user) {
            model.addAttribute("user", user);
        }
        return "index";
    }

    /**
     * 登陆后首页
     * */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @Log(description = "登陆后首页")
    public String home(Model model) {
        long size = collectRepository.countByUserIdAndIsDelete(super.getUserId(),StatusEnum.IS_DELETE_NO.getValue());
        Config config = configRepository.findByUserId(super.getUserId());
//        Favorites favorites = favoritesRepository.findOne(Long.parseLong(config.getDefaultFavorties()));
        List<String> followList = followRepository.findByUserId(super.getUserId());
        model.addAttribute("config", config);
//        model.addAttribute("favorites", favorites);
        model.addAttribute("size", size);
        model.addAttribute("followList", followList);
        model.addAttribute("user", super.getUser());
        model.addAttribute("newAtMeCount", noticeRepository.countByUserIdAndTypeAndReaded(super.getUserId(), "at", "unread"));
        model.addAttribute("newCommentMeCount", noticeRepository.countByUserIdAndTypeAndReaded(super.getUserId(), "comment", "unread"));
        model.addAttribute("newPraiseMeCount", noticeRepository.countPraiseByUserIdAndReaded(super.getUserId(), "unread"));
        return "home";
    }

    /**
     * 登陆页面
     * */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @Log(description = "登陆页面")
    public String login() {
        return "login";
    }

    /**
     * 注册页面
     * */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @Log(description = "注册页面")
    public String regist() {
        return "register";
    }

    /**
     * 工具页面
     */
    @RequestMapping(value = "/tool")
    @Log(description = "工具页面")
    public String tool(Model model) {
        String path = "javascript:(function()%7Bvar%20description;var%20desString=%22%22;var%20metas=document.getElementsByTagName('meta');for(var%20x=0,y=metas.length;x%3Cy;x++)%7Bif(metas%5Bx%5D.name.toLowerCase()==%22description%22)%7Bdescription=metas%5Bx%5D;%7D%7Dif(description)%7BdesString=%22&amp;description=%22+encodeURIComponent(description.content);%7Dvar%20win=window.open(%22"
                + ConstUtil.BASE_PATH
                + "collect?from=webtool&url=%22+encodeURIComponent(document.URL)+desString+%22&title=%22+encodeURIComponent(document.title)+%22&charset=%22+document.charset,'_blank');win.focus();%7D)();";
        model.addAttribute("path", path);
        return "tool";
    }

    /**
     * 收藏夹导入页面
     * */
    @RequestMapping(value = "/import")
    @Log(description = "收藏夹导入页面")
    public String importm() {
        return "favorites/import";
    }

    /**
     * 新建收藏夹页面
     * */
    @RequestMapping(value = "/newFavorites")
    @Log(description = "新建收藏夹页面")
    public String newFavorites() {
        return "favorites/newfavorites";
    }

    /**
     * 意见反馈页面
     * */
    @RequestMapping(value = "/feedback")
    @Log(description = "意见反馈页面")
    public String feedback(Model model) {
        User user = null;
        user = userRepository.findOne(getUserId());
        model.addAttribute("user", user);
        return "favorites/feedback";
    }

    /**
     * 收藏页面
     * */
    @RequestMapping(value = "/collect", method = RequestMethod.GET)
    @Log(description = "收藏页面")
    public String collect(Model model) {
        List<Favorites> favoritesList = favoritesRepository.findByUserId(getUserId());
        Config config = configRepository.findByUserId(getUserId());
        List<String> followList = followRepository.findByUserId(getUserId());
        logger.info("model：" + config.getDefaultModel());
        model.addAttribute("favoritesList", favoritesList);
        model.addAttribute("configObj", config);
        model.addAttribute("followList", followList);
        return "collect";
    }

    /**
     * 登出
     * */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @Log(description = "登出")
    public String logout(HttpServletResponse response, Model model) {
        getSession().removeAttribute(ConstUtil.LOGIN_SESSION_KEY);
        getSession().removeAttribute(ConstUtil.LAST_REFERER);
        Cookie cookie = new Cookie(ConstUtil.LOGIN_SESSION_KEY, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
//        IndexCollector indexCollector = collectorService.getCollectors();
//        model.addAttribute("collector", indexCollector);
        return "index";
    }

    /**
     * 忘记密码页面
     * */
    @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
    @Log(description = "忘记密码页面")
    public String forgotPassword() {
        return "user/forgotpassword";
    }

    /**
     * 忘记密码-设置新密码
     * */
    @RequestMapping(value = "/newPassword", method = RequestMethod.GET)
    @Log(description = "忘记密码-设置新密码")
    public String newPassword(String email) {
        return "user/newpassword";
    }

    /**
     * 上传你头像页面
     * */
    @RequestMapping(value = "/uploadHeadPortrait")
    @Log(description = "上传你头像页面")
    public String uploadHeadPortrait() {
        return "user/uploadheadportrait";
    }

    /**
     * 收藏夹导出页面
     * */
    @RequestMapping(value = "/export")
    @Log(description = "收藏夹导出页面")
    public String export(Model model) {
        List<Favorites> favoritesList = favoritesRepository.findByUserId(getUserId());
        model.addAttribute("favoritesList", favoritesList);
        return "favorites/export";
    }

    /**
     * 上传背景页面
     * */
    @RequestMapping(value = "/uploadBackground")
    @Log(description = "上传背景页面")
    public String uploadBackground() {
        return "user/uploadbackground";
    }

    /**
     * 首页收藏家个人首页
     */
    @RequestMapping(value = "/collector/{userId}/{favoritesId:[0-9]*}")
    @Log(description = "首页收藏家个人首页")
    public String collectorPageShow(Model model
            , @PathVariable("userId") Long userId
            , @PathVariable("favoritesId") Long favoritesId
            , @RequestParam(value = "page", defaultValue = "0") Integer page
            , @RequestParam(value = "size", defaultValue = "15") Integer size) {
        User user = userRepository.findOne(userId);
        Long collectCount = 0L;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        List<CollectSummary> collects = null;
        Integer isFollow = 0;
        if (super.getUserId().longValue() == userId.longValue()) {
            model.addAttribute("myself", StatusEnum.IS_DELETE_YES.getValue());
            collectCount = collectRepository.countByUserIdAndIsDelete(userId, StatusEnum.IS_DELETE_NO.getValue());
            if (0 == favoritesId) {
                collects = collectService.getCollects("myself", userId, pageable, null, null);
            } else {
                collects = collectService.getCollects(String.valueOf(favoritesId), userId, pageable, 0L, null);
            }
        } else {
            model.addAttribute("myself", StatusEnum.IS_DELETE_NO.getValue());
            collectCount = collectRepository.countByUserIdAndTypeAndIsDelete(userId, StatusEnum.COLLECT_TYPE_PUBLIC.getValue(),StatusEnum.IS_DELETE_NO.getValue());
            if (favoritesId == 0) {
                collects = collectService.getCollects("others", userId, pageable, null, getUserId());
            } else {
                collects = collectService.getCollects("otherpublic", userId, pageable, favoritesId, getUserId());
            }
            isFollow = followRepository.countByUserIdAndFollowIdAndStatus(getUserId(), userId, StatusEnum.FOLLOW_STATUS_FOLLOW.getValue());
        }
        Integer follow = followRepository.countByUserIdAndStatus(userId, StatusEnum.FOLLOW_STATUS_FOLLOW.getValue());
        Integer followed = followRepository.countByFollowIdAndStatus(userId, StatusEnum.FOLLOW_STATUS_FOLLOW.getValue());
        List<Favorites> favoritesList = favoritesRepository.findByUserId(userId);
        List<String> followUser = followRepository.findFollowUserByUserId(userId);
        List<String> followedUser = followRepository.findFollowedUserByFollowId(userId);
        Config config = configRepository.findByUserId(getUserId());
        if (getUserId() == null || getUserId() == 0) {
            config = configRepository.findByUserId(userId);
        }
        model.addAttribute("collectCount", collectCount);
        model.addAttribute("follow", follow);
        model.addAttribute("followed", followed);
        model.addAttribute("user", user);
        model.addAttribute("collects", collects);
        model.addAttribute("favoritesList", favoritesList);
        model.addAttribute("followUser", followUser);
        model.addAttribute("followedUser", followedUser);
        model.addAttribute("isFollow", isFollow);
        model.addAttribute("loginUserInfo", getUser());
        model.addAttribute("config", config);
        model.addAttribute("configObj", config);
        return "collector";
    }
}