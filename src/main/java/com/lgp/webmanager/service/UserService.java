package com.lgp.webmanager.service;
public interface UserService {
    /**
     * 新增一个用户
     */
    void create(String name, Integer age);

    /**
     * 根据name删除一个用户高
     */
    void deleteByName(String name);

    /**
     * 获取用户总量
     */
    Integer getAllUsers();

    /**
     * 删除所有用户
     */
    void deleteAllUsers();
}