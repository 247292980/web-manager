//package com.lgp.webmanager.controller;
//
//import com.lgp.webmanager.WebmanagerApplication;
//import com.lgp.webmanager.domain.Comment;
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
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.List;
//
///**
// * @AUTHOR lgp
// * @DATE 2018/2/5 15:57
// * @DESCRIPTION
// **/
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = WebmanagerApplication.class)
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@WebAppConfiguration
//public class CommentControllerTest {
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
//     * 添加评论
//     * */
//    @Test
//    public void getCollectLogoUrlTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/comment/add")
//                .param("content", "测试测试")
//                .param("collectId", "1")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//    /*
//     * 获取评论列表
//     * */
//    @Test
//    public void listTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/comment/list/{collectId}","1")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//    /*
//     * 删除评论
//     * */
//    @Test
//    public void deleteTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/comment/delete/{id}","1")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//
//}
