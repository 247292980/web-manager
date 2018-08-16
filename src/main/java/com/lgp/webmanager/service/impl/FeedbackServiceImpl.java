package com.lgp.webmanager.service.impl;

import com.lgp.webmanager.comm.aop.Log;
import com.lgp.webmanager.domain.Feedback;
import com.lgp.webmanager.repository.FeedbackRepository;
import com.lgp.webmanager.service.FeedbackService;
import com.lgp.webmanager.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 意见反馈服务
 **/
@Service("feedbackService")
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    @Log(description = "保存")
    public void saveFeeddback(Feedback feedback,Long userId) {
        feedback.setUserId(userId == null || userId == 0L ? 0L : userId);
        feedback.setCreateTime(DateUtil.getCurrentTime());
        feedback.setLastModifyTime(DateUtil.getCurrentTime());
        feedbackRepository.save(feedback);
    }
}
