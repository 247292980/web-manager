package com.lgp.webmanager.service.impl;

import com.lgp.webmanager.comm.aop.Log;
import com.lgp.webmanager.domain.IndexCollector;
import com.lgp.webmanager.repository.CollectorRepository;
import com.lgp.webmanager.repository.UserRepository;
import com.lgp.webmanager.service.CollectorService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 特殊用户服务
 **/
@Service
public class CollectorServiceImpl implements CollectorService {
    protected org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CollectorRepository collectorRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * 首页获取收藏家
     */
    @Override
    @Log(description = "首页获取收藏家")
    public IndexCollector getCollectors() {
        IndexCollector indexCollector = new IndexCollector();
        Long mostCollectUser = collectorRepository.getMostCollectUser();
        indexCollector.setMostCollectUser(userRepository.findById(mostCollectUser));
        Long mostFollowedUser = collectorRepository.getMostFollowedUser(mostCollectUser);
        indexCollector.setMostFollowedUser(userRepository.findById(mostFollowedUser));
        String notUserIds = mostCollectUser + "," + mostFollowedUser;
        Long mostPraisedUser = collectorRepository.getMostPraisedUser(notUserIds);
        indexCollector.setMostPraisedUser(userRepository.findById(mostPraisedUser));
        notUserIds += "," + mostPraisedUser;
        Long mostCommentedUser = collectorRepository.getMostCommentedUser(notUserIds);
        indexCollector.setMostCommentedUser(userRepository.findById(mostCommentedUser));
        notUserIds += "," + mostCommentedUser;
        Long mostPopularUser = collectorRepository.getMostPopularUser(notUserIds);
        indexCollector.setMostPopularUser(userRepository.findById(mostPopularUser));
        notUserIds += "," + mostPopularUser;
        Long mostActiveUser = collectorRepository.getMostActiveUser(notUserIds);
        indexCollector.setMostActiveUser(userRepository.findById(mostActiveUser));
        return indexCollector;
    }
}
