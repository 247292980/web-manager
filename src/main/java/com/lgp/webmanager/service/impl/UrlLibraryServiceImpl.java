package com.lgp.webmanager.service.impl;

import com.lgp.webmanager.comm.aop.LoggerManage;
import com.lgp.webmanager.domain.UrlLibrary;
import com.lgp.webmanager.repository.UrlLibraryRepository;
import com.lgp.webmanager.service.UrlLibraryService;
import com.lgp.webmanager.util.HtmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 网页存储记录服务
 **/
@Service("urlLibraryService")
public class UrlLibraryServiceImpl implements UrlLibraryService {

    private Map<String, String> maps = new HashMap<String, String>();
    @Autowired
    private UrlLibraryRepository urlLibraryRepository;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    @LoggerManage(description = "得到网页存储记录")
    public String getMap(String key) {
        if (maps.isEmpty()) {
            List<UrlLibrary> collectLibraryList = urlLibraryRepository.findAll();
            for (UrlLibrary urlLibrary : collectLibraryList) {
                maps.put(urlLibrary.getUrl(), urlLibrary.getLogoUrl());
            }
        }
        if (null == maps.get(key)) {
            this.addMaps(key);
        }
        return maps.get(key);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    @LoggerManage(description = "添加网页存储记录")
    public void addMaps(String key) {
        String logoUrl = HtmlUtil.getImage(key);
        maps.put(key, logoUrl);
        UrlLibrary urlLibrary = new UrlLibrary();
        urlLibrary.setUrl(key);
        urlLibrary.setLogoUrl(logoUrl);
        urlLibraryRepository.save(urlLibrary);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    @LoggerManage(description = "刷新网页存储记录")
    public boolean refreshOne(String key, String newValue) {
        if (StringUtils.isNotBlank(key)) {
            String value = getMap(key);
            if (StringUtils.isNotBlank(newValue) && !newValue.equals(value)) {
                maps.put(key, newValue);
                return true;
            }
        }
        return false;
    }
}
