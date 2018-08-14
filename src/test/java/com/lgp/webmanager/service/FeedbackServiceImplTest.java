//package com.lgp.webmanager.service;
//
//import com.lgp.webmanager.domain.Feedback;
//import com.lgp.webmanager.service.impl.FavoritesServiceImpl;
//import com.lgp.webmanager.util.DateUtil;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * @AUTHOR lgp
// * @DATE 2018/2/6 17:10
// * @DESCRIPTION
// **/
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class FeedbackServiceImplTest {
//    @Autowired
//    private FeedbackService feedbackService;
//
//    /**
//     * 保存
//     */
//    @Test
//    public void saveFeeddbackTest() {
//        Long userId = 1L;
//        Feedback feedback = new Feedback();
//        feedback.setFeedbackAdvice("测试测试");
//        feedback.setPhone("12345678910");
//        feedbackService.saveFeeddback(feedback, userId);
//    }
//}
