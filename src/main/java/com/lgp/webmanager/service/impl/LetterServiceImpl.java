package com.lgp.webmanager.service.impl;

import com.lgp.webmanager.comm.aop.Log;
import com.lgp.webmanager.domain.Letter;
import com.lgp.webmanager.domain.LetterSummary;
import com.lgp.webmanager.domain.User;
import com.lgp.webmanager.domain.enums.StatusEnum;
import com.lgp.webmanager.domain.view.LetterSummaryView;
import com.lgp.webmanager.repository.LetterRepository;
import com.lgp.webmanager.repository.UserRepository;
import com.lgp.webmanager.service.LetterService;
import com.lgp.webmanager.service.NoticeService;
import com.lgp.webmanager.util.DateUtil;
import com.lgp.webmanager.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 私信服务
 **/
@Service
public class LetterServiceImpl implements LetterService{

    @Autowired
    private LetterRepository letterRepository;
    @Resource
    private NoticeService noticeService;
    @Autowired
    private UserRepository userRepository;

    /**
     * 发送私信
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    @Log(description = "发送私信")
    public void sendLetter(Letter letter){
        if("original".equals(letter.getSendType())){
            letter.setType(StatusEnum.LETTER_TYPE_ORIGINAL.getValue());
        }else{
            letter.setType(StatusEnum.LETTER_TYPE_REPLY.getValue());
            List<String> userNameList = StringUtil.getAtUser(letter.getContent());
            if(null != userNameList && userNameList.size() > 0){
                User receiveUser = userRepository.findByUserName(userNameList.get(0));
                if(null != receiveUser){
                    letter.setReceiveUserId(receiveUser.getId());
                }
                String content = letter.getContent().substring(0,letter.getContent().indexOf("@"));
                if(StringUtils.isBlank(content)){
                    content = letter.getContent().substring(letter.getContent().indexOf("@")+receiveUser.getUserName().length()+1,letter.getContent().length());
                    letter.setContent(content);
                }
            }
        }
        letter.setCreateTime(DateUtil.getCurrentTime());
        letterRepository.save(letter);
        if(null == letter.getPid()){
            letter.setPid(letter.getId());
            letterRepository.updatePidById(letter.getId(),letter.getId());
        }
        // 添加消息通知
        noticeService.saveNotice(null,"letter",letter.getReceiveUserId(),String.valueOf(letter.getId()));
    }

    /**
     * 私信信息查询
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    @Log(description = "私信信息查询")
    public List<LetterSummary> findLetter(Long userId, Pageable pageable){
        List<LetterSummaryView> viewList = letterRepository.findLetterByReceiveUserId(userId,pageable);
        List<LetterSummary> summaryList = new ArrayList<LetterSummary>();
        for(LetterSummaryView view : viewList){
            LetterSummary summary = new LetterSummary(view);
            summaryList.add(summary);
        }
        return summaryList;
    }
}
