package com.lgp.webmanager.service.impl;

import com.lgp.webmanager.comm.aop.LoggerManage;
import com.lgp.webmanager.domain.LookRecord;
import com.lgp.webmanager.domain.Praise;
import com.lgp.webmanager.domain.CollectSummary;
import com.lgp.webmanager.domain.view.CollectSummaryView;
import com.lgp.webmanager.repository.CommentRepository;
import com.lgp.webmanager.repository.LookRecordRepository;
import com.lgp.webmanager.repository.PraiseRepository;
import com.lgp.webmanager.service.LookRecordService;
import com.lgp.webmanager.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 浏览记录 服务
 **/
@Service("lookRecordService")
public class LookRecordServiceImpl implements LookRecordService {

    @Autowired
    private LookRecordRepository lookRecordRepository;

    @Autowired
    private PraiseRepository praiseRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    @LoggerManage(description = "添加浏览记录")
    public void saveLookRecord(Long userId, Long collectId) {
        if (userId != null && userId > 0 && collectId != null) {
            Integer count = lookRecordRepository.countByUserIdAndCollectId(userId, collectId);
            if (count > 0) {
                lookRecordRepository.updatelastModifyTime(userId, collectId, DateUtil.getCurrentTime());
            } else {
                LookRecord lookRecord = new LookRecord();
                lookRecord.setUserId(userId);
                lookRecord.setCollectId(collectId);
                lookRecord.setCreateTime(DateUtil.getCurrentTime());
                lookRecord.setLastModifyTime(DateUtil.getCurrentTime());
                lookRecordRepository.save(lookRecord);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    @LoggerManage(description = "删除浏览记录")
    public void deleteLookRecord(Long userId, Long collectId) {
        lookRecordRepository.deleteByUserIdAndCollectId(userId, collectId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    @LoggerManage(description = "删除浏览记录-用户id")
    public void deleteLookRecordByUserID(Long userId) {
        lookRecordRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    @LoggerManage(description = "得到浏览记录")
    public List<CollectSummary> getLookRecords(Long userId, Pageable pageable) {
        Page<CollectSummaryView> views = null;

        views = lookRecordRepository.findViewByUserId(userId, pageable);

        return convertCollect(views, userId);
    }

    private List<CollectSummary> convertCollect(Page<CollectSummaryView> views, Long userId) {
        List<CollectSummary> summarys = new ArrayList<CollectSummary>();
        for (CollectSummaryView view : views) {
            CollectSummary summary = new CollectSummary(view);
            summary.setPraiseCount(praiseRepository.countByCollectId(view.getId()));
            summary.setCommentCount(commentRepository.countByCollectId(view.getId()));
            Praise praise = praiseRepository.findByUserIdAndCollectId(userId, view.getId());
            if (praise != null) {
                summary.setPraise(true);
            } else {
                summary.setPraise(false);
            }
            summary.setCollectTime(DateUtil.getTimeFormatText(view.getCreateTime()));
            summarys.add(summary);
        }
        return summarys;
    }
}