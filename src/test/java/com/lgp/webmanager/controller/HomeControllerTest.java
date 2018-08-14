//package com.lgp.webmanager.controller;
//
//import com.lgp.webmanager.WebmanagerApplication;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.transaction.TransactionConfiguration;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
///**
// * @AUTHOR lgp
// * @DATE 2018/2/6 9:13
// * @DESCRIPTION
// **/
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = WebmanagerApplication.class)
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@WebAppConfiguration
//public class HomeControllerTest {
//    private MockMvc mockMvc;
//
//    @Autowired
//    WebApplicationContext webApplicationContext;
//
//    @Before
//    public void setUp() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }
//
//    /*
//     * 私信我的页面展示
//     * */
//    @Test
//    public void letterMeTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/letter/letterMe")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//    /*
//     * 浏览记录 简单显示
//     * */
//    @Test
//    public void getLookRecordSimpleTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/lookRecord/simple/{type}/{userId}","my","1")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//    /*
//     * 浏览记录 标准显示
//     * */
//    @Test
//    public void getLookRecordStandardTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/lookRecord/standard/{type}/{userId}","my","1")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//    /*
//     * 消息通知赞我的
//     * */
//    @Test
//    public void praiseMeTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/notice/praiseMe")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//    /*
//     * 消息通知评论我
//     * */
//    @Test
//    public void commentMeTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/notice/commentMe")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//    /*
//     * 消息通知@我
//     * */
//    @Test
//    public void atMeTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/notice/atMe")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /*
//     * 搜索
//     * */
//    @Test
//    public void searchTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/search/{key}", "测试")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /*
//     * 个人首页内容替换
//     * */
////    @Test
////    public void userContentShowTest() throws Exception {
////        mockMvc.perform(MockMvcRequestBuilders
////                .post("/usercontent/{userId}/{favoritesId}", "1", "1")
////                .accept(MediaType.APPLICATION_JSON))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andDo(MockMvcResultHandlers.print())
////                .andReturn();
////    }
//
//    /*
//     * 文章列表standard
//     * */
//    @Test
//    public void standardTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/standard/{type}/{userId}", "my", "15")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /*
//     * 文章列表simple
//     * */
//    @Test
//    public void simpleTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/simple/{type}/{userId}", "my", "15")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /*
//     * 个人首页
//     * */
////    @Test
////    public void userPageShowTest() throws Exception {
////        mockMvc.perform(MockMvcRequestBuilders
////                .post("/user/{userId}/{favoritesId}", "1", "1")
////                .accept(MediaType.APPLICATION_JSON))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andDo(MockMvcResultHandlers.print())
////                .andReturn();
////    }
//}
