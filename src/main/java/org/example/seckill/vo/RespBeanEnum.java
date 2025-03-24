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
    ERROR(500, "失败"),
    // 登录模块
    LOGIN_ERROR(500, "用户名或密码错误"),
    BIND_ERROR(500, "参数校验异常"),
    EMPTY_STOCK(500, "库存不足"),
    USER_NOT_EXIST(500, "用户不存在"),
    PASSWORD_UPDATE_ERROR(500, "密码更新失败"),
    REPEATE_ERROR(500, "不能重复下单");
    private final Integer code;
    private final String message;
}
