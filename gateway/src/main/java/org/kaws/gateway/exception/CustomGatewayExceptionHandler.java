package org.kaws.gateway.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.kaws.common.exception.BusinessException;
import org.kaws.common.exception.NotEnableAuthorizeException;
import org.kaws.common.exception.TokenInvalidException;
import org.kaws.common.reponse.R;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author Bosco
 * @date 2022/4/7 3:25 下午
 */

@Slf4j
@Data
public class CustomGatewayExceptionHandler implements ErrorWebExceptionHandler {

    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();
    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();
    private List<ViewResolver> viewResolvers = Collections.emptyList();
    private ThreadLocal<R> exceptionHandlerResult = new ThreadLocal<>();


    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {

        log.error("Gateway Exception [{}] [{}]", exchange.getRequest().getURI(), Objects.nonNull(throwable.getMessage()) ? throwable.getMessage() : throwable.toString());

        if (exchange.getResponse().isCommitted()) {
            return Mono.error(throwable);
        }

        exceptionHandlerResult.set(R.failure(HttpStatus.BAD_GATEWAY.value(), Objects.nonNull(throwable.getMessage()) ? throwable.getMessage() : throwable.toString()));

        if (throwable instanceof BusinessException) {
            BusinessException exception = (BusinessException) throwable;
            exceptionHandlerResult.set(R.failure(exception.getCode(), exception.getMessage()));
        }

        if (throwable instanceof TokenInvalidException) {
            TokenInvalidException exception = (TokenInvalidException) throwable;
            exceptionHandlerResult.set(R.failure(exception.getCode(), exception.getMessage()));
        }

        if (throwable instanceof NotEnableAuthorizeException) {
            NotEnableAuthorizeException exception = (NotEnableAuthorizeException) throwable;
            exceptionHandlerResult.set(R.failure(exception.getCode(), exception.getMessage()));
        }

        ServerRequest newRequest = ServerRequest.create(exchange, messageReaders);

        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse).route(newRequest)
                .switchIfEmpty(Mono.error(throwable))
                .flatMap((handler) -> handler.handle(newRequest))
                .flatMap((response) -> write(exchange, response));
    }


    protected Mono<? extends Void> write(ServerWebExchange exchange, ServerResponse response) {
        exchange.getResponse().getHeaders().setContentType(response.headers().getContentType());
        return response.writeTo(exchange, new ResponseContext());
    }


    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        // ThreadLocal清除
        Function<Boolean, R> getHandlerResult = (remove) -> {
            R result = exceptionHandlerResult.get();
            if (remove) {
                exceptionHandlerResult.remove();
            }
            return result;
        };
        return ServerResponse
                .status(Objects.nonNull(exceptionHandlerResult) && Objects.nonNull(getHandlerResult.apply(false))
                        && exceptionHandlerResult.get().getCode() == HttpStatus.UNAUTHORIZED.value() ? HttpStatus.UNAUTHORIZED : HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(getHandlerResult.apply(true)));
    }

    private class ResponseContext implements ServerResponse.Context {

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return CustomGatewayExceptionHandler.this.messageWriters;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return CustomGatewayExceptionHandler.this.viewResolvers;
        }
    }


}
