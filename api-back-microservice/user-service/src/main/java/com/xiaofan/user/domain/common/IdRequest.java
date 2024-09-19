package com.xiaofan.user.domain.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 只传id请求
 */
@Data
public class IdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}