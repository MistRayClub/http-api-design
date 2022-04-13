package org.kaws.schedule;


import org.junit.Before;
import org.junit.Test;
import org.kaws.common.reponse.R;
import org.kaws.schedule.domain.dto.ScheduleJobDTO;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * @author Bosco
 * @date 2022/4/13 8:47 下午
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScheduleApplicationTest {

    private int port = 8082;

    private WebTestClient webTestClient;

    @Before
    public void setup() {
        webTestClient = WebTestClient.bindToServer().responseTimeout(Duration.ofSeconds(30))
                .baseUrl("http://localhost:" + port).build();
    }


    @Test
    public void testAddScheduleJob() {
        ScheduleJobDTO scheduleJobDTO = new ScheduleJobDTO();
        scheduleJobDTO.setJobName("Paul");
        this.webTestClient
                .post()
                .uri("/schedule/job/add")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(scheduleJobDTO), ScheduleJobDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(R.class);
    }

    @Test
    public void testGetScheduleJob() {
        this.webTestClient
                .get()
                .uri("/schedule/job/getById?jobId=1514240950920220672")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(R.class);
    }


}