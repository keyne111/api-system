package com.xiaofan.apiback.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaofan.apiback.mapper.InterfaceInfoMapper;
import com.xiaofan.apicommon.common.ErrorCode;
import com.xiaofan.apicommon.domain.po.InterfaceInfo;
import com.xiaofan.apicommon.exception.BusinessException;
import com.xiaofan.apicommon.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    /**
     * 从数据库中查询模拟接口是否存在（请求路径、请求方法、请求参数）
     *
     * @param url
     * @param method
     */
    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if(StringUtils.isAnyBlank(url,method)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求路径或请求方法不存在");
        }

        if(url.length()>50 || (method.length()<0 || method.length()>6)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求路径或请求方法过长");
        }

        QueryWrapper<InterfaceInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("url",url);
        queryWrapper.eq("method",method);


        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(queryWrapper);
        if(interfaceInfo==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return interfaceInfo;
    }
}
