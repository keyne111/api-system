package com.xiaofan.feign.client;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.xiaofan.apicommon.domain.po.InterfaceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

/**
 * @Author xiaofan
 * @from <a href="https://github.com/keyne111"></a>
 * @Date 2024/9/19 17:25
 */
@FeignClient(value = "interfaceInfo-service",path = "/api/interfaceInfo/inner")
public interface InterfaceInfoClient{

    @GetMapping("/get/list")
    public List<InterfaceInfo> list(@RequestParam("ids") Collection<Long> ids);


}
