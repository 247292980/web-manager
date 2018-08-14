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
// * @DATE 2018/2/5 9:45
// * @DESCRIPTION
// **/
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = WebmanagerApplication.class)
////配置事务的回滚,对数据库的增删改都会回滚,便于测试用例的循环利用
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@WebAppConfiguration
//public class CollectControllerTest {
//    private MockMvc mockMvc;
//
//    @Autowired
//    WebApplicationContext webApplicationContext;
//
//    @Before
//    public void setUp() throws Exception {
//        //MockMvcBuilders使用构建MockMvc对象,通过webApplicationContext获取控制器
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//        //通过类名获取控制器，没有webApplicationContext，不加载项目配置
//        // mockMvc = MockMvcBuilders.standaloneSteup(userController).build();
//    }
//
//    /**
//     * 收藏收集
//     */
//    @Test
//    public void collectTest() throws Exception {
////        执行一个请求
//        mockMvc.perform(MockMvcRequestBuilders
////                构造一个请求
//                .post("/collect/collect")
////                添加参数
////        Mock将URL的参数和通过
//// 使用param添加的参数添加到request中的parameter中了，
//// 而将content内容、类型并没有进行解析，直接添加到request的content中了。
//                .param("favoritesId", "12")
//                .param("title", "测试用例")
//                .param("url", "http://127.0.0.1:8080/")
////                .content(collectStr)
////                 指定传过来的是什么类型
//                .accept(MediaType.APPLICATION_JSON))
////                添加验证断言
//                .andExpect(MockMvcResultMatchers.status().isOk())
////        添加结果处理器
//                .andDo(MockMvcResultHandlers.print())
////        进行自定义断言/进行下一步的异步请求
//                .andReturn();
//    }
//
//    /*
//     * 获取收藏页面的LogoUrl
//     * */
//    @Test
//    public void getCollectLogoUrlTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/collect/getCollectLogoUrl")
//                .param("url", "http://127.0.0.1:8080/")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /**
//     * 根据收藏的收藏标题和描述推荐收藏夹
//     */
//    @Test
//    public void getFavoriteResultTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/collect/getFavoriteResult")
//                .param("title", "测试用例")
//                .param("description", "测试用例简介")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /**
//     * 收藏列表standard
//     */
//    @Test
//    public void standardTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/collect/standard/{type}/{favoritesId}/{userId}", "PUBLIC", "12", "0")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /**
//     * 收藏列表simple
//     */
//    @Test
//    public void simpleTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/collect/simple/{type}/{favoritesId}/{userId}", "PUBLIC", "12", "0")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /**
//     * 修改收藏类型
//     */
//    @Test
//    public void changePrivacyTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/collect/changePrivacy/{id}/{type}", "0", "PUBLIC")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /**
//     * 取消收藏
//     */
//    @Test
//    public void deleteTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/collect/delete/{id}", "0")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//    /**
//     * 收藏详细信息
//     */
//    @Test
//    public void detailTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/collect/detail/{id}", "1")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /**
//     * 查询点赞状态及该收藏的点赞数量
//     */
//    @Test
//    public void getPraiseStatusTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/collect/getPaiseStatus/{collectId}","1")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//}
