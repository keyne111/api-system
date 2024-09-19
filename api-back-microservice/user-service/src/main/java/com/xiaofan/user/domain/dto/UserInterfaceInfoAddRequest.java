package com.xiaofan.user.domain.dto;

import lombok.Data;

/**
 * 创建请求
 */
@Data
public class UserInterfaceInfoAddRequest {


    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    private static final long serialVersionUID = 1L;
}
