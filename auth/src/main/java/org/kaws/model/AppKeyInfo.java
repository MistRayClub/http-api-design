package org.kaws.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Bosco
 * @date 2022/4/1 10:57 上午
 */

@Getter
@Setter
@ToString
@TableName("app_key")
public class AppKeyInfo implements Serializable {

    private static final long serialVersionUID = 2383998446727687939L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String appKey;

    private String appSecret;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
