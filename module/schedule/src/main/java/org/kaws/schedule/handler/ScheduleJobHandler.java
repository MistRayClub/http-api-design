package org.kaws.schedule.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import org.kaws.common.reponse.R;
import org.kaws.schedule.domain.dto.ScheduleJobDTO;
import org.kaws.schedule.domain.pojo.ScheduleJob;
import org.kaws.schedule.service.ScheduleJobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author Bosco
 * @date 2022/4/13 7:36 下午
 */

@Component
public class ScheduleJobHandler {

    @Resource
    private ScheduleJobService scheduleJobService;

    public Mono<ServerResponse> add(ServerRequest request) {
        Mono<ScheduleJobDTO> scheduleJobDTOMono = request.bodyToMono(ScheduleJobDTO.class);
        // flatmap：异步处理，并不是Java SDK Stream flatmap
        return scheduleJobDTOMono.flatMap(scheduleJobDTO -> {
            ScheduleJob entity = new ScheduleJob();
            BeanUtil.copyProperties(scheduleJobDTO, entity);
            entity.setJobId(IdUtil.getSnowflakeNextIdStr());
            boolean flag = scheduleJobService.save(entity);
            if (flag) {
                return ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(R.success()), R.class);
            } else {
                return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(R.success()), R.class);
            }
        });
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        String jobId = request.queryParam("jobId").orElse(null);
        ScheduleJob scheduleJob = scheduleJobService.getById(jobId);
        ScheduleJobDTO entity = new ScheduleJobDTO();
        BeanUtil.copyProperties(scheduleJob, entity);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(JSONUtil.toJsonStr(R.success(entity))));
    }

}
