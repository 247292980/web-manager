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
// * @DATE 2018/2/5 17:42
// * @DESCRIPTION
// **/
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = WebmanagerApplication.class)
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@WebAppConfiguration
//public class LookRecordControllerTest {
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
//     * 保存瀏覽記錄
//     * */
//    @Test
//    public void saveLookRecordTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/lookRecord/save/{collectId}", "8")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /*
//     * 删除瀏覽記錄
//     * */
//    @Test
//    public void deleteLookRecordTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/lookRecord/delete/{collectId}", "8")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /*
//     * 删除全部瀏覽記錄
//     * */
//    @Test
//    public void deleteAllTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/lookRecord/deleteAll")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//}
