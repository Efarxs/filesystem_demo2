package com.filesystem.manager.controller;

import com.filesystem.manager.config.FileSystemProperties;
import com.filesystem.manager.entity.SysUser;
import com.filesystem.manager.service.SysFileService;
import com.filesystem.manager.vo.FileVo;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Description 视图跳转控制器
 * @Author Efar <efarxs@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2023/12/8
 */
@Controller
@RequiredArgsConstructor
public class IndexController {
    private final FileSystemProperties fileSystemProperties;
    private final SysFileService sysFileService;

    /**
     * 首页 也是文件列表页
     * @return
     */
    @GetMapping({"index","/"})
    public String index(HttpServletRequest request, Model model) {
        SysUser user = (SysUser) request.getSession().getAttribute("user");
        List<FileVo> list = sysFileService.selectByUserId(user.getId());

        model.addAttribute("user",(user.getIsVip()?"尊贵的VIP用户：":"普通用户:") + user.getUsername());
        model.addAttribute("list",list);
        model.addAttribute("vip",user.getIsVip());
        return "index";
    }

    /**
     * 注册页
     * @return
     */
    @GetMapping("register")
    public String register() {
        return "register";
    }

    /**
     * 登录页
     * @return
     */
    @GetMapping("login")
    public String login() {
        return "login";
    }


    /**
     * 验证码接口
     * @param request
     * @param response
     */
    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Cache-Control","no-store");
        response.setHeader("Pragma","no-cache");
        response.setDateHeader("Expires",0);
        response.setContentType("image/gif");
        //生成验证码对象,三个参数分别是 宽、高、位数
        GifCaptcha captcha = new GifCaptcha(130, 48, 5);
        // 设置验证码的字符类型为字母
        captcha.setCharType(Captcha.TYPE_ONLY_CHAR);
        // 设置内置字体
        captcha.setCharType(Captcha.FONT_1);
        // 验证码存入session
        request.getSession().setAttribute("captcha",captcha.text().toLowerCase());
        try(ServletOutputStream out = response.getOutputStream()) {
            //输出图片流
            captcha.out(out);
        } catch (IOException ignored) {
        }
    }
}
