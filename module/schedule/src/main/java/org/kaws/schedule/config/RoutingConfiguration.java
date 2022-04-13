package org.kaws.schedule.config;

import org.kaws.schedule.handler.ScheduleJobHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


/**
 * @author Bosco
 * @date 2022/4/13 8:21 下午
 */

@Configuration
public class RoutingConfiguration {

    @Bean
    public RouterFunction<ServerResponse> scheduleJobRouterFunction(ScheduleJobHandler scheduleJobHandler) {
        return route(POST("/schedule/job/add"), scheduleJobHandler::add)
                .andRoute(GET("/schedule/job/getById"), scheduleJobHandler::getById);
    }
}
