package org.kaws.system.controller;

import cn.hutool.core.bean.BeanUtil;
import org.kaws.common.domain.dto.SysLogDTO;
import org.kaws.common.domain.model.SysLog;
import org.kaws.common.reponse.R;
import org.kaws.system.service.SysLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Bosco
 * @date 2022/4/11 7:00 下午
 */


@RestController
@RequestMapping("/sys/log")
public class SysLogController {

    @Resource
    private SysLogService sysLogService;

    @GetMapping("/query")
    public R<SysLogDTO> query(String id) {
        SysLog sysLog = sysLogService.getById(id);
        SysLogDTO entity = new SysLogDTO();
        BeanUtil.copyProperties(sysLog, entity);
        return R.success(entity);
    }


}
