package com.myself.server.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共返回对象
 *
 * @author Wei
 * @since 2021/5/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "RespBean对象", description = "公共返回对象")
public class RespBean {

    @ApiModelProperty(value = "状态码")
    private long code;

    @ApiModelProperty(value = "提示信息")
    private String message;

    @ApiModelProperty(value = "返回对象")
    private Object obj;

    //返回成功信息
    public static RespBean success(String message) {
        return new RespBean(200, message, null);
    }

    //返回成功信息和对象
    public static RespBean success(String message, Object obj) {
        return new RespBean(200, message, obj);
    }

    //返回失败信息
    public static RespBean error(String message) {
        return new RespBean(500, message, null);
    }

    //返回失败信息和对象
    public static RespBean error(String message, Object obj) {
        return new RespBean(500, message, obj);
    }

}
