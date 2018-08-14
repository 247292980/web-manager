package com.lgp.webmanager.service;

import com.lgp.webmanager.domain.Letter;
import com.lgp.webmanager.domain.LetterSummary;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LetterService {

    void sendLetter(Letter letter);

    List<LetterSummary> findLetter(Long userId, Pageable pageable);
}
