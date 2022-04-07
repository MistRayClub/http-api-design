package org.kaws.gateway.web.filter;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.kaws.common.exception.TokenInvalidException;
import org.kaws.common.reponse.CodeEnum;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * @author Bosco
 * @date 2022/4/6 7:55 下午
 */


@Slf4j
@Component
public class VerifyTokenFilter extends AbstractGatewayFilterFactory {

    private static final String JWT_SECRET = "LebronJames";

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String token = request.getHeaders().getFirst("token");
            if (BooleanUtil.isFalse(JWTUtil.verify(token, JWT_SECRET.getBytes()))) {
                throw new TokenInvalidException(CodeEnum.TOKEN_INVALID.getMessage(), CodeEnum.TOKEN_INVALID.getCode());
            }

            JWT jwt = JWTUtil.parseToken(token);

            Long timestamp = (Long) jwt.getPayload("timestamp");
            if (System.currentTimeMillis() > timestamp) {
                throw new TokenInvalidException(CodeEnum.TOKEN_EXPIRED.getMessage(), CodeEnum.TOKEN_EXPIRED.getCode());
            }

            log.info("TokenFilter is filtered");
            return chain.filter(exchange);
        };
    }
}
