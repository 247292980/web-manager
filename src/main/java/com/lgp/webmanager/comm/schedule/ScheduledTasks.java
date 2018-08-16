package com.lgp.webmanager.comm.schedule;


import com.lgp.webmanager.comm.aop.Log;
import com.lgp.webmanager.domain.Collect;
import com.lgp.webmanager.domain.UrlLibrary;
import com.lgp.webmanager.domain.enums.StatusEnum;
import com.lgp.webmanager.repository.CollectRepository;
import com.lgp.webmanager.repository.FavoritesRepository;
import com.lgp.webmanager.repository.UrlLibraryRepository;
import com.lgp.webmanager.service.UrlLibraryService;
import com.lgp.webmanager.util.ConstUtil;
import com.lgp.webmanager.util.DateUtil;
import com.lgp.webmanager.util.HtmlUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 定时任务
 **/
@Component
public class ScheduledTasks {

    protected org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CollectRepository collectRespository;
    @Autowired
    private FavoritesRepository favoritesRespository;
    @Autowired
    private UrlLibraryRepository urlLibraryRepository;
    @Autowired
    private UrlLibraryService urlLibraryService;

    /**
     * 一个cron表达式有至少6个（也可能7个，多了年）有空格分隔的时间元素
     * 秒 分 时 日 月 星期 年
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    @Log(description = "回收站定时")
    public void autoRecovery() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.DAY_OF_YEAR, -30);
        Long date = ca.getTime().getTime();
        List<Long> favoritesId = favoritesRespository.findIdByName("未读列表");
        List<Collect> collectList = collectRespository.findByCreateTimeLessThanAndIsDeleteAndFavoritesIdIn(date, StatusEnum.IS_DELETE_NO.getValue(), favoritesId);
        for (Collect collect : collectList) {
            try {
                collectRespository.modifyIsDeleteById(StatusEnum.IS_DELETE_YES.getValue(), DateUtil.getCurrentTime(), collect.getId());
                favoritesRespository.reduceCountById(collect.getFavoritesId(), DateUtil.getCurrentTime());
            } catch (Exception e) {
                logger.error("回收站定时任务异常：", e);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Log(description = "获取图片logoUrl定时")
    public void getImageLogoUrl() {
        List<UrlLibrary> urlLibraryList = urlLibraryRepository.findByCountLessThanAndLogoUrl(10, ConstUtil.BASE_PATH + ConstUtil.DEFAULT_LOGO);
        for (UrlLibrary urlLibrary : urlLibraryList) {
            try {
                String logoUrl = HtmlUtil.getImage(urlLibrary.getUrl());
                // 刷新缓存
                boolean result = urlLibraryService.refreshOne(urlLibrary.getUrl(), logoUrl);
                if (result) {
                    collectRespository.updateLogoUrlByUrl(logoUrl, DateUtil.getCurrentTime(), urlLibrary.getUrl());
                    urlLibraryRepository.updateLogoUrlById(urlLibrary.getId(), logoUrl);
                } else {
                    urlLibraryRepository.increaseCountById(urlLibrary.getId());
                }
            } catch (Exception e) {
                logger.error("获取图片异常：", e);
            }
        }
    }
}
