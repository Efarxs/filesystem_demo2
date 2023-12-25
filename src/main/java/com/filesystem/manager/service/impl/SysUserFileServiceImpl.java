package com.filesystem.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.filesystem.manager.entity.SysUserFile;
import com.filesystem.manager.service.SysUserFileService;
import com.filesystem.manager.mapper.SysUserFileMapper;
import org.springframework.stereotype.Service;

/**
* @author efar
* @description 针对表【sys_user_file(用户文件关联表)】的数据库操作Service实现
* @createDate 2023-12-08 11:18:48
*/
@Service
public class SysUserFileServiceImpl extends ServiceImpl<SysUserFileMapper, SysUserFile>
    implements SysUserFileService{

}




