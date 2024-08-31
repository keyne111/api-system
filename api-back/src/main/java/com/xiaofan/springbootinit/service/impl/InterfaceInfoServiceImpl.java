package com.xiaofan.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaofan.apicommon.model.entity.InterfaceInfo;
import com.xiaofan.apicommon.model.entity.User;
import com.xiaofan.springbootinit.common.ErrorCode;
import com.xiaofan.springbootinit.constant.CommonConstant;
import com.xiaofan.springbootinit.exception.BusinessException;
import com.xiaofan.springbootinit.exception.ThrowUtils;
import com.xiaofan.springbootinit.mapper.InterfaceInfoMapper;
import com.xiaofan.springbootinit.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.xiaofan.springbootinit.model.vo.InterfaceInfoVO;
import com.xiaofan.springbootinit.model.vo.UserVO;
import com.xiaofan.springbootinit.service.InterfaceInfoService;
import com.xiaofan.springbootinit.service.UserService;
import com.xiaofan.springbootinit.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author dell
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
 * @createDate 2024-07-19 21:26:21
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Resource
    private UserService userService;

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {


        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();
        String method = interfaceInfo.getMethod();

        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(
                    name,
                    description,
                    url,
                    requestHeader,
                    responseHeader,
                    method), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无请求名或请求名过长");
        }
        if (StringUtils.isNotBlank(description) && description.length() > 300) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无描述或描述过长");
        }
        if (StringUtils.isNotBlank(url) && url.length() > 200) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无请求路径或请求路径过长");
        }
        if (StringUtils.isBlank(requestHeader)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求头不存在");
        }
        if (StringUtils.isBlank(responseHeader)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "响应头不存在");
        }
        if (StringUtils.isBlank(method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求类型不存在");
        }
    }

    @Override
    public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request) {
        InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
        BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);

        Long id = interfaceInfo.getId();
        // 1. 关联查询用户信息
        Long userId = interfaceInfo.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        interfaceInfoVO.setUser(userVO);

        return interfaceInfoVO;
    }

    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequestt) {


        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (interfaceInfoQueryRequestt == null) {
            return queryWrapper;
        }

        String sortField = interfaceInfoQueryRequestt.getSortField();
        String sortOrder = interfaceInfoQueryRequestt.getSortOrder();

        Long id = interfaceInfoQueryRequestt.getId();
        String name = interfaceInfoQueryRequestt.getName();
        String description = interfaceInfoQueryRequestt.getDescription();
        String url = interfaceInfoQueryRequestt.getUrl();
        String requestHeader = interfaceInfoQueryRequestt.getRequestHeader();
        String responseHeader = interfaceInfoQueryRequestt.getResponseHeader();

        String method = interfaceInfoQueryRequestt.getMethod();


        Long userId = interfaceInfoQueryRequestt.getUserId();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.like(StringUtils.isNotBlank(url), "url", url);
        queryWrapper.like(StringUtils.isNotBlank(requestHeader), "requestHeader", requestHeader);
        queryWrapper.like(StringUtils.isNotBlank(responseHeader), "responseHeader", responseHeader);
        queryWrapper.like(StringUtils.isNotBlank(method), "method", method);


        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request) {
        List<InterfaceInfo> interfaceInfoList = interfaceInfoPage.getRecords();
        Page<InterfaceInfoVO> interfaceInfoVOPage = new Page<>(interfaceInfoPage.getCurrent(), interfaceInfoPage.getSize(), interfaceInfoPage.getTotal());
        if (CollUtil.isEmpty(interfaceInfoList)) {
            return interfaceInfoVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = interfaceInfoList.stream().map(InterfaceInfo::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));

        // 填充信息
        List<InterfaceInfoVO> interfaceInfoVOList = interfaceInfoList.stream().map(interfaceInfo -> {

            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            Long userId = interfaceInfo.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            interfaceInfoVO.setUser(userService.getUserVO(user));

            return interfaceInfoVO;
        }).collect(Collectors.toList());
        interfaceInfoVOPage.setRecords(interfaceInfoVOList);
        return interfaceInfoVOPage;
    }
}




