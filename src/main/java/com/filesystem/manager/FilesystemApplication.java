package com.filesystem.manager;

import com.filesystem.manager.config.FileSystemProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.filesystem.manager.mapper"})
public class FilesystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilesystemApplication.class, args);
    }

}
