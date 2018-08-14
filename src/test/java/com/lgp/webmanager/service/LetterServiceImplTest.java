//package com.lgp.webmanager.service;
//
//import com.lgp.webmanager.domain.Feedback;
//import com.lgp.webmanager.domain.Letter;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * @AUTHOR lgp
// * @DATE 2018/2/6 17:33
// * @DESCRIPTION
// **/
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class LetterServiceImplTest {
//
//    @Autowired
//    private LetterService letterService;
//
//    /**
//     * 私信信息查询
//     */
//    @Test
//    public void findLetterTest() {
//        Long userId = 1L;
//        Pageable pageable = null;
//        letterService.findLetter(userId,pageable);
//    }/**
//     * 发送私信
//     */
//    @Test
//    public void sendLetterTest() {
//        Letter letter = new Letter();
//        letter.setContent("测试测试");
//        letter.setReceiveUserId(1L);
//        letter.setSendUserId(1L);
//
//        letterService.sendLetter(letter);
//    }
//}
