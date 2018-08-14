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
// * @DATE 2018/2/7 9:38
// * @DESCRIPTION
// **/
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class LookRecordServiceImplTest {
//    protected Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    private LookRecordService lookRecordService;
//
//    /**
//     * 得到浏览记录
//     */
//    @Test
//    public void getLookRecordsTest() {
//        Long userId = 1L;
//        Pageable pageable = null;
//
//        List<CollectSummary> list = lookRecordService.getLookRecords(userId, pageable);
//        logger.info("LookRecordServiceImplTest||getLookRecordsTest||list={}", list.toString());
//    }
//
//    /**
//     * 删除浏览记录-用户id
//     */
//    @Test
//    public void deleteLookRecordByUserIDTest() {
//        Long userId = 1L;
//
//        lookRecordService.deleteLookRecordByUserID(userId);
//    }
//
//    /**
//     * 删除浏览记录
//     */
//    @Test
//    public void deleteLookRecordTest() {
//        Long userId = 1L;
//        Long collectId = 1L;
//
//        lookRecordService.deleteLookRecord(userId, collectId);
//    }
//
//    /**
//     * 添加浏览记录
//     */
//    @Test
//    public void saveLookRecordTest() {
//        Long userId = 1L;
//        Long collectId = 1L;
//
//        lookRecordService.saveLookRecord(userId, collectId);
//    }
//
//}
