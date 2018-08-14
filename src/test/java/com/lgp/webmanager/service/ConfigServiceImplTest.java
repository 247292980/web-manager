//package com.lgp.webmanager.service;
//
//import com.lgp.webmanager.domain.Collect;
//import com.lgp.webmanager.domain.Config;
//import com.lgp.webmanager.service.impl.ConfigServiceImpl;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * @AUTHOR lgp
// * @DATE 2018/2/6 16:27
// * @DESCRIPTION
// **/
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ConfigServiceImplTest {
//    protected Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    private ConfigService configService;
//
//    /**
//     * 属性修改
//     */
//    @Test
//    public void updateConfigTest() {
//        Long id = 1L;
//        String type = "hahahah";
//        String defaultFavorites = "hahahah";
//        configService.updateConfig(id, type,defaultFavorites);
//    }
//    /**
//     * 保存属性设置
//     */
//    @Test
//    public void saveConfigTest() {
//        Long userId = 1L;
//        String favoritesId = "hahahah";
//        Config config = configService.saveConfig(userId, favoritesId);
//        logger.info("CollectServiceImplTest||searchMyTest||config={} ", config.toString());
//    }
//}
