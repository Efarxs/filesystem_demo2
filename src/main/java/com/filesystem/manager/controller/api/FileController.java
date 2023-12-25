package com.filesystem.manager.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.filesystem.manager.config.FileSystemProperties;
import com.filesystem.manager.entity.SysFile;
import com.filesystem.manager.entity.SysUser;
import com.filesystem.manager.entity.SysUserFile;
import com.filesystem.manager.service.SysFileService;
import com.filesystem.manager.service.SysUserFileService;
import com.filesystem.manager.util.MD5;
import com.filesystem.manager.vo.FileVo;
import com.filesystem.manager.vo.ResponseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @Description 文件接口
 * @Author Efar <efarxs@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2023/12/8
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {
    private final SysFileService sysFileService;
    private final SysUserFileService sysUserFileService;
    private final FileSystemProperties fileSystemProperties;


    /**
     * 删除文件
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseVo<String> delete(@PathVariable Integer id, HttpServletRequest request) {
        // 通过ID查找文件
        SysUser user = (SysUser) request.getSession().getAttribute("user");
        SysFile file = sysFileService.getFileByUserFile(user.getId(), id);
        if (null == file) {
            return ResponseVo.fail(-1,"文件不存在");
        }
        LambdaQueryWrapper<SysUserFile> lq = new LambdaQueryWrapper<>();
        lq.eq(SysUserFile::getFileId,file.getId());
        lq.eq(SysUserFile::getUserId,user.getId());

        if (sysUserFileService.remove(lq)) {
            return ResponseVo.success();
        }
        return ResponseVo.fail(-2,"删除失败");
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping("/")
    public ResponseVo<String> uploadAndCompressFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            SysUser user = (SysUser) request.getSession().getAttribute("user");
            // 计算md5值
            String fileHash = MD5.md5Hex(Arrays.toString(DigestUtils.md5Digest(file.getInputStream())));
            // 判断文件是否已经上传过了
            LambdaQueryWrapper<SysFile> lq = new LambdaQueryWrapper<>();
            lq.eq(SysFile::getHash,fileHash);
            SysFile sysFile = sysFileService.getOne(lq);
            if (null == sysFile) {
                // 获取文件名
                String originalFileName = file.getOriginalFilename();

                // 压缩文件名
                String zipFileName = originalFileName + ".zip";

                // 创建压缩文件
                FileOutputStream fos = new FileOutputStream(fileSystemProperties.getSrc() + zipFileName);
                ZipOutputStream zipOut = new ZipOutputStream(fos);

                // 添加文件到压缩文件
                ZipEntry zipEntry = new ZipEntry(originalFileName);
                zipOut.putNextEntry(zipEntry);
                zipOut.write(file.getBytes());
                zipOut.closeEntry();

                // 关闭流
                zipOut.close();
                fos.close();
                sysFile = new SysFile();
                sysFile.setAuthor(user.getId());
                sysFile.setHash(fileHash);
                sysFile.setFilename(originalFileName);
                //sysFile.setCreateBy(user.getUsername());
                sysFile.setCreateTime(new Date());

                sysFileService.save(sysFile);
            }
            // 保存到关联表
            SysUserFile sysUserFile = new SysUserFile();
            sysUserFile.setFileId(sysFile.getId());
            sysUserFile.setUserId(user.getId());
            sysUserFile.setCreateTime(new Date());
            sysUserFileService.save(sysUserFile);


            return ResponseVo.success();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseVo.error("上传失败");
        } catch (NoSuchAlgorithmException e) {
            return ResponseVo.error("上传失败");
        }
    }

    /**
     * 下载文件
     * @param id
     * @param response
     */
    @GetMapping("/{id}")
    public void downloadFile(@PathVariable(value = "id") Integer id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 通过ID查找文件
        SysUser user = (SysUser) request.getSession().getAttribute("user");
        //System.out.println(user);
        SysFile file = sysFileService.getFileByUserFile(user.getId(), id);
        //System.out.println(file);
        if (null == file) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().println(new ObjectMapper().writeValueAsString(ResponseVo.fail(-404,"文件不存在")));
            return;
        }
        try {
            // 解压文件名
            String unzipFileName = file.getFilename();



            // 创建输出流
            OutputStream os = response.getOutputStream();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(unzipFileName, "UTF-8"));

            // 读取压缩文件
            FileInputStream fis = new FileInputStream(fileSystemProperties.getSrc() + file.getFilename() + ".zip");
            ZipInputStream zipIn = new ZipInputStream(fis);

            // 解压文件
            zipIn.getNextEntry();

            ByteArrayOutputStream bao = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = zipIn.read(buffer)) != -1) {
                bao.write(buffer,0, bytesRead);
            }

            // 关闭流
            zipIn.close();
            fis.close();

            // 输出
            byte[] byteArray = bao.toByteArray();
            int size = user.getIsVip() ? byteArray.length : 50 * 1024;
            int start = 0;
            int end = Math.min(size,byteArray.length);
            // 循环逐步输出数据流
            while (start < byteArray.length) {
                // 将当前块的数据写入response的输出流
                os.write(byteArray, start, end - start);
                // 刷新缓冲区
                os.flush();
                if (!user.getIsVip()) {
                    Thread.sleep(1000);
                }
                // 更新起始位置和结束位置
                start = end;
                end = Math.min(start + size, byteArray.length);
            }

            os.close();
            bao.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
