package com.filesystem.manager.mapper;

import com.filesystem.manager.entity.SysFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.filesystem.manager.vo.FileVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author efar
* @description 针对表【sys_file(文件表)】的数据库操作Mapper
* @createDate 2023-12-08 10:51:25
* @Entity com.filesystem.manager.entity.SysFile
*/
public interface SysFileMapper extends BaseMapper<SysFile> {

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
    SysFile getFileByUserFile(@Param("userId") Integer userId, @Param("fileId") Integer fileId);
}




