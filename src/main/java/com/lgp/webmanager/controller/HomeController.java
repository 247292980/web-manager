package com.lgp.webmanager.controller;

import com.lgp.webmanager.comm.aop.LoggerManage;
import com.lgp.webmanager.domain.CollectSummary;
import com.lgp.webmanager.domain.Favorites;
import com.lgp.webmanager.domain.LetterSummary;
import com.lgp.webmanager.domain.User;
import com.lgp.webmanager.domain.enums.StatusEnum;
import com.lgp.webmanager.repository.*;
import com.lgp.webmanager.service.CollectService;
import com.lgp.webmanager.service.LetterService;
import com.lgp.webmanager.service.LookRecordService;
import com.lgp.webmanager.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * Home控制器
 **/
@Controller
@RequestMapping("/")
public class HomeController extends BaseController {

    @Autowired
    private CollectService collectService;
    @Autowired
    private FavoritesRepository favoritesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private LookRecordService lookRecordService;
    @Autowired
    private LetterService letterService;
    @Autowired
    private NoticeRepository noticeRepository;

    /**
     * 收藏列表standard
     * */
    @RequestMapping(value = "/standard/{type}/{userId}")
    @LoggerManage(description = "收藏列表standard")
    public String standard(Model model
            , @RequestParam(value = "page", defaultValue = "0") Integer page
            , @RequestParam(value = "size", defaultValue = "15") Integer size
            , @PathVariable("type") String type
            , @PathVariable("userId") Long userId) {
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        model.addAttribute("type", type);
        Favorites favorites = new Favorites();
        if (!"my".equals(type) && !"explore".equals(type) && !"garbage".equals(type)) {
            try {
                favorites = favoritesRepository.findOne(Long.parseLong(type));
                favorites.setPublicCount(collectRepository.countByFavoritesIdAndTypeAndIsDelete(favorites.getId(), StatusEnum.COLLECT_TYPE_PUBLIC.getValue(),StatusEnum.IS_DELETE_NO.getValue()));
            } catch (Exception e) {
                logger.error("获取收藏夹异常：", e);
            }
        }
        List<CollectSummary> collects = null;
        if (null != userId && 0 != userId && userId.longValue() != super.getUserId().longValue()) {
            User user = userRepository.findOne(userId);
            model.addAttribute("otherPeople", user);
            collects = collectService.getCollects("otherpublic", userId, pageable, favorites.getId(), null);
        } else {
            collects = collectService.getCollects(type, getUserId(), pageable, null, null);
        }
        model.addAttribute("collects", collects);
        model.addAttribute("favorites", favorites);
        model.addAttribute("userId", super.getUserId());
        model.addAttribute("size", collects.size());
        return "collect/standard";
    }

    /**
     * 收藏列表simple
     * */
    @RequestMapping(value = "/simple/{type}/{userId}")
    @LoggerManage(description = "收藏列表simple")
    public String simple(Model model
            , @RequestParam(value = "page", defaultValue = "0") Integer page
            , @RequestParam(value = "size", defaultValue = "20") Integer size
            , @PathVariable("type") String type
            , @PathVariable("userId") Long userId) {
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        model.addAttribute("type", type);
        Favorites favorites = new Favorites();
        if (!"my".equals(type) && !"explore".equals(type) && !"garbage".equals(type)) {
            try {
                favorites = favoritesRepository.findOne(Long.parseLong(type));
                favorites.setPublicCount(collectRepository.countByFavoritesIdAndTypeAndIsDelete(favorites.getId(), StatusEnum.COLLECT_TYPE_PUBLIC.getValue(), StatusEnum.IS_DELETE_NO.getValue()));
            } catch (Exception e) {
                logger.error("获取收藏夹异常：", e);
            }
        }
        List<CollectSummary> collects = null;
        if (null != userId && 0 != userId && userId.longValue() != super.getUserId().longValue()) {
            User user = userRepository.findOne(userId);
            model.addAttribute("otherPeople", user);
            collects = collectService.getCollects("otherpublic", userId, pageable, favorites.getId(), null);
        } else {
            collects = collectService.getCollects(type, super.getUserId(), pageable, null, null);
        }
        model.addAttribute("collects", collects);
        model.addAttribute("favorites", favorites);
        model.addAttribute("userId", super.getUserId());
        model.addAttribute("size", collects.size());
        return "collect/simple";
    }

