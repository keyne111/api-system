package com.xiaofan.apicommon.domain.service;


import com.xiaofan.apicommon.domain.po.User;

/**
 * 用户服务
 */
public interface InnerUserService{

    /**
     * 数据库中查是否已分配给用户秘钥（accessKey）
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);

}
