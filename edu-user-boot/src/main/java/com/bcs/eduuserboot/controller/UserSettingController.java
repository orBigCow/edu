package com.bcs.eduuserboot.controller;

import com.bcs.eduuserboot.entity.FileSystem;
import com.bcs.eduuserboot.service.UserSettingService;
import org.csource.common.IniFileReader;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@RestController
@RequestMapping("/userSetting")
@CrossOrigin
public class UserSettingController {

    @Autowired
    private UserSettingService userSettingService;

    // fastdfs服务器ip
    private static String fastdfsIp = null;

    static{
        Properties props = new Properties();
        InputStream inputStream = IniFileReader.loadFromOsFileSystemOrClasspathAsStream("config/fastdfs-client.properties");
        if (inputStream != null) {
            try {
                props.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 从配置文件中取出ip
        fastdfsIp = props.getProperty("fastdfs.tracker_servers").split(":")[0];
    }

    /**
     *   上传图片到fastdfs
     * @param file 要上传的图片
     * @return
     */
    @RequestMapping("/upload")
    public FileSystem upload(@RequestParam("file") MultipartFile file) {

        FileSystem fileSystem = new FileSystem();
        //获得文件的原始名称
        String originalFilename = file.getOriginalFilename();
        //获得后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        try {
            //加载配置文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //创建tracker客户端
            TrackerClient trackerClient = new TrackerClient();
            //根据tracker客户端创建连接
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageServer = null;
            //定义storage客户端
            StorageClient1 client = new StorageClient1(trackerServer, storageServer);
            //文件元信息
            NameValuePair[] list = new NameValuePair[1];
            list[0] = new NameValuePair("fileName", originalFilename);
            // 上传到fastdfs，返回fileId
            String fileId = client.upload_file1(file.getBytes(), suffix, list);
            trackerServer.close();
            // 封装数据对象
            fileSystem.setFileId(fileId);
            fileSystem.setFilePath(fileId);
            fileSystem.setFileName(originalFilename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileSystem;
    }

    /**
     *   修改用户信息 （用户名，头像地址）
     * @param userId  要修改信息的用户id
     * @param newName  新的用户名
     * @param fileId  头像上传到fastdfs后返回的fileId
     */
    @RequestMapping("/updateUser")
    public void updateUserInfo(int userId, String newName, String fileId) {
        if (fileId == null || fileId == "") {
            userSettingService.updateUserInfo(userId, newName, null);
        } else {
            String headImg = "http://" + fastdfsIp + "/" + fileId;
            userSettingService.updateUserInfo(userId, newName, headImg);
        }

    }

    /**
     *   修改密码
     * @param userId  要修改密码的用户id
     * @param newPwd  新密码
     */
    @RequestMapping("/updatePassword")
    public void updateUserInfo(int userId, String newPwd) {
        userSettingService.updatePassword(userId, newPwd);
    }
}
