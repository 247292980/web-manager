package com.lgp.webmanager.service.impl;

import com.lgp.webmanager.comm.aop.LoggerManage;
import com.lgp.webmanager.domain.*;
import com.lgp.webmanager.domain.enums.StatusEnum;
import com.lgp.webmanager.domain.view.CollectSummaryView;
import com.lgp.webmanager.repository.*;
import com.lgp.webmanager.service.CollectService;
import com.lgp.webmanager.service.FavoritesService;
import com.lgp.webmanager.service.NoticeService;
import com.lgp.webmanager.util.DateUtil;
import com.lgp.webmanager.util.HtmlUtil;
import com.lgp.webmanager.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 收藏服务
 **/
@Service("collectService")
public class CollectServiceImpl extends UrlLibraryServiceImpl implements CollectService {
    protected org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private FavoritesRepository favoritesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PraiseRepository praiseRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private FavoritesService favoritesService;
    @Autowired
    private NoticeService noticeService;

    /**
     * 展示收藏列表
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    @LoggerManage(description = "展示收藏列表")
    public List<CollectSummary> getCollects(String type, Long userId, Pageable pageable, Long favoritesId, Long specUserId) {
        Page<CollectSummaryView> views = null;
        if ("my".equals(type)) {
            List<Long> userIds = followRepository.findMyFollowIdByUserId(userId);
            if (userIds == null || userIds.size() == 0) {
                views = collectRepository.findViewByUserId(userId, pageable);
            } else {
                views = collectRepository.findViewByUserIdAndFollows(userId, userIds, pageable);
            }
        } else if ("myself".equals(type)) {
            views = collectRepository.findViewByUserId(userId, pageable);
        } else if ("explore".equals(type)) {
            views = collectRepository.findExploreView(userId, pageable);
        } else if ("others".equals(type)) {
            views = collectRepository.findViewByUserIdAndType(userId, pageable, StatusEnum.COLLECT_TYPE_PUBLIC.getValue());
            if (null != specUserId) {
                userId = specUserId;
            }
        } else if ("otherpublic".equals(type)) {
            views = collectRepository.findViewByUserIdAndTypeAndFavoritesId(userId, pageable, StatusEnum.COLLECT_TYPE_PUBLIC.getValue(), favoritesId);
            if (null != specUserId) {
                userId = specUserId;
            }
        } else if ("garbage".equals(type)) {
            views = collectRepository.findViewByUserIdAndIsDelete(userId, pageable);
        } else {
            views = collectRepository.findViewByFavoritesId(Long.parseLong(type), pageable);
        }
        return convertCollect(views, userId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    @LoggerManage(description = "查询自己")
    public List<CollectSummary> searchMy(Long userId, String key, Pageable pageable) {
        Page<CollectSummaryView> views = collectRepository.searchMyByKey(userId, "%" + key + "%", pageable);
        return convertCollect(views, userId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    @LoggerManage(description = "查询他人")
    public List<CollectSummary> searchOther(Long userId, String key, Pageable pageable) {
        Page<CollectSummaryView> views = collectRepository.searchOtherByKey(userId, "%" + key + "%", pageable);
        return convertCollect(views, userId);
    }

    /**
     * 收藏文章
     */
    @Override
    @LoggerManage(description = "收藏文章")
    @Transactional(rollbackFor = {Exception.class})
    public void saveCollect(Collect collect) {
        if (StringUtils.isNotBlank(collect.getNewFavorites())) {
            collect.setFavoritesId(createfavorites(collect.getNewFavorites(), collect.getUserId()));
        } else {
            favoritesRepository.increaseCountById(collect.getFavoritesId(), DateUtil.getCurrentTime());
        }
        if (collect.getType() == null) {
            collect.setType(StatusEnum.COLLECT_TYPE_PUBLIC.getValue());
        } else {
            collect.setType(StatusEnum.COLLECT_TYPE_PRIVATE.getValue());
        }
        if (StringUtils.isBlank(collect.getDescription())) {
            collect.setDescription(collect.getTitle());
        }
        collect.setIsDelete(StatusEnum.IS_DELETE_NO.getValue());
        collect.setCreateTime(DateUtil.getCurrentTime());
        collect.setLastModifyTime(DateUtil.getCurrentTime());
        collectRepository.save(collect);
        noticeFriends(collect);
    }

