package com.lgp.webmanager.controller;

import com.lgp.webmanager.comm.aop.Log;
import com.lgp.webmanager.domain.Feedback;
import com.lgp.webmanager.domain.enums.ExceptionMsg;
import com.lgp.webmanager.domain.result.Response;
import com.lgp.webmanager.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 意见反馈控制器
 **/
@RestController
@RequestMapping("/feedback")
public class FeedbackController extends BaseController {
    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @Log(description = "意见反馈")
    public Response save(Feedback feedback) {
        try {
            feedbackService.saveFeeddback(feedback, super.getUserId());
        } catch (Exception e) {
            logger.error("feedback failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }
}
