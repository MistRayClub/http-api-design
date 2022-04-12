package org.kaws.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Bosco
 * @date 2022/4/11 6:58 下午
 */


@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(value = {"org.kaws.common", "org.kaws.system"})
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }

}