    /**
     * 个人首页
     */
    @RequestMapping(value = "/user/{userId}/{favoritesId}")
    @LoggerManage(description = "个人首页")
    public String userPageShow(Model model
            , @PathVariable("userId") Long userId
            , @PathVariable("favoritesId") Long favoritesId
            , @RequestParam(value = "page", defaultValue = "0") Integer page
            , @RequestParam(value = "size", defaultValue = "15") Integer size) {
        User user = userRepository.findOne(userId);
        Long collectCount = 0L;
        Sort sort = new Sort(Direction.DESC, "id");
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
            collectCount = collectRepository.countByUserIdAndTypeAndIsDelete(userId, StatusEnum.COLLECT_TYPE_PUBLIC.getValue(), StatusEnum.IS_DELETE_NO.getValue());
            if (favoritesId == 0) {
                collects = collectService.getCollects("others", userId, pageable, null, super.getUserId());
            } else {
                collects = collectService.getCollects("otherpublic", userId, pageable, favoritesId, super.getUserId());
            }
            isFollow = followRepository.countByUserIdAndFollowIdAndStatus(super.getUserId(), userId, StatusEnum.FOLLOW_STATUS_FOLLOW.getValue());
        }
        Integer follow = followRepository.countByUserIdAndStatus(userId, StatusEnum.FOLLOW_STATUS_FOLLOW.getValue());
        Integer followed = followRepository.countByFollowIdAndStatus(userId, StatusEnum.FOLLOW_STATUS_FOLLOW.getValue());
        List<Favorites> favoritesList = favoritesRepository.findByUserId(userId);
        List<String> followUser = followRepository.findFollowUserByUserId(userId);
        List<String> followedUser = followRepository.findFollowedUserByFollowId(userId);
        model.addAttribute("collectCount", collectCount);
        model.addAttribute("follow", follow);
        model.addAttribute("followed", followed);
        model.addAttribute("user", user);
        model.addAttribute("collects", collects);
        model.addAttribute("favoritesList", favoritesList);
        model.addAttribute("followUser", followUser);
        model.addAttribute("followedUser", followedUser);
        model.addAttribute("isFollow", isFollow);
        User userTemp = null;
        User currentUser = super.getUser();
        if (super.getUser() == null) {
            userTemp = new User();
            userTemp.setId(0L);
        }
        model.addAttribute("loginUser", currentUser == null ? userTemp : currentUser);
        return "user";
    }


    /**
     * 个人首页内容替换
     */
    @RequestMapping(value = "/usercontent/{userId}/{favoritesId}")
    @LoggerManage(description = "个人首页内容替换")
    public String userContentShow(Model model
            , @PathVariable("userId") Long userId
            , @PathVariable("favoritesId") Long favoritesId
            , @RequestParam(value = "page", defaultValue = "0") Integer page
            , @RequestParam(value = "size", defaultValue = "15") Integer size) {
        User user = userRepository.findOne(userId);
        Long collectCount = 0L;
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        List<CollectSummary> collects = null;
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
            collectCount = collectRepository.countByUserIdAndTypeAndIsDelete(userId, StatusEnum.COLLECT_TYPE_PUBLIC.getValue(), StatusEnum.IS_DELETE_NO.getValue());
            if (favoritesId == 0) {
                collects = collectService.getCollects("others", userId, pageable, null, super.getUserId());
            } else {
                collects = collectService.getCollects("otherpublic", userId, pageable, favoritesId, super.getUserId());
            }
        }
        List<Favorites> favoritesList = favoritesRepository.findByUserId(userId);
        model.addAttribute("collectCount", collectCount);
        model.addAttribute("user", user);
        model.addAttribute("collects", collects);
        model.addAttribute("favoritesList", favoritesList);
        model.addAttribute("favoritesId", favoritesId);
        model.addAttribute("loginUser", super.getUser());
        return "fragments/usercontent";
    }

    /**
     * 搜索
     */
    @RequestMapping(value = "/search/{key}")
    @LoggerManage(description = "搜索")
    public String search(Model model
            , @RequestParam(value = "page", defaultValue = "0") Integer page
            , @RequestParam(value = "size", defaultValue = "20") Integer size
            , @PathVariable("key") String key) {
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        List<CollectSummary> myCollects = collectService.searchMy(super.getUserId(), key, pageable);
        List<CollectSummary> otherCollects = collectService.searchOther(super.getUserId(), key, pageable);
        model.addAttribute("myCollects", myCollects);
        model.addAttribute("otherCollects", otherCollects);
        model.addAttribute("userId", super.getUserId());
        model.addAttribute("mysize", myCollects.size());
        model.addAttribute("othersize", otherCollects.size());
        model.addAttribute("key", key);
        return "collect/search";
    }

    /**
     * 消息通知@我
     */
    @RequestMapping(value = "/notice/atMe")
    @LoggerManage(description = "消息通知@我的")
    public String atMe(Model model
            , @RequestParam(value = "page", defaultValue = "0") Integer page
            , @RequestParam(value = "size", defaultValue = "15") Integer size) {
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        List<CollectSummary> collects = noticeService.getNoticeCollects("at", super.getUserId(), pageable);
        model.addAttribute("collects", collects);
        noticeRepository.updateReadedByUserId("read", super.getUserId(), "at");
        return "notice/atme";
    }

    /**
     * 消息通知评论我
     */
    @RequestMapping(value = "/notice/commentMe")
    @LoggerManage(description = "消息通知评论我")
    public String commentMe(Model model
            , @RequestParam(value = "page", defaultValue = "0") Integer page
            , @RequestParam(value = "size", defaultValue = "15") Integer size) {
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        List<CollectSummary> collects = noticeService.getNoticeCollects("comment", super.getUserId(), pageable);
        model.addAttribute("collects", collects);
        noticeRepository.updateReadedByUserId("read", super.getUserId(), "comment");
        return "notice/commentme";
    }

    /**
     * 消息通知赞我的
     */
    @RequestMapping(value = "/notice/praiseMe")
    @LoggerManage(description = "消息通知赞我的")
    public String praiseMe(Model model
            , @RequestParam(value = "page", defaultValue = "0") Integer page
            , @RequestParam(value = "size", defaultValue = "15") Integer size) {
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        List<CollectSummary> collects = noticeService.getNoticeCollects("praise", super.getUserId(), pageable);
        model.addAttribute("collects", collects);
        noticeRepository.updateReadedByUserId("read", super.getUserId(), "praise");
        return "notice/praiseme";
    }

    /**
     * 浏览记录 标准显示
     */
    @RequestMapping(value = "/lookRecord/standard/{type}/{userId}")
    @LoggerManage(description = "浏览记录lookRecord")
    public String getLookRecordStandard(Model model
            , @RequestParam(value = "page", defaultValue = "0") Integer page
            , @RequestParam(value = "size", defaultValue = "15") Integer size
            , @PathVariable("type") String type
            , @PathVariable("userId") Long userId) {
        Sort sort = new Sort(Direction.DESC, "lastModifyTime");
        Pageable pageable = new PageRequest(page, size, sort);
        model.addAttribute("type", "lookRecord");
        Favorites favorites = new Favorites();

        List<CollectSummary> collects = null;
        User user = userRepository.findOne(userId);
        model.addAttribute("otherPeople", user);
        collects = lookRecordService.getLookRecords(super.getUserId(), pageable);

        model.addAttribute("collects", collects);
        model.addAttribute("favorites", favorites);
        model.addAttribute("userId", super.getUserId());
        model.addAttribute("size", collects.size());
        return "lookRecord/standard";
    }

    /**
     * 浏览记录 简单显示
     */
    @RequestMapping(value = "/lookRecord/simple/{type}/{userId}")
    @LoggerManage(description = "浏览记录lookRecord")
    public String getLookRecordSimple(Model model
            , @RequestParam(value = "page", defaultValue = "0") Integer page
            , @RequestParam(value = "size", defaultValue = "20") Integer size
            , @PathVariable("type") String type, @PathVariable("userId") Long userId) {

        Sort sort = new Sort(Direction.DESC, "lastModifyTime");
        Pageable pageable = new PageRequest(page, size, sort);
        model.addAttribute("type", "lookRecord");
        Favorites favorites = new Favorites();
        List<CollectSummary> collects = null;
        User user = userRepository.findOne(userId);
        model.addAttribute("otherPeople", user);
        collects = lookRecordService.getLookRecords(super.getUserId(), pageable);

        model.addAttribute("collects", collects);
        model.addAttribute("favorites", favorites);
        model.addAttribute("userId", super.getUserId());
        model.addAttribute("size", collects.size());
        return "lookRecord/simple";
    }

    @RequestMapping("/letter/letterMe")
    @LoggerManage(description = "私信我的页面展示")
    public String letterMe(Model model
            , @RequestParam(value = "page", defaultValue = "0") Integer page
            , @RequestParam(value = "size", defaultValue = "15") Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        List<LetterSummary> letterList = letterService.findLetter(super.getUserId(), pageable);
        model.addAttribute("letterList", letterList);
        noticeRepository.updateReadedByUserId("read", super.getUserId(), "letter");
        return "notice/letterme";
    }
}