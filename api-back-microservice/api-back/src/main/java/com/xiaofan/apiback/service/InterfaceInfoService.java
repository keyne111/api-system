package com.xiaofan.apiback.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaofan.apiback.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.xiaofan.apiback.model.vo.InterfaceInfoVO;
import com.xiaofan.apicommon.domain.po.InterfaceInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dell
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service
 * @createDate 2024-07-19 21:26:21
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验
     *
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);


    /**
     * 获取接口封装
     *
     * @param interfaceInfo
     * @param request
     * @return InterfaceInfoVO
     */
    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request);

    /**
     * 获取查询条件
     *
     * @param interfaceInfoQueryRequestt
     * @return
     */
    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequestt);


    /**
     * 分页获取帖子封装
     *
     * @param interfaceInfoPage
     * @param request
     * @return
     */
    Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request);


}
