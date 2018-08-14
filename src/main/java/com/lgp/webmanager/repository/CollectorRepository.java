package com.lgp.webmanager.repository;

/**
 * 获取收藏家
 **/
public interface CollectorRepository {

    Long getMostCollectUser();

    Long getMostFollowedUser(Long notUserId);

    Long getMostPraisedUser(String notUserIds);

    Long getMostCommentedUser(String notUserIds);

    Long getMostPopularUser(String notUserIds);

    Long getMostActiveUser(String notUserIds);
}
