package com.xiaofan.user.controller;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import com.xiaofan.apicommon.common.BaseResponse;
import com.xiaofan.apicommon.common.ErrorCode;
import com.xiaofan.apicommon.common.ResultUtils;
import com.xiaofan.apicommon.domain.po.InterfaceInfo;
import com.xiaofan.apicommon.domain.po.UserInterfaceInfo;
import com.xiaofan.apicommon.domain.vo.InterfaceInfoVO;
import com.xiaofan.apicommon.exception.BusinessException;
import com.xiaofan.feign.client.InterfaceInfoClient;
import com.xiaofan.user.annotation.AuthCheck;
import com.xiaofan.user.mapper.UserInterfaceInfoMapper;
import com.xiaofan.user.task.InterfaceStatisticsTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分析控制器
 *
 * @author <a href=""></a>
 * @from <a href=""></a>
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    // @Resource
    // private InterfaceInfoService interfaceInfoService;

    @Autowired
    private InterfaceInfoClient interfaceInfoClient;


    // @GetMapping("/top/interface/invoke")
    // @AuthCheck(mustRole = "admin")
    // public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {
    //     List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
    //
    //     Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfoList.stream()
    //             .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
    //     // QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
    //     // queryWrapper.in("id", interfaceInfoIdObjMap.keySet());
    //     // List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
    //     List<InterfaceInfo> list = interfaceInfoClient.list(interfaceInfoIdObjMap.keySet());
    //
    //     if (CollectionUtils.isEmpty(list)) {
    //         throw new BusinessException(ErrorCode.SYSTEM_ERROR);
    //     }
    //     List<InterfaceInfoVO> interfaceInfoVOList = list.stream().map(interfaceInfo -> {
    //         InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
    //         BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
    //         int totalNum = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
    //         interfaceInfoVO.setTotalNum(totalNum);
    //         return interfaceInfoVO;
    //     }).collect(Collectors.toList());
    //     return ResultUtils.success(interfaceInfoVOList);
    // }

    /**
     * 获取调用次数最多的接口信息列表。
     * 通过用户接口信息表查询调用次数最多的接口ID，再关联查询接口详细信息。
     *
     * @return 接口信息列表，包含调用次数最多的接口信息
     */
    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {

        // 拿到缓存的结果
        Map<Long, InterfaceInfoVO> cachedMap = InterfaceStatisticsTask.getCachedInterfaceInfoVOMap();
        // 如果缓存不存在，就正常走数据库
        if (cachedMap.isEmpty()) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            // 查询调用次数最多的接口信息列表
            List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
            // 将接口信息按照接口ID分组，便于关联查询
            Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfoList.stream()
                    .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));

            // 调试
            for (Map.Entry<Long, List<UserInterfaceInfo>> entry : interfaceInfoIdObjMap.entrySet()) {
                Long interfaceId = entry.getKey();
                List<UserInterfaceInfo> interfaceInfoList = entry.getValue();
                System.out.println("接口 ID: " + interfaceId);
                System.out.println("对应的接口信息列表: " + interfaceInfoList);
                System.out.println("--------------");
            }

            // 调用接口信息服务的list方法，传入条件包装器，获取符合条件的接口信息列表
            List<InterfaceInfo> list = interfaceInfoClient.list(interfaceInfoIdObjMap.keySet());
            // 判断查询结果是否为空
            if (CollectionUtils.isEmpty(list)) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
            // 构建接口信息VO列表，使用流式处理将接口信息映射为接口信息VO对象，并加入列表中
            List<InterfaceInfoVO> interfaceInfoVOList = list.stream().map(interfaceInfo -> {
                // 创建一个新的接口信息VO对象
                InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
                // 将接口信息复制到接口信息VO对象中
                BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
                // 从接口信息ID对应的映射中获取调用次数
                int totalNum = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
                // 将调用次数设置到接口信息VO对象中
                interfaceInfoVO.setTotalNum(totalNum);
                // 返回构建好的接口信息VO对象
                return interfaceInfoVO;
            }).collect(Collectors.toList());
            //重新写入缓存，方便下一次拿到
            InterfaceStatisticsTask.setCachedInterfaceInfoVOMap(interfaceInfoVOList);
            // 返回处理结果
            return ResultUtils.success(interfaceInfoVOList);
        }

        List<InterfaceInfoVO> interfaceInfoVOList = new ArrayList<>(cachedMap.values());
        // 可以根据需要对结果进行排序或其他处理
        // 例如，根据totalNum降序排序
        interfaceInfoVOList.sort(Comparator.comparingInt(InterfaceInfoVO::getTotalNum).reversed());

        return ResultUtils.success(interfaceInfoVOList);

    }
}
