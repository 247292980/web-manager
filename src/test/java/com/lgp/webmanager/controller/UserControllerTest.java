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
// * @DATE 2018/2/6 10:29
// * @DESCRIPTION
// **/
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = WebmanagerApplication.class)
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@WebAppConfiguration
//public class UserControllerTest {
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
//     * 上传背景
//     * */
//    @Test
//    public void uploadBackgroundTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/user/uploadBackground")
//                .param("dataUrl", "测试测试")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /**
//     * 上传头像
//     */
//    @Test
//    public void uploadHeadPortraitTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/user/uploadHeadPortrait")
//                .param("dataUrl", "测试测试")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /*
//     * 获取关注列表
//     * */
//    @Test
//    public void getFollowsTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/user/getFollows")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
////    /*
////     * 属性修改
////     * */
////    @Test
////    public void updateConfigTest() throws Exception {
////        mockMvc.perform(MockMvcRequestBuilders
////                .post("/user/updateConfig")
////                .param("id", "1")
////                .param("type", "public")
////                .param("defaultFavorites", "1")
////                .accept(MediaType.APPLICATION_JSON))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andDo(MockMvcResultHandlers.print())
////                .andReturn();
////    }
//
//    /*
//     * 获取属性设置
//     * */
//    @Test
//    public void getConfigTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/user/getConfig")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /*
//     * 获取收藏夹
//     * */
//    @Test
//    public void getFavoritesTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/user/getFavorites")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /*
//     * 注册
//     * */
//    @Test
//    public void registTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/user/regist")
//                .param("email", "测试测试")
//                .param("userName", "测试测试")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /*
//     * 登陆
//     * */
//    @Test
//    public void loginTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/user/login")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//}
