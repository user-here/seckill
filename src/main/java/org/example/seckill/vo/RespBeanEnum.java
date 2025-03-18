package org.example.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 公共返回对象枚举
 */
@ToString
@AllArgsConstructor
@Getter
public enum RespBeanEnum {
    SUCCESS(200, "成功"),
    ERROR(500, "失败");

    private final Integer code;
    private final String message;
}
