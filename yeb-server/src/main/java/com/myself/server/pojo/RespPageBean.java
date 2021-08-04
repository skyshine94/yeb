package com.myself.server.pojo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页公共返回对象
 *
 * @author Wei
 * @since 2021/6/3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "RespPageBean", description = "分页公共返回对象")
public class RespPageBean {

    @ApiModelProperty(value = "总记录数")
    private Long total;

    @ApiModelProperty(value = "返回数据")
    private List<?> data;

}
