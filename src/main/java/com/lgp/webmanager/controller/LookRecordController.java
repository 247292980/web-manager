package com.lgp.webmanager.controller;

import com.lgp.webmanager.comm.aop.LoggerManage;
import com.lgp.webmanager.domain.enums.ExceptionMsg;
import com.lgp.webmanager.domain.result.Response;
import com.lgp.webmanager.domain.result.ResponseData;
import com.lgp.webmanager.service.LookRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * lookRecord控制器
 **/
@RestController
@RequestMapping("/lookRecord")
public class LookRecordController extends BaseController {

    @Autowired
    private LookRecordService lookRecordService;

    /**
     * 保存瀏覽記錄
     * */
    @RequestMapping(value = "/save/{collectId}")
    @LoggerManage(description = "保存瀏覽記錄")
    public Response saveLookRecord(@PathVariable("collectId") long collectId) {
        lookRecordService.saveLookRecord(this.getUserId(), collectId);
        return new ResponseData(ExceptionMsg.SUCCESS, collectId);
    }

    /**
     * 删除瀏覽記錄
     * */
    @RequestMapping(value = "/delete/{collectId}")
    @LoggerManage(description = "删除瀏覽記錄")
    public Response deleteLookRecord(@PathVariable("collectId") long collectId) {
        lookRecordService.deleteLookRecord(this.getUserId(), collectId);
        return new ResponseData(ExceptionMsg.SUCCESS, collectId);
    }

    /**
     * 删除全部瀏覽記錄
     * */
    @RequestMapping(value = "/deleteAll")
    @LoggerManage(description = "删除全部瀏覽記錄")
    public Response deleteAll() {
        lookRecordService.deleteLookRecordByUserID(this.getUserId());
        return new ResponseData(ExceptionMsg.SUCCESS);
    }
}
