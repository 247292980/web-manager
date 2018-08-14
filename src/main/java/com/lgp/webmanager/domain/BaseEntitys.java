package com.lgp.webmanager.domain;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 基础类
 */
public class BaseEntitys implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        String json = JSONObject.toJSONString(this);
        return json;
    }
}

