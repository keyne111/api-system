package com.xiaofan.springbootinit.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaofan.apicommon.model.entity.UserInterfaceInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author dell
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
 * @createDate 2024-07-31 19:31:12
 * @Entity com.xiaofan.springbootinit.model.entity.UserInterfaceInfo
 */
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {


    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




