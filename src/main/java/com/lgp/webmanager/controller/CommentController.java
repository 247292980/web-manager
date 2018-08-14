package com.lgp.webmanager.controller;

import java.util.List;

import com.lgp.webmanager.domain.Collect;
import com.lgp.webmanager.domain.Comment;
import com.lgp.webmanager.domain.User;
import com.lgp.webmanager.domain.result.Response;
import com.lgp.webmanager.repository.CollectRepository;
import com.lgp.webmanager.repository.UserRepository;
import com.lgp.webmanager.service.NoticeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lgp.webmanager.util.DateUtil;
import com.lgp.webmanager.util.StringUtil;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 评论控制器
 **/
@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @Autowired
    private com.lgp.webmanager.repository.CommentRepository CommentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private CollectRepository colloectRepository;

    /**
     * 添加评论
     * */
    @RequestMapping(value = "/add")
    public Response add(Comment comment) {
        User user = null;
        if (comment.getContent().indexOf("@") > -1) {
            List<String> atUsers = StringUtil.getAtUser(comment.getContent());
            if (atUsers != null && atUsers.size() > 0) {
                user = userRepository.findByUserName(atUsers.get(0));
                if (null != user) {
                    comment.setReplyUserId(user.getId());
                } else {
                    logger.info("为找到匹配：" + atUsers.get(0) + "的用户.");
                }
                String content = comment.getContent().substring(0, comment.getContent().indexOf("@"));
                if (StringUtils.isBlank(content)) {
                    content = comment.getContent().substring(comment.getContent().indexOf("@") + user.getUserName().length() + 1, comment.getContent().length());
                }
                comment.setContent(content);
            }
        }
        comment.setUserId(getUserId());
        comment.setCreateTime(DateUtil.getCurrentTime());
        CommentRepository.save(comment);
        if (null != user) {
            // 保存消息通知(回复)
            noticeService.saveNotice(String.valueOf(comment.getCollectId()), "comment", user.getId(), String.valueOf(comment.getId()));
        } else {
            // 保存消息通知（直接评论）
            Collect collect = colloectRepository.findOne(comment.getCollectId());
            if (null != collect) {
                noticeService.saveNotice(String.valueOf(comment.getCollectId()), "comment", collect.getUserId(), String.valueOf(comment.getId()));
            }
        }
        return result();
    }

    /**
     * 获取评论列表
     * */
    @RequestMapping(value = "/list/{collectId}")
    public List<Comment> list(@PathVariable("collectId") long collectId) {
        List<Comment> comments = CommentRepository.findByCollectIdOrderByIdDesc(collectId);
//        logger.info("+++++++++++++++++++++++++++++++++++++"+comments.toString());
        return convertComment(comments);
    }

    /**
     * 删除评论
     * */
    @RequestMapping(value = "/delete/{id}")
    public Response delete(@PathVariable("id") long id) {
        CommentRepository.deleteById(id);
        return result();
    }

    /**
     * 转化时间和用户名
     */
    private List<Comment> convertComment(List<Comment> comments) {
        for (Comment comment : comments) {
            //userId找不到user时，报错
            User user = userRepository.findOne(comment.getUserId());
            if (null == user) {
                continue;
            }
            comment.setCommentTime(DateUtil.getTimeFormatText(comment.getCreateTime()));
            comment.setUserName(user.getUserName());
            comment.setProfilePicture(user.getProfilePicture());
            if (comment.getReplyUserId() != null) {
                User replyUser = userRepository.findOne(comment.getReplyUserId());
                comment.setReplyUserName(replyUser.getUserName());
            }
        }
        return comments;
    }
}
