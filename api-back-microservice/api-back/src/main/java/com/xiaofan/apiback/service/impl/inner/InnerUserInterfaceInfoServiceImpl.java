package com.xiaofan.apiback.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaofan.apiback.mapper.UserInterfaceInfoMapper;
import com.xiaofan.apiback.service.UserInterfaceInfoService;
import com.xiaofan.apicommon.common.ErrorCode;
import com.xiaofan.apicommon.domain.po.UserInterfaceInfo;
import com.xiaofan.apicommon.exception.BusinessException;
import com.xiaofan.apicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    /**
     * 统计调用次数
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId,userId);
    }

    /**
     * 统计用户是否还有调用次数？
     *
     * @param userId
     * @return
     */
    @Override
    public boolean isHaveInvoke(long userId) {

        if(userId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userId",userId);


        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(queryWrapper);
        if(userInterfaceInfo.getLeftNum()<=0){
            return false;
        }
        return true;
    }
}
