package com.filesystem.manager.service;

import com.filesystem.manager.entity.SysFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.filesystem.manager.vo.FileVo;

import java.util.List;

/**
* @author efar
* @description 针对表【sys_file(文件表)】的数据库操作Service
* @createDate 2023-12-08 10:51:25
*/
public interface SysFileService extends IService<SysFile> {
    /**
     * 根据用户ID获取文件列表
     * @param id
     * @return
     */
    List<FileVo> selectByUserId(Integer id);

    /**
     * 根据用户ID和文件ID获取文件信息
     * @param userId
     * @param fileId
     * @return
     */
    SysFile getFileByUserFile(Integer userId, Integer fileId);
}
