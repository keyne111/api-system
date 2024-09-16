package com.xiaofan.apiback.model.dto.interfaceinfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.xiaofan.apiback.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 接口名称
     */

    private String name;

    /**
     * 接口描述
     */

    private String description;

    /**
     * 接口地址
     */

    private String url;

    /**
     * 请求体
     */

    private String requestHeader;

    /**
     * 响应头
     */

    private String responseHeader;

    /**
     * 接口状态 (0-关闭,1-开启)
     */

    private Integer status;

    /**
     * 请求类型
     */

    private String method;

    /**
     * 创建人
     */

    private Long userId;


    private static final long serialVersionUID = 1L;
}