    /**
     * 修改文章
     */
    @Override
    @LoggerManage(description = "修改文章")
    @Transactional(rollbackFor = {Exception.class})
    public void updateCollect(Collect newCollect) {
        Collect collect = collectRepository.findOne(newCollect.getId());
        if (StringUtils.isNotBlank(newCollect.getNewFavorites())) {
            collect.setFavoritesId(createfavorites(newCollect.getNewFavorites(), collect.getUserId()));
        } else if (!collect.getFavoritesId().equals(newCollect.getFavoritesId()) && !StatusEnum.IS_DELETE_YES.getValue().equals(collect.getIsDelete())) {
            favoritesRepository.reduceCountById(collect.getFavoritesId(), DateUtil.getCurrentTime());
            favoritesRepository.increaseCountById(newCollect.getFavoritesId(), DateUtil.getCurrentTime());
            collect.setFavoritesId(newCollect.getFavoritesId());
        }
        if (StatusEnum.IS_DELETE_YES.getValue().equals(collect.getIsDelete())) {
            collect.setIsDelete(StatusEnum.IS_DELETE_NO.getValue());
            if (StringUtils.isBlank(newCollect.getNewFavorites())) {
                favoritesRepository.increaseCountById(newCollect.getFavoritesId(), DateUtil.getCurrentTime());
                collect.setFavoritesId(newCollect.getFavoritesId());
            }
        }
        if (newCollect.getType() == null) {
            collect.setType(StatusEnum.COLLECT_TYPE_PUBLIC.getValue());
        } else {
            collect.setType(StatusEnum.COLLECT_TYPE_PRIVATE.getValue());
        }
        collect.setTitle(newCollect.getTitle());
        collect.setDescription(newCollect.getDescription());
        collect.setLogoUrl(newCollect.getLogoUrl());
        collect.setRemark(newCollect.getRemark());
        collect.setLastModifyTime(DateUtil.getCurrentTime());
        collectRepository.save(collect);
        noticeFriends(collect);
    }

