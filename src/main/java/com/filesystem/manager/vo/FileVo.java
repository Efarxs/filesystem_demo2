package com.filesystem.manager.vo;

import lombok.Data;

/**
 * @Description 文件列表视图
 * @Author Efar <efarxs@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2023/12/8
 */
@Data
public class FileVo {
    /**
     * 文件ID
     */
    private Integer id;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 文件名
     */
    private String name;
    /**
     * 文件大小
     */
    private long size;
    /**
     * 文件hash值
     */
    private String hash;
    /**
     * 文件上传时间
     */
    private String createTime;
}
