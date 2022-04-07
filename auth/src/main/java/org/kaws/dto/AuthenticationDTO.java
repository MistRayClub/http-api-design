package org.kaws.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Bosco
 * @date 2022/4/2 11:24 上午
 */

@Getter
@Setter
@ToString
public class AuthenticationDTO {

    private String appKey;

    private Integer nonce;

    private Long timestamp;

    private String url;

}
