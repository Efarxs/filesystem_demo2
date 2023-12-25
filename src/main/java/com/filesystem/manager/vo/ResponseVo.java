package com.filesystem.manager.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 自定义接口返回视图
 * @Author Efar <efarxs@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2023/12/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseVo<T> {
    private Integer code;
    private String message;
    private T data;

    /**
     * 成功方法
     * @param data
     * @return
     * @param <T>
     */
    public static <T> ResponseVo<T> success(T data) {
        return new ResponseVo<T>(200,"success",data);
    }

    public static  ResponseVo<String> success() {
        return new ResponseVo<>(200,"success",null);
    }

    public static  ResponseVo<String> success(String message) {
        return new ResponseVo<>(200,message,null);
    }

    public static <T> ResponseVo<T> success(String message, T data) {
        return new ResponseVo<T>(200,message,data);
    }

    /**
     * 错误方法
     * @param message 自定义错误信息
     * @return
     */
    public static ResponseVo<String> error(String message) {
        return new ResponseVo<>(500,message,null);
    }

    /**
     * 失败方法
     * @param code 自定义状态码
     * @param message 自定义失败信息
     * @return
     */
    public static ResponseVo<String> fail(Integer code, String message) {
        return new ResponseVo<>(code, message,null);
    }

}
