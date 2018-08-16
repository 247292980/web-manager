package com.lgp.webmanager.controller;

import com.lgp.webmanager.comm.aop.Log;
import com.lgp.webmanager.domain.Collect;
import com.lgp.webmanager.domain.CollectSummary;
import com.lgp.webmanager.domain.Favorites;
import com.lgp.webmanager.domain.Praise;
import com.lgp.webmanager.domain.enums.ExceptionMsg;
import com.lgp.webmanager.domain.enums.StatusEnum;
import com.lgp.webmanager.domain.result.Response;
import com.lgp.webmanager.repository.CollectRepository;
import com.lgp.webmanager.repository.FavoritesRepository;
import com.lgp.webmanager.repository.PraiseRepository;
import com.lgp.webmanager.service.CollectService;
import com.lgp.webmanager.service.FavoritesService;
import com.lgp.webmanager.service.LookRecordService;
import com.lgp.webmanager.service.UrlLibraryService;
import com.lgp.webmanager.util.DateUtil;
import com.lgp.webmanager.util.HtmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.lgp.webmanager.util.ConstUtil.BASE_PATH;
import static com.lgp.webmanager.util.ConstUtil.DEFAULT_LOGO;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 收藏控制器
 **/
@RestController
@RequestMapping("/collect")
public class CollectController extends BaseController {
    @Autowired
    private FavoritesService favoritesService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private UrlLibraryService urlLibraryService;
    @Autowired
    private FavoritesRepository favoritesRepository;
    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private PraiseRepository praiseRepository;
    @Autowired
    private LookRecordService lookRecordService;

