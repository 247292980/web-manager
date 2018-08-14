package com.lgp.webmanager.service.impl;

import com.lgp.webmanager.comm.aop.LoggerManage;
import com.lgp.webmanager.domain.Config;
import com.lgp.webmanager.repository.ConfigRepository;
import com.lgp.webmanager.service.ConfigService;
import com.lgp.webmanager.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 设置服务
 **/
@Service("configService")
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigRepository configRepository;

    /**
     * 保存属性设置
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    @LoggerManage(description = "保存属性设置")
    public Config saveConfig(Long userId, String favoritesId) {
        Config config = new Config();
        config.setUserId(userId);
        config.setDefaultModel("simple");
        config.setDefaultFavorties(favoritesId);
        config.setDefaultCollectType("public");
        config.setCreateTime(DateUtil.getCurrentTime());
        config.setLastModifyTime(DateUtil.getCurrentTime());
        configRepository.save(config);
        return config;
    }

    /**
     * 属性修改
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    @LoggerManage(description = "保存属性设置")
    public void updateConfig(Long id, String type, String defaultFavorites) {
        Config config = configRepository.findOne(id);
        String value = "";
        if ("defaultCollectType".equals(type)) {
            if ("public".equals(config.getDefaultCollectType())) {
                value = "private";
            } else {
                value = "public";
            }
            configRepository.updateCollectTypeById(id, value, DateUtil.getCurrentTime());
        } else if ("defaultFavorites".equals(type)) {
            configRepository.updateFavoritesById(id, defaultFavorites, DateUtil.getCurrentTime());
        }
    }
}