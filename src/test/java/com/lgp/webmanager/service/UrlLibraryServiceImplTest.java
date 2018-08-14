//package com.lgp.webmanager.service;
//
//import com.lgp.webmanager.domain.CollectSummary;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
///**
// * @AUTHOR lgp
// * @DATE 2018/2/7 10:12
// * @DESCRIPTION
// **/
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class UrlLibraryServiceImplTest {
//    protected Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    private UrlLibraryService urlLibraryService;
//
//    /**
//     * 刷新网页存储记录
//     */
//    @Test
//    public void refreshOneTest() {
//        String key = "http://127.0.0.1:8080/";
//        String newValue = "测试测试";
//
//        Boolean bool = urlLibraryService.refreshOne(key, newValue);
//        logger.info("NoticeServiceImplTest||getNoticeCollectsTest||bool={}", bool);
//    }
//
//    /**
//     * 得到网页存储记录
//     */
//    @Test
//    public void getMapTest() {
//        String key = "http://127.0.0.1:8080/";
//
//        String str = urlLibraryService.getMap(key);
//        logger.info("NoticeServiceImplTest||getNoticeCollectsTest||str={}", str);
//    }
//}
