package com.lgp.webmanager.repository;

import com.lgp.webmanager.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
/*
* spring jpa
* 只要实现了JpaRepository接口
* 就会自动生成常用sql语句
* */
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
