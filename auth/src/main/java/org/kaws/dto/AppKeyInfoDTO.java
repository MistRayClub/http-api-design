package org.kaws.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Bosco
 * @date 2022/4/1 7:04 下午
 */

@Getter
@Setter
@ToString
public class AppKeyInfoDTO {

    private String appKey;

    private String appSecret;

    private String remark;

}
