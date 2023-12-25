package com.filesystem.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.filesystem.manager.entity.SysFile;
import com.filesystem.manager.service.SysFileService;
import com.filesystem.manager.mapper.SysFileMapper;
import com.filesystem.manager.vo.FileVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author efar
* @description 针对表【sys_file(文件表)】的数据库操作Service实现
* @createDate 2023-12-08 10:51:25
*/
@Service
@RequiredArgsConstructor
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile>
    implements SysFileService{

    private final SysFileMapper mapper;

    /**
     * 根据用户ID获取文件列表
     * @param id
     * @return
     */
    @Override
    public List<FileVo> selectByUserId(Integer id) {
        return mapper.selectByUserId(id);
    }

    /**
     * 根据用户ID和文件ID获取文件信息
     * @param userId
     * @param fileId
     * @return
     */
    public SysFile getFileByUserFile(Integer userId, Integer fileId) {
        return mapper.getFileByUserFile(userId,fileId);
    }
}




