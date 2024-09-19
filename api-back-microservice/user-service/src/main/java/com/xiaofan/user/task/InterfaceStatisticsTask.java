package com.xiaofan.user.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xiaofan.apicommon.domain.po.InterfaceInfo;
import com.xiaofan.apicommon.domain.po.UserInterfaceInfo;
import com.xiaofan.apicommon.domain.vo.InterfaceInfoVO;
import com.xiaofan.feign.client.InterfaceInfoClient;
import com.xiaofan.user.mapper.UserInterfaceInfoMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class InterfaceStatisticsTask {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    // @Resource
    // private InterfaceInfoService interfaceInfoService;
    @Autowired
    private InterfaceInfoClient interfaceInfoClient;

    // 定义一个集合来存储计算结果
    private static final Map<Long, InterfaceInfoVO> cachedInterfaceInfoVOMap = new ConcurrentHashMap<>();

    // 每天凌晨2点执行任务
    // @Scheduled(cron = "* * * * * *")
    @Scheduled(cron = "0 0 2 * * ?")
    public void calculateTopInvokeInterfaceInfo() {
        // 执行原方法中的逻辑来获取和计算接口调用次数最多的接口信息列表
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfoList.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));

        // QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        // queryWrapper.in("id", interfaceInfoIdObjMap.keySet());
        List<InterfaceInfo> list = interfaceInfoClient.list(interfaceInfoIdObjMap.keySet());

        if (!CollectionUtils.isEmpty(list)) {
            List<InterfaceInfoVO> interfaceInfoVOList = list.stream().map(interfaceInfo -> {
                InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
                BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
                int totalNum = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
                interfaceInfoVO.setTotalNum(totalNum);
                return interfaceInfoVO;
            }).collect(Collectors.toList());

            // 更新缓存
            cachedInterfaceInfoVOMap.clear();
            interfaceInfoVOList.forEach(vo -> cachedInterfaceInfoVOMap.put(vo.getId(), vo));
        }
    }

    public static Map<Long, InterfaceInfoVO> getCachedInterfaceInfoVOMap() {
        return cachedInterfaceInfoVOMap;
    }

    public static Map<Long, InterfaceInfoVO> setCachedInterfaceInfoVOMap(List<InterfaceInfoVO> interfaceInfoVOList) {
        interfaceInfoVOList.forEach(vo -> cachedInterfaceInfoVOMap.put(vo.getId(), vo));
        return cachedInterfaceInfoVOMap;
    }
}
