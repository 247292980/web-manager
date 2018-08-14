package com.lgp.webmanager.service;

import java.util.List;

import com.lgp.webmanager.domain.CollectSummary;
import org.springframework.data.domain.Pageable;

public interface NoticeService {

    void saveNotice(String collectId, String type, Long userId, String operId);

    List<CollectSummary> getNoticeCollects(String type, Long userId, Pageable pageable);

}
