package org.kaws.gateway.web.controller;

import org.kaws.common.reponse.R;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Bosco
 * @date 2022/4/13 11:12 上午
 * @see org.kaws.gateway.config.FallbackConfig
 */

@Deprecated
@RestController
public class FallbackController {

    @GetMapping("/gateway/fallback")
    public R fallback() {
        return R.failure(HttpStatus.TOO_MANY_REQUESTS.value(), HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
    }

}