    /**
     * 收藏收集
     */
    @RequestMapping(value = "/collect", method = RequestMethod.POST)
    @Log(description = "收藏收集")
    public Response collect(Collect collect) {
        try {
            if (StringUtils.isBlank(collect.getLogoUrl())) {
                collect.setLogoUrl(BASE_PATH + DEFAULT_LOGO);
            }
            collect.setUserId(super.getUserId());
            if (collectService.checkCollect(collect)) {
                Collect exist = collectRepository.findByIdAndUserId(collect.getId(), collect.getUserId());
                if (collect.getId() == null) {
                    collectService.saveCollect(collect);
                    //收藏别人的文章
                } else if (exist == null) {
                    collectService.otherCollect(collect);
                } else {
                    collectService.updateCollect(collect);
                }
            } else {
                return result(ExceptionMsg.CollectExist);
            }
        } catch (Exception e) {
            logger.error("collect failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }

    /**
     * 获取收藏页面的LogoUrl
     * */
    @RequestMapping(value = "/getCollectLogoUrl", method = RequestMethod.POST)
    @Log(description = "获取收藏页面的LogoUrl")
    public String getCollectLogoUrl(String url) {
        if (StringUtils.isNotBlank(url)) {
            String logoUrl = urlLibraryService.getMap(url);
            if (StringUtils.isNotBlank(logoUrl)) {
                return logoUrl;
            } else {
                return DEFAULT_LOGO;
            }
        } else {
            return DEFAULT_LOGO;
        }
    }

    /**
     * 根据收藏的标题和描述推荐收藏夹
     */
    @RequestMapping(value = "/getFavoriteResult", method = RequestMethod.POST)
    @Log(description = "获取推荐收藏夹")
    public Map<String, Object> getFavoriteResult(String title, String description) {
        Long result = null;
        int faultPosition = 0;
        Map<String, Object> maps = new HashMap<String, Object>();
        List<Favorites> favoritesList = favoritesRepository.findByUserId(super.getUserId());
        if (0==favoritesList.size()){
            logger.error("获取推荐收藏夹为空");
            return maps;
        }
        for (int i = 0; i < favoritesList.size(); i++) {
            Favorites favorites = favoritesList.get(i);
            if (favorites.getName().indexOf(title) > 0 || favorites.getName().indexOf(description) > 0) {
                result = favorites.getId();
            }
            if ("未读列表".equals(favorites.getName())) {
                faultPosition = i;
            }
        }
        result = result == null ? favoritesList.get(faultPosition).getId() : result;
        maps.put("favoritesResult", result == null ? 0 : result);
        maps.put("favoritesList", favoritesList);
        return maps;
    }

    /**
     * 收藏列表standard
     * */
    @RequestMapping(value = "/standard/{type}/{favoritesId}/{userId}")
    @Log(description = "收藏列表standard")
    public List<CollectSummary> standard(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", defaultValue = "15") Integer size,
                                         @PathVariable("type") String type,
                                         @PathVariable("favoritesId") Long favoritesId,
                                         @PathVariable("userId") Long userId) {
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        List<CollectSummary> collects = null;
        if ("otherpublic".equalsIgnoreCase(type)) {
            if (null != favoritesId && 0 != favoritesId) {
                collects = collectService.getCollects(type, userId, pageable, favoritesId, super.getUserId());
            } else {
                collects = collectService.getCollects("others", userId, pageable, null, super.getUserId());
            }
        } else if (type != null && !"".equals(type) && "lookRecord".equals(type)) {
            //用于浏览记录功能中收藏列表显示
            collects = lookRecordService.getLookRecords(super.getUserId(), pageable);
        } else {
            if (null != favoritesId && 0 != favoritesId) {
                collects = collectService.getCollects(String.valueOf(favoritesId), super.getUserId(), pageable, null, null);
            } else {
                collects = collectService.getCollects(type, super.getUserId(), pageable, null, null);
            }
        }
        return collects;
    }

    /**
     * 收藏列表simple
     * */
    @RequestMapping(value = "/simple/{type}/{favoritesId}/{userId}")
    @Log(description = "收藏列表simple")
    public List<CollectSummary> simple(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                       @RequestParam(value = "size", defaultValue = "15") Integer size,
                                       @PathVariable("type") String type,
                                       @PathVariable("favoritesId") Long favoritesId,
                                       @PathVariable("userId") Long userId) {
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        List<CollectSummary> collects = null;
        if ("otherpublic".equalsIgnoreCase(type)) {
            if (null != favoritesId && 0 != favoritesId) {
                collects = collectService.getCollects(type, userId, pageable, favoritesId, super.getUserId());
            } else {
                collects = collectService.getCollects("others", userId, pageable, null, super.getUserId());
            }
        } else {
            if (null != favoritesId && 0 != favoritesId) {
                collects = collectService.getCollects(String.valueOf(favoritesId), super.getUserId(), pageable, null, null);
            } else {
                collects = collectService.getCollects(type, super.getUserId(), pageable, null, null);
            }
        }
        return collects;
    }

    /**
     * 修改收藏类型
     * */
    @RequestMapping(value = "/changePrivacy/{id}/{type}")
    @Log(description = "修改收藏类型")
    public Response changePrivacy(@PathVariable("id") long id, @PathVariable("type") String type) {
        collectRepository.modifyByIdAndUserId(type, id, super.getUserId());
        return result();
    }

    /**
     * 收藏点赞或者取消点赞
     *
     * */
    @RequestMapping(value = "/like/{id}")
    @Log(description = "收藏点赞或者取消点赞")
    public Response like(@PathVariable("id") long id) {
        try {
            collectService.like(super.getUserId(), id);
        } catch (Exception e) {
            logger.error("文章点赞或者取消点赞异常：", e);
        }
        return result();
    }

    /**
     * 取消收藏
     * */
    @RequestMapping(value = "/delete/{id}")
    @Log(description = "取消收藏")
    public Response delete(@PathVariable("id") Long id) {
        Collect collect = collectRepository.findOne(id);
        if (null != collect && super.getUserId().equals(collect.getUserId())) {
            collectRepository.deleteById(id);
            if (null != collect.getFavoritesId() && !StatusEnum.IS_DELETE_YES.getValue().equals(collect.getIsDelete())) {
                favoritesRepository.reduceCountById(collect.getFavoritesId(), DateUtil.getCurrentTime());
            }
        }
        return result();
    }

    /**
     * 收藏详细信息
     * */
    @RequestMapping(value = "/detail/{id}")
    @Log(description = "收藏详细信息")
    public Collect detail(@PathVariable("id") long id) {
        Collect collect = collectRepository.findOne(id);
        return collect;
    }

    /**
     * 导入收藏夹
     */
    @RequestMapping("/import")
    @Log(description = "导入收藏夹操作")
    public void importCollect(@RequestParam("htmlFile") MultipartFile htmlFile, String structure, String type) {
        try {
            if (StringUtils.isNotBlank(structure) && StatusEnum.IS_DELETE_YES.getValue().toString().equals(structure)) {
                // 按照目录结构导入
                Map<String, Map<String, String>> map = HtmlUtil.parseHtmlTwo(htmlFile.getInputStream());
                if (null == map || map.isEmpty()) {
                    logger.info("未获取到url连接");
                    return;
                }
                for (Entry<String, Map<String, String>> entry : map.entrySet()) {
                    String favoritesName = entry.getKey();
                    Favorites favorites = favoritesRepository.findByUserIdAndName(super.getUserId(), favoritesName);
                    if (null == favorites) {
                        favorites = favoritesService.saveFavorites(super.getUserId(), 0L, favoritesName);
                    }
                    collectService.importHtml(entry.getValue(), favorites.getId(), super.getUserId(), type);
                }
            } else {
                Map<String, String> map = HtmlUtil.parseHtmlOne(htmlFile.getInputStream());
                if (null == map || map.isEmpty()) {
                    logger.info("未获取到url连接");
                    return;
                }
                // 全部导入到<导入自浏览器>收藏夹
                Favorites favorites = favoritesRepository.findByUserIdAndName(super.getUserId(), "导入自浏览器");
                if (null == favorites) {
                    favorites = favoritesService.saveFavorites(super.getUserId(), 0L, "导入自浏览器");
                }
                collectService.importHtml(map, favorites.getId(), super.getUserId(), type);
            }
        } catch (Exception e) {
            logger.error("导入html异常:", e);
        }
    }

    /**
     * 导出收藏夹
     */
    @RequestMapping("/export")
    @Log(description = "导出收藏夹")
    public void export(String favoritesId, HttpServletResponse response) {
        if (StringUtils.isNotBlank(favoritesId)) {
            try {
                String[] ids = favoritesId.split(",");
                String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String fileName = "favorites_" + date + ".html";
                StringBuilder sb = new StringBuilder();
                for (String id : ids) {
                    try {
                        sb = sb.append(collectService.exportToHtml(Long.parseLong(id)));
                    } catch (Exception e) {
                        logger.error("异常：", e);
                    }
                }
                sb = HtmlUtil.exportHtml("云收藏夹", sb);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-disposition", "attachment; filename=" + fileName);
                response.getWriter().print(sb);
            } catch (Exception e) {
                logger.error("异常：", e);
            }
        }
    }

    /**
     * 查询我的收藏
     * */
    @RequestMapping(value = "/searchMy/{key}")
    @Log(description = "查询我的收藏")
    public List<CollectSummary> searchMy(Model model
            , @RequestParam(value = "page", defaultValue = "0") Integer page
            , @RequestParam(value = "size", defaultValue = "20") Integer size
            , @PathVariable("key") String key) {
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        List<CollectSummary> myCollects = collectService.searchMy(super.getUserId(), key, pageable);
        model.addAttribute("myCollects", myCollects);
        return myCollects;
    }

    /**
     * 查询他人的收藏
     * */
    @RequestMapping(value = "/searchOther/{key}")
    @Log(description = "查询他人的收藏")
    public List<CollectSummary> searchOther(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page,
                                            @RequestParam(value = "size", defaultValue = "20") Integer size, @PathVariable("key") String key) {
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        List<CollectSummary> otherCollects = collectService.searchOther(super.getUserId(), key, pageable);
        return otherCollects;
    }

    /**
     * 查询点赞状态及该文章的点赞数量
     */
    @RequestMapping(value = "/getPaiseStatus/{collectId}")
    @Log(description = "查询点赞状态及该文章的点赞数量")
    public Map<String, Object> getPraiseStatus(Model model, @PathVariable("collectId") Long collectId) {
        Map<String, Object> maps = new HashMap<String, Object>();
        Praise praise = praiseRepository.findByUserIdAndCollectId(super.getUserId(), collectId);
        Long praiseCount = praiseRepository.countByCollectId(collectId);
        maps.put("status", praise != null ? "praise" : "unpraise");
        maps.put("praiseCount", praiseCount);
        return maps;
    }
}