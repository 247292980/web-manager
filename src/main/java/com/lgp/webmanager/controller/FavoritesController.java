package com.lgp.webmanager.controller;

import com.lgp.webmanager.comm.aop.Log;
import com.lgp.webmanager.domain.Config;
import com.lgp.webmanager.domain.Favorites;
import com.lgp.webmanager.domain.enums.ExceptionMsg;
import com.lgp.webmanager.domain.enums.StatusEnum;
import com.lgp.webmanager.domain.result.Response;
import com.lgp.webmanager.domain.result.ResponseData;
import com.lgp.webmanager.repository.CollectRepository;
import com.lgp.webmanager.repository.ConfigRepository;
import com.lgp.webmanager.repository.FavoritesRepository;
import com.lgp.webmanager.service.FavoritesService;
import com.lgp.webmanager.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 收藏夹控制器
 **/
@RestController
@RequestMapping("/favorites")
public class FavoritesController extends BaseController {

    @Autowired
    private FavoritesRepository favoritesRepository;
    @Resource
    private FavoritesService favoritesService;
    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private ConfigRepository configRespository;

    /**
     * 新建收藏夹
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @Log(description = "新建收藏夹")
    public Response addFavorites(String name) {
        if (StringUtils.isNotBlank(name)) {
            Favorites favorites = favoritesRepository.findByUserIdAndName(super.getUserId(), name);
            if (null != favorites) {
                logger.info("收藏夹名称已被创建");
                return result(ExceptionMsg.FavoritesNameUsed);
            } else {
                try {
                    favoritesService.saveFavorites(super.getUserId(), 0L, name);
                } catch (Exception e) {
                    logger.error("异常：", e);
                    return result(ExceptionMsg.FAILED);
                }
            }
        } else {
            logger.info("收藏夹名称为空");
            return result(ExceptionMsg.FavoritesNameIsNull);
        }
        return result();
    }

    /**
     * 导入收藏夹
     */
    @RequestMapping(value = "/addImportFavorites", method = RequestMethod.POST)
    @Log(description = "导入收藏夹")
    public ResponseData addImportFavorites() {
        Favorites favorites = favoritesRepository.findByUserIdAndName(super.getUserId(), "导入自浏览器");
        if (null == favorites) {
            try {
                favorites = favoritesService.saveFavorites(super.getUserId(), 0L, "导入自浏览器");
            } catch (Exception e) {
                logger.error("异常：", e);
            }
        }
        return new ResponseData(ExceptionMsg.SUCCESS, favorites.getId());
    }

    /**
     * 修改收藏夹
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @Log(description = "修改收藏夹")
    public Response updateFavorites(String favoritesName, Long favoritesId) {
        if (StringUtils.isNotBlank(favoritesName) && null != favoritesId) {
            Favorites fav = favoritesRepository.findOne(favoritesId);
            if (null != fav && super.getUserId().longValue() == fav.getUserId().longValue()) {
                Favorites favorites = favoritesRepository.findByUserIdAndName(super.getUserId(), favoritesName);
                if (null != favorites) {
                    logger.info("收藏夹名称已被创建");
                    return result(ExceptionMsg.FavoritesNameUsed);
                } else {
                    try {
                        favoritesRepository.updateNameById(favoritesId, DateUtil.getCurrentTime(), favoritesName);
                    } catch (Exception e) {
                        logger.error("修改收藏夹名称异常：", e);
                    }
                }
            }
        } else {
            logger.info("参数错误name:" + favoritesName + "----" + "id:" + favoritesId);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }

    /**
     * 删除收藏夹
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @Log(description = "删除收藏夹")
    public Response delFavorites(Long id) {
        if (null == id) {
            return result(ExceptionMsg.FAILED);
        }
        try {
            Favorites fav = favoritesRepository.findOne(id);
            if (null != fav && super.getUserId().longValue() == fav.getUserId().longValue()) {
                favoritesRepository.delete(id);
                // 删除该收藏夹下文章
                collectRepository.deleteByFavoritesId(id);
                Config config = configRespository.findByUserIdAndDefaultFavorties(super.getUserId(), String.valueOf(id));
                if (null != config) {
                    // 默认收藏夹被删除，设置“未读列表”为默认收藏夹
                    Favorites favorites = favoritesRepository.findByUserIdAndName(super.getUserId(), "未读列表");
                    if (null != favorites) {
                        configRespository.updateFavoritesById(config.getId(), String.valueOf(favorites.getId()), DateUtil.getCurrentTime());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("删除异常：", e);
        }
        return result();
    }

    /**
     * 获取收藏夹
     */
    @RequestMapping(value = "/getFavorites/{userId}", method = RequestMethod.POST)
    @Log(description = "获取收藏夹")
    public List<Favorites> getFavorites(@PathVariable("userId") Long userId) {
        List<Favorites> favorites = null;
        try {
            Long id = super.getUserId();
            if (null != userId && 0 != userId) {
                id = userId;
            }
            favorites = favoritesRepository.findByUserIdOrderByIdDesc(id);
            if (!super.getUserId().equals(userId)) {
                for (Favorites favorites2 : favorites) {
                    favorites2.setPublicCount(collectRepository.countByFavoritesIdAndTypeAndIsDelete(favorites2.getId(), StatusEnum.COLLECT_TYPE_PUBLIC.getValue(), StatusEnum.IS_DELETE_NO.getValue()));
                }
            }
        } catch (Exception e) {
            logger.error("getFavorites failed, ", e);
        }
        return favorites;
    }
}
