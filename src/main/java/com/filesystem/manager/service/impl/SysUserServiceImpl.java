package com.filesystem.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.filesystem.manager.entity.SysUser;
import com.filesystem.manager.service.SysUserService;
import com.filesystem.manager.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
* @author efar
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2023-12-08 10:51:25
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

}




