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
// * @DATE 2018/2/7 9:54
// * @DESCRIPTION
// **/
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class NoticeServiceImplTest {
//    protected Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    private NoticeService noticeService;
//
////    /**
////     * 展示消息通知
////     */
////    @Test
////    public void getNoticeCollectsTest() {
////        String type = "测试测试";
////        Long userId = 1L;
////        Pageable pageable = null;
////
////        List<CollectSummary> list = noticeService.getNoticeCollects(type, userId, pageable);
////        logger.info("NoticeServiceImplTest||getNoticeCollectsTest||list={}", list.toString());
////    }
//
//    /**
//     * 保存消息通知
//     */
//    @Test
//    public void saveNoticeTest() {
//        String collectId = "测试";
//        String type = "测试测试";
//        Long userId = 1L;
//        String operId = "测试测试测试";
//
//        noticeService.saveNotice(collectId, type, userId, operId);
//    }
//}
