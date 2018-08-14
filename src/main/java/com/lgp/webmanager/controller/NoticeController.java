package com.lgp.webmanager.controller;

import com.lgp.webmanager.comm.aop.LoggerManage;
import com.lgp.webmanager.domain.Comment;
import com.lgp.webmanager.domain.Notice;
import com.lgp.webmanager.domain.enums.ExceptionMsg;
import com.lgp.webmanager.domain.result.Response;
import com.lgp.webmanager.domain.result.ResponseData;
import com.lgp.webmanager.repository.CommentRepository;
import com.lgp.webmanager.repository.NoticeRepository;
import com.lgp.webmanager.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * notice控制器
 **/
@RestController
@RequestMapping("/notice")
public class NoticeController extends BaseController {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private CommentRepository commentRepository;

    /**
     * 回复
     */
    @RequestMapping(value = "/reply", method = RequestMethod.POST)
    @LoggerManage(description = "回复")
    public Response reply(Comment comment) {
        try {
            comment.setUserId(super.getUserId());
            comment.setCreateTime(DateUtil.getCurrentTime());
            Comment saveCommon = commentRepository.save(comment);
            Notice notice = new Notice();
            notice.setCollectId(comment.getCollectId().toString());
            notice.setUserId(comment.getReplyUserId());
            notice.setType("comment");
            notice.setReaded("unread");
            notice.setOperId(saveCommon.getId().toString());
            notice.setCreateTime(DateUtil.getCurrentTime());
            noticeRepository.save(notice);
            return new ResponseData(ExceptionMsg.SUCCESS, comment);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("reply failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
    }

    /**
     * 获取新消息数量
     * */
    @RequestMapping(value = "/getNoticeNum")
    @LoggerManage(description = "获取新消息数量")
    public ResponseData getNoticeNum() {
        Map<String, Long> result = new HashMap<String, Long>();
        Long newAtMeCount = noticeRepository.countByUserIdAndTypeAndReaded(super.getUserId(), "at", "unread");
        Long newCommentMeCount = noticeRepository.countByUserIdAndTypeAndReaded(super.getUserId(), "comment", "unread");
        Long newPraiseMeCount = noticeRepository.countPraiseByUserIdAndReaded(super.getUserId(), "unread");
        Long newLetterNotice = noticeRepository.countByUserIdAndTypeAndReaded(super.getUserId(), "letter", "unread");
        result.put("newAtMeCount", newAtMeCount);
        result.put("newCommentMeCount", newCommentMeCount);
        result.put("newPraiseMeCount", newPraiseMeCount);
        result.put("newLetterNotice", newLetterNotice);
        return new ResponseData(ExceptionMsg.SUCCESS, result);
    }

}
