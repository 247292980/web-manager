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
// * @DATE 2018/2/5 17:02
// * @DESCRIPTION
// **/
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = WebmanagerApplication.class)
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@WebAppConfiguration
//public class FavoritesControllerTest {
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
//     * 新建收藏夹
//     * */
//    @Test
//    public void addFavoritesTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/favorites/add")
//                .param("name", "测试测试")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /*
//     * 导入收藏夹
//     * */
//    @Test
//    public void addImportFavoritesTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/favorites/addImportFavorites")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//    /*
//     * 修改收藏夹
//     * */
//    @Test
//    public void updateFavoritesTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/favorites/update")
//                .param("favoritesName", "测试测试")
//                .param("favoritesId", "29")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//    /*
//     * 删除收藏夹
//     * */
//    @Test
//    public void delFavoritesTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/favorites/del")
//                .param("id", "1")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//    /*
//     * 获取收藏夹
//     * */
//    @Test
//    public void getFavoritesTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/favorites/getFavorites/{userId}","1")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//
//
//}
