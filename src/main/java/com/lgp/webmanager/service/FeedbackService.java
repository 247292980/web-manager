package com.lgp.webmanager.service;

import com.lgp.webmanager.domain.Feedback;

public interface FeedbackService {

    void saveFeeddback(Feedback feedback, Long userId);
}
