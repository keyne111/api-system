package com.xiaofan.apicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaofan.apicommon.model.entity.UserInterfaceInfo;


/**
 * @author dell
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
 * @createDate 2024-07-31 19:31:12
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 统计调用次数
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 统计用户是否还有调用次数？
     * @param userId
     * @return
     */
    boolean isHaveInvoke(long userId);

}
