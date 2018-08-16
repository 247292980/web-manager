package com.lgp.webmanager.controller;

import com.lgp.webmanager.comm.aop.Log;
import com.lgp.webmanager.domain.Follow;
import com.lgp.webmanager.domain.enums.ExceptionMsg;
import com.lgp.webmanager.domain.enums.StatusEnum;
import com.lgp.webmanager.domain.result.Response;
import com.lgp.webmanager.repository.FollowRepository;
import com.lgp.webmanager.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 关注控制器
 **/
@RestController
@RequestMapping("/follow")
public class FollowController extends BaseController {

    @Autowired
    private FollowRepository followRepository;

    /**
     * 关注&取消关注
     */
    @RequestMapping("/changeFollowStatus")
    @Log(description = "关注&取消关注")
    public Response changeFollowStatus(String status, Long userId) {
        try {
            String followStatus = StatusEnum.FOLLOW_STATUS_FOLLOW.getValue();
            if (!"follow".equals(status)) {
                followStatus = StatusEnum.FOLLOW_STATUS_UNFOLLOW.getValue();
            }
            Follow follow = followRepository.findByUserIdAndFollowId(super.getUserId(), userId);
            if (null != follow) {
                followRepository.updateStatusById(followStatus, DateUtil.getCurrentTime(), follow.getId());
            } else {
                follow = new Follow();
                follow.setFollowId(userId);
                follow.setUserId(super.getUserId());
                follow.setStatus(followStatus);
                follow.setCreateTime(DateUtil.getCurrentTime());
                follow.setLastModifyTime(DateUtil.getCurrentTime());
                followRepository.save(follow);
            }
        } catch (Exception e) {
            logger.error("关注&取消关注异常：", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }
}
