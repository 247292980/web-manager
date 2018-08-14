package com.lgp.webmanager.service;

import com.lgp.webmanager.domain.CollectSummary;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 浏览记录service接口
 */
public interface LookRecordService {

    void saveLookRecord(Long userId, Long collectId);

    void deleteLookRecord(Long userId, Long collectId);

    void deleteLookRecordByUserID(Long userId);

    List<CollectSummary> getLookRecords(Long userId, Pageable pageable);

}
