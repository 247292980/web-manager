//package com.lgp.webmanager.service;
//
//import com.lgp.webmanager.domain.Collect;
//import com.lgp.webmanager.domain.CollectSummary;
//import com.lgp.webmanager.service.impl.CollectServiceImpl;
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
// * @DATE 2018/2/6 15:29
// * @DESCRIPTION
// **/
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class CollectServiceImplTest {
//    protected Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    private CollectService collectService;
//
////    /**
////     * 验证是否重复收藏
////     */
////    @Test
////    public void checkCollectTest() {
////        Collect collect = new Collect();
////        collect.setFavoritesId(1L);
////        collect.setTitle("测试测试");
////        collect.setUrl("测试测试");
////        collect.setUserId(1L);
////        collect.setId(1L);
////        boolean bool = collectService.checkCollect(collect);
////        logger.info("CollectServiceImplTest||searchMyTest||bool={} ", bool);
////
////    }
//    /**
//     * 收藏文章默认点赞
//     */
//    @Test
//    public void likeTest() {
//        Long userId = 1L;
//        Long id = 1L;
//        collectService.like(userId,id);
//    }
////
////    /**
////     * 收藏别人的文章
////     */
////    @Test
////    public void otherCollectTest() {
////        Collect collect = new Collect();
////        collect.setFavoritesId(1L);
////        collect.setTitle("测试测试");
////        collect.setUrl("测试测试");
////        collect.setUserId(1L);
////        collect.setId(1L);
////        collectService.otherCollect(collect);
////    }
////
////    /**
////     * 修改文章
////     */
////    @Test
////    public void updateCollectTest() {
////        Collect collect = new Collect();
////        collect.setFavoritesId(1L);
////        collect.setTitle("测试测试");
////        collect.setUrl("测试测试");
////        collect.setUserId(1L);
////        collect.setId(1L);
////        collectService.updateCollect(collect);
////    }
//
//    /**
//     * 收藏文章
//     */
//    @Test
//    public void saveCollectTest() {
//        Collect collect = new Collect();
//        collect.setFavoritesId(1L);
//        collect.setTitle("测试测试");
//        collect.setUrl("测试测试");
//        collect.setUserId(1L);
//
//        collectService.saveCollect(collect);
//    }
//
//    @Test
//    public void searchMyTest() {
//        Long userId = 1L;
//        String key = "测试";
//        Pageable pageable = null;
//
//        List<CollectSummary> list = collectService.searchMy(userId, key, pageable);
//        logger.info("CollectServiceImplTest||searchMyTest||list={} ", list.toString());
//    }
//
//    @Test
//    public void searchOtherTest() {
//        Long userId = 1L;
//        String key = "测试";
//        Pageable pageable = null;
//
//        List<CollectSummary> list = collectService.searchOther(userId, key, pageable);
//        logger.info("CollectServiceImplTest||searchMyTest||list={} ", list.toString());
//    }
//
//    /**
//     * 展示收藏列表
//     */
//    @Test
//    public void getCollectsTest() {
//        String type = "my";
//        Long userId = 1L;
//        Pageable pageable = null;
//        Long favoritesId = 1L;
//        Long specUserId = 1L;
//
//        List<CollectSummary> list = collectService.getCollects(type, userId, pageable, favoritesId, specUserId);
//        logger.info("CollectServiceImplTest||getCollectsTest||list={} ", list.toString());
//    }
//}