    /**
     * 收藏别人的文章
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    @LoggerManage(description = "收藏别人的文章")
    public void otherCollect(Collect collect) {
        Collect other = collectRepository.findOne(collect.getId());
        //收藏别人文章默认给点赞
        like(collect.getUserId(), other.getId());
        if (StringUtils.isNotBlank(collect.getNewFavorites())) {
            collect.setFavoritesId(createfavorites(collect.getNewFavorites(), collect.getUserId()));
        } else {
            favoritesRepository.increaseCountById(collect.getFavoritesId(), DateUtil.getCurrentTime());
        }
        collect.setId(null);
        collect.setIsDelete(StatusEnum.IS_DELETE_NO.getValue());
        if (collect.getType() == null) {
            collect.setType(StatusEnum.COLLECT_TYPE_PUBLIC.getValue());
        } else {
            collect.setType(StatusEnum.COLLECT_TYPE_PRIVATE.getValue());
        }
        if (StringUtils.isBlank(collect.getDescription())) {
            collect.setDescription(collect.getTitle());
        }
        collect.setUrl(other.getUrl());
        collect.setLastModifyTime(DateUtil.getCurrentTime());
        collect.setCreateTime(DateUtil.getCurrentTime());
        collectRepository.save(collect);
        noticeFriends(collect);
    }

    /**
     * 验证是否重复收藏
     */
    @Override
    @LoggerManage(description = "验证是否重复收藏")
    @Transactional(rollbackFor = {Exception.class})
    public boolean checkCollect(Collect collect) {
        if (StringUtils.isNotBlank(collect.getNewFavorites())) {
            // url+favoritesId+userId
            Favorites favorites = favoritesRepository.findByUserIdAndName(collect.getUserId(), collect.getNewFavorites());
            if (null == favorites) {
                return true;
            } else {
                List<Collect> list = collectRepository.findByFavoritesIdAndUrlAndUserIdAndIsDelete(favorites.getId(), collect.getUrl(), collect.getUserId(), StatusEnum.IS_DELETE_NO.getValue());
                if (null != list && list.size() > 0) {
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            if (collect.getId() != null) {
                Collect c = collectRepository.findOne(collect.getId());
                if (c.getFavoritesId().longValue() == collect.getFavoritesId().longValue()) {
                    return true;
                } else {
                    List<Collect> list = collectRepository.findByFavoritesIdAndUrlAndUserIdAndIsDelete(collect.getFavoritesId(), collect.getUrl(), collect.getUserId(), StatusEnum.IS_DELETE_NO.getValue());
                    if (null != list && list.size() > 0) {
                        return false;
                    } else {
                        return true;
                    }
                }
            } else {
                List<Collect> list = collectRepository.findByFavoritesIdAndUrlAndUserIdAndIsDelete(collect.getFavoritesId(), collect.getUrl(), collect.getUserId(), StatusEnum.IS_DELETE_NO.getValue());
                if (null != list && list.size() > 0) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    /**
     * 导入收藏文章
     */
    @Override
    @LoggerManage(description = "导入收藏文章")
    @Transactional(rollbackFor = {Exception.class})
    public void importHtml(Map<String, String> map, Long favoritesId, Long userId, String type) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                Map<String, String> result = HtmlUtil.getCollectFromUrl(entry.getKey());
                Collect collect = new Collect();
                collect.setCharset(result.get("charset"));
                if (StringUtils.isBlank(result.get("title"))) {
                    collect.setTitle(entry.getValue());
                } else {
                    collect.setTitle(result.get("title"));
                }
                if (StringUtils.isBlank(result.get("description"))) {
                    collect.setDescription(collect.getTitle());
                } else {
                    collect.setDescription(result.get("description"));
                }
                collect.setRemark(entry.getValue());
                collect.setFavoritesId(favoritesId);
                collect.setIsDelete(StatusEnum.IS_DELETE_NO.getValue());
                collect.setLogoUrl(getMap(entry.getKey()));
                if (StatusEnum.COLLECT_TYPE_PRIVATE.getValue().equals(type)) {
                    collect.setType(StatusEnum.COLLECT_TYPE_PRIVATE.getValue());
                } else {
                    collect.setType(StatusEnum.COLLECT_TYPE_PUBLIC.getValue());
                }
                collect.setUrl(entry.getKey());
                collect.setUserId(userId);
                collect.setCreateTime(DateUtil.getCurrentTime());
                collect.setLastModifyTime(DateUtil.getCurrentTime());
                List<Collect> list = collectRepository.findByFavoritesIdAndUrlAndUserIdAndIsDelete(favoritesId, entry.getKey(), userId, StatusEnum.IS_DELETE_NO.getValue());
                if (null != list && list.size() > 0) {
                    logger.info("收藏夹：" + favoritesId + "中已经存在：" + entry.getKey() + "这个文章，不在进行导入操作");
                    continue;
                }
                collectRepository.save(collect);
                favoritesRepository.increaseCountById(favoritesId, DateUtil.getCurrentTime());
            } catch (Exception e) {
                logger.error("导入存储异常：", e);
            }
        }

    }

    /**
     * 导出到html文件
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    @LoggerManage(description = "导出到html文件")
    public StringBuilder exportToHtml(Long favoritesId) {
        try {
            Favorites favorites = favoritesRepository.findOne(favoritesId);
            StringBuilder sb = new StringBuilder();
            List<Collect> collects = collectRepository.findByFavoritesIdAndIsDelete(favoritesId, StatusEnum.IS_DELETE_NO.getValue());
            StringBuilder sbc = new StringBuilder();
            for (Collect collect : collects) {
                sbc.append("<DT><A HREF=\"" + collect.getUrl() + "\" TARGET=\"_blank\">" + collect.getTitle() + "</A></DT>");
            }
            sb.append("<DL><P></P><DT><H3>" + favorites.getName() + "</H3><DL><P></P>" + sbc + "</DL></DT></DL>");
            return sb;
        } catch (Exception e) {
            logger.error("异常：", e);
        }
        return null;
    }


    /**
     * 收藏文章默认点赞
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    @LoggerManage(description = "收藏文章默认点赞")
    public void like(Long userId, long id) {
        Praise praise = praiseRepository.findByUserIdAndCollectId(userId, id);
        if (praise == null) {
            Praise newPraise = new Praise();
            newPraise.setUserId(userId);
            newPraise.setCollectId(id);
            newPraise.setCreateTime(DateUtil.getCurrentTime());
            praiseRepository.save(newPraise);
            // 保存消息通知
            Collect collect = collectRepository.findOne(id);
            if (null != collect) {
                noticeService.saveNotice(String.valueOf(id), "praise", collect.getUserId(), String.valueOf(newPraise.getId()));
            }
        } else if (praise.getUserId().equals(userId)) {
            praiseRepository.delete(praise.getId());
        }
    }

    /**
     * 通知好友
     */
    @LoggerManage(description = "通知好友")
    private void noticeFriends(Collect collect) {
        if (StringUtils.isNotBlank(collect.getRemark()) && collect.getRemark().indexOf("@") > -1) {
            List<String> atUsers = StringUtil.getAtUser(collect.getRemark());
            for (String str : atUsers) {
                User user = userRepository.findByUserName(str);
                if (null != user) {
                    // 保存消息通知
                    noticeService.saveNotice(String.valueOf(collect.getId()), "at", user.getId(), null);
                } else {
                    logger.info("为找到匹配：" + str + "的用户.");
                }
            }
        }
    }

    private Long createfavorites(String fname, Long userId) {
        Favorites favorites = favoritesRepository.findByUserIdAndName(userId, fname);
        if (null == favorites) {
            favorites = favoritesService.saveFavorites(userId, 1L, fname);
        } else {
            favoritesRepository.increaseCountById(favorites.getId(), DateUtil.getCurrentTime());
        }
        return favorites.getId();
    }

    private List<CollectSummary> convertCollect(Page<CollectSummaryView> views, Long userId) {
        List<CollectSummary> summarys = new ArrayList<CollectSummary>();
        for (CollectSummaryView view : views) {
            CollectSummary summary = new CollectSummary(view);
            summary.setPraiseCount(praiseRepository.countByCollectId(view.getId()));
            summary.setCommentCount(commentRepository.countByCollectId(view.getId()));
            Praise praise = praiseRepository.findByUserIdAndCollectId(userId, view.getId());
            if (praise != null) {
                summary.setPraise(true);
            } else {
                summary.setPraise(false);
            }
            summary.setCollectTime(DateUtil.getTimeFormatText(view.getCreateTime()));
            summarys.add(summary);
        }
        return summarys;
    }
}