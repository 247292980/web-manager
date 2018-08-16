package com.lgp.webmanager.controller;

import com.lgp.webmanager.comm.aop.Log;
import com.lgp.webmanager.domain.Letter;
import com.lgp.webmanager.domain.enums.ExceptionMsg;
import com.lgp.webmanager.domain.result.Response;
import com.lgp.webmanager.domain.result.ResponseData;
import com.lgp.webmanager.domain.LetterSummary;
import com.lgp.webmanager.service.LetterService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 私信控制器
 **/

@RestController
@RequestMapping("/letter")
public class LetterController extends BaseController {

    @Resource
    private LetterService letterService;

    /**
     * 发送私信
     */
    @RequestMapping("/sendLetter")
    @Log(description = "发送私信")
    public Response sendLetter(Letter letter) {
        try {
            letter.setSendUserId(super.getUserId());
            letterService.sendLetter(letter);
            return new ResponseData(ExceptionMsg.SUCCESS, letter);
        } catch (Exception e) {
            logger.error("发送私信异常：", e);
            return result(ExceptionMsg.FAILED);
        }
    }

    /**
     * 私信列表获取
     */
    @RequestMapping("/getLetterList")
    @Log(description = "获取私信列表")
    public List<LetterSummary> getLetterList(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                             @RequestParam(value = "size", defaultValue = "15") Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        List<LetterSummary> letterList = letterService.findLetter(super.getUserId(), pageable);
        return letterList;
    }
}
