package com.xiaofan.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaofan.apicommon.domain.po.UserInterfaceInfo;

import java.util.List;

/**
 * @author dell
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
 * @createDate 2024-07-31 19:31:12
 * @Entity com.xiaofan.apiback.model.entity.UserInterfaceInfo
 */
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {


    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




