package com.lgp.webmanager.repository.impl;

import com.lgp.webmanager.comm.aop.LoggerManage;
import com.lgp.webmanager.domain.Follow;
import com.lgp.webmanager.repository.CollectorRepository;
import com.lgp.webmanager.util.DateUtil;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 特殊用户 持久层
 **/
@Service
public class CollectorRepositoryImplImpl extends BaseNativeSqlRepositoryImpl implements CollectorRepository {
    protected org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 收藏文章最多的用户
     */
    @Override
    @LoggerManage(description = "收藏文章最多的用户")
    public Long getMostCollectUser() throws IndexOutOfBoundsException {
        String querySql = "SELECT c.user_id ,COUNT(1) AS counts FROM collect c " +
                "WHERE type='PUBLIC' AND is_delete='NO' GROUP BY c.user_id ORDER BY counts DESC LIMIT 1";
        List<Object[]> list = sqlArrayList(querySql);
        if (list.size() < 1) {
            return 1L;
        }
        Object[] obj =  list.get(0);
        return Long.valueOf(obj[0].toString());
    }

    /**
     * 被关注最多的用户
     */
    @Override
    @LoggerManage(description = "被关注最多的用户")
    public Long getMostFollowedUser(Long notUserId) throws IndexOutOfBoundsException{
        String querySql = "SELECT id,follow_id as user_id,COUNT(1) AS counts FROM follow \n" +
                "WHERE status='FOLLOW' " +
//                "and follow_id != " + notUserId +
                " GROUP BY follow_id ORDER BY counts DESC LIMIT 1";
        Follow follow = new Follow();
        List<Object[]> list = sqlArrayList(querySql);
        if (list.size() < 1) {
            return 1L;
        }
        Object[] obj =  list.get(0);
        return Long.valueOf(obj[0].toString());
    }

    /**
     * 文章被赞最多的用户
     */
    @Override
    @LoggerManage(description = "文章被赞最多的用户")
    public Long getMostPraisedUser(String notUserIds) throws IndexOutOfBoundsException{
        String querySql = "SELECT c.user_id,SUM(p.counts) as counts FROM collect c LEFT JOIN \n" +
                "(SELECT collect_id,COUNT(1) as counts FROM praise GROUP BY collect_id)p \n" +
                "ON c.id=p.collect_id WHERE c.type='PUBLIC' AND c.is_delete='NO' " +
//                "AND c.user_id NOT IN (" + notUserIds +") \n" +
                "GROUP BY c.user_id ORDER BY counts DESC LIMIT 1";
        List<Object[]> list = sqlArrayList(querySql);
        if (list.size() < 1) {
            return 1L;
        }
        Object[] obj =  list.get(0);
        return Long.valueOf(obj[0].toString());
    }

    /**
     * 文章被评论最多的用户
     */
    @Override
    @LoggerManage(description = "文章被评论最多的用户")
    public Long getMostCommentedUser(String notUserIds) throws IndexOutOfBoundsException{
        String querySql="SELECT c.user_id,SUM(p.counts) as counts FROM collect c LEFT JOIN \n" +
                "(SELECT collect_id,COUNT(1) as counts FROM `comment` GROUP BY collect_id)p \n" +
                "ON c.id=p.collect_id WHERE c.type='PUBLIC' AND c.is_delete='NO' " +
//                "AND c.user_id NOT IN (" + notUserIds +") \n" +
                "GROUP BY c.user_id ORDER BY counts DESC LIMIT 1";
        List<Object[]> list = sqlArrayList(querySql);
        if (list.size() < 1) {
            return 1L;
        }
        Object[] obj =  list.get(0);
        return Long.valueOf(obj[0].toString());
    }

    /**
     * 最受欢迎的用户
     */
    @Override
    @LoggerManage(description = "最受欢迎的用户")
    public Long getMostPopularUser(String notUserIds) throws IndexOutOfBoundsException{
        String querySql = "SELECT u.user_id,SUM(u.counts) as counts FROM\n" +
                "(SELECT c.user_id,COUNT(1) as counts FROM collect c LEFT JOIN notice n ON c.id=n.collect_id WHERE c.type='PUBLIC' AND c.is_delete='NO' GROUP BY c.user_id\n" +
                "UNION ALL\n" +
                "SELECT follow_id,COUNT(1) AS counts FROM follow GROUP BY follow_id)u\n" +
//                "WHERE u.user_id NOT IN (" + notUserIds + ")\n" +
                "GROUP BY u.user_id ORDER BY counts DESC LIMIT 1";
        List<Object[]> list = sqlArrayList(querySql);
        if (list.size() < 1) {
            return 1L;
        }
        Object[] obj =  list.get(0);
        return Long.valueOf(obj[0].toString());
    }

    /**
     * 近一个月最活跃用户
     */
    @Override
    @LoggerManage(description = "近一个月最活跃用户")
    public Long getMostActiveUser(String notUserIds) throws IndexOutOfBoundsException {
        long nowTime = DateUtil.getCurrentTime();
        long lastMonth = DateUtil.getLastMonthTime();
        String querySql = "SELECT u.user_id,SUM(u.counts) as counts FROM\n" +
                "(SELECT user_id,COUNT(1) as counts FROM collect WHERE create_time>" + lastMonth + " AND create_time<" + nowTime + " AND type='PUBLIC' AND is_delete='NO' GROUP BY user_id\n" +
                "UNION ALL\n" +
                "SELECT user_id,COUNT(1) as counts FROM `comment` WHERE create_time>" + lastMonth + " AND create_time<" + nowTime + " GROUP BY user_id\n" +
                "UNION ALL\n" +
                "SELECT user_id,COUNT(1) as counts FROM praise WHERE create_time>" + lastMonth + " AND create_time<" + nowTime + " GROUP BY user_id\n" +
                "UNION ALL\n" +
                "SELECT user_id,COUNT(1) as counts FROM follow WHERE create_time>" + lastMonth + " AND create_time<" + nowTime + " GROUP BY user_id)u\n" +
//                "WHERE u.user_id NOT IN (" + notUserIds + ")\n" +
                "GROUP BY u.user_id ORDER BY counts DESC LIMIT 1";
        List<Object[]> list = sqlArrayList(querySql);
        if (list.size() < 1) {
            return 1L;
        }
        Object[] obj =  list.get(0);
        return Long.valueOf(obj[0].toString());
    }
}
