package com.lgp.webmanager.domain;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 首页特殊用户信息
 **/
public class IndexCollector extends BaseEntitys {
    //收藏文章最多的用户
    private User mostCollectUser;
    //被关注最多的用户
    private User mostFollowedUser;
    //文章被赞最多的用户
    private User mostPraisedUser;
    //文章被评论最多的用户
    private User mostCommentedUser;
    //最受欢迎的用户
    private User mostPopularUser;
    //近一个月最活跃用户
    private User mostActiveUser;

    public User getMostCollectUser() {
        return mostCollectUser;
    }

    public void setMostCollectUser(User mostCollectUser) {
        this.mostCollectUser = mostCollectUser;
    }

    public User getMostFollowedUser() {
        return mostFollowedUser;
    }

    public void setMostFollowedUser(User mostFollowedUser) {
        this.mostFollowedUser = mostFollowedUser;
    }

    public User getMostPraisedUser() {
        return mostPraisedUser;
    }

    public void setMostPraisedUser(User mostPraisedUser) {
        this.mostPraisedUser = mostPraisedUser;
    }

    public User getMostCommentedUser() {
        return mostCommentedUser;
    }

    public void setMostCommentedUser(User mostCommentedUser) {
        this.mostCommentedUser = mostCommentedUser;
    }

    public User getMostPopularUser() {
        return mostPopularUser;
    }

    public void setMostPopularUser(User mostPopularUser) {
        this.mostPopularUser = mostPopularUser;
    }

    public User getMostActiveUser() {
        return mostActiveUser;
    }

    public void setMostActiveUser(User mostActiveUser) {
        this.mostActiveUser = mostActiveUser;
    }
}
