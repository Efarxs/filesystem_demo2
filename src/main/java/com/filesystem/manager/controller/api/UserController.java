package com.filesystem.manager.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.filesystem.manager.entity.SysUser;
import com.filesystem.manager.service.SysUserService;
import com.filesystem.manager.util.MD5;
import com.filesystem.manager.vo.ResponseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * @Description 用户接口
 * @Author Efar <efarxs@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2023/12/8
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final SysUserService sysUserService;

    /**
     * 开通VIP
     * @param request
     * @param cdk
     * @return
     */
    @PostMapping("ktvip")
    public ResponseVo<String> ktvip(HttpServletRequest request, String cdk) {
        SysUser user = (SysUser) request.getSession().getAttribute("user");
        if ("吴习嘉牛逼".equals(cdk.trim())) {
            // 口令正确
            user.setIsVip(true);
            sysUserService.updateById(user);
            return ResponseVo.success();
        }

        return ResponseVo.fail(-1,"口令错误");
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @GetMapping("logout")
    public ResponseVo<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");

        return ResponseVo.success();
    }

    /**
     * 用户登录
     * @return
     */
    @PostMapping("login")
    public ResponseVo<String> login(@RequestBody SysUser sysUser, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        System.out.println(sysUser);

        // 创建查询器
        LambdaQueryWrapper<SysUser> lq = new LambdaQueryWrapper<>();
        // 比较条件： username 和 md5加密后的password
        lq.eq(SysUser::getUsername,sysUser.getUsername());
        lq.eq(SysUser::getPassword, MD5.md5Hex(sysUser.getPassword()));

        // 查询数据
        SysUser user = sysUserService.getOne(lq);
        // 查询到空数据 就是不存在这个用户
        if (null == user) {
            return ResponseVo.fail(-1,"用户信息不匹配，登录失败");
        }
        // 登录成功，保存登录信息到Session
        request.getSession().setAttribute("user",user);

        return ResponseVo.success();
    }

    /**
     * 用户注册
     * @return
     */
    @PostMapping("register")
    public ResponseVo<String> register(HttpServletRequest request, String username, String password, String captcha) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // 先判断验证码
        String verifyCode = (String) request.getSession().getAttribute("captcha");
        // 判断验证码在session中是否过期
        if (StringUtils.isEmpty(verifyCode)) {
            return ResponseVo.fail(-1,"验证码已过期，请重新登录");
        }
        // 校验验证码是否正确
        if (!verifyCode.equalsIgnoreCase(captcha)) {
            return ResponseVo.fail(-2,"验证码校验失败");
        }
        // 判断账号密码是否为空
        if (StringUtils.isEmptyOrWhitespace(username) || StringUtils.isEmptyOrWhitespace(password)) {
            return ResponseVo.fail(-3,"账号和密码都不能为空哦");
        }
        // 比较密码长度
        if (password.length() < 6 || password.length() > 12) {
            return ResponseVo.fail(-4,"密码长度请控制在6-12位");
        }
        // 创建查询器
        LambdaQueryWrapper<SysUser> lq = new LambdaQueryWrapper<>();
        // 比较条件：username
        lq.eq(SysUser::getUsername,username);
        // 获取查询结果
        SysUser user = sysUserService.getOne(lq);
        if (user != null) {
            // 用户名已经被注册了
            return ResponseVo.fail(-101,"用户名已被注册");
        }
        // 创建一个User对象，用来保存到数据库
        SysUser sysUser = new SysUser();
        // 配置创建时间
        sysUser.setCreateTime(new Date());
        // 用户名
        sysUser.setUsername(username);
        // 密码 MD5加密
        sysUser.setPassword(MD5.md5Hex(password));
        // 是否VIP 默认0
        sysUser.setIsVip(false);
        // 更新时间
        sysUser.setUpdateTime(new Date());

        // 保存到数据库
        if (sysUserService.save(sysUser)) {
            return ResponseVo.success("注册成功",sysUser.getId().toString());
        }

        return ResponseVo.fail(-102,"注册失败");
    }
}
