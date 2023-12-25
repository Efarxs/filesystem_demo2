package com.filesystem.manager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description 自定义配置信息
 * @Author Efar <efarxs@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2023/12/8
 */
@Data
@Component
@ConfigurationProperties(prefix = "filesystem")
public class FileSystemProperties {
    private String src;
}
