/*
 * @date 2017年02月15日 15:20
 */
package com.icourt.common;

import com.icourt.core.error.ParamException;
import org.apache.commons.io.FileUtils;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @author june
 */
public class FileKit {

    /**
     * 保存上传文件到本地
     * @param file 用户上传文件
     * @param saveDir 要保存的一级目录，比如 /data/
     * @param subDir 要保存的二级目录 比如 image
     * @param extNames 支持的扩展名 如 {"jpg","gif"}
     * @param maxSize 最大大小 单位字节
     * @return 保存后的文件路径，从二级目录开始 如  /image/201702/15/aaa-as-as-asa-sa-sa.jpg
     */
    public static String saveFileToLocal(MultipartFile file, String saveDir, String subDir, String[] extNames, long maxSize){
        Assert.notNull(saveDir);
        Assert.notNull(subDir);
        Assert.notEmpty(extNames);
        if(file == null || file.isEmpty()){
            throw new ParamException("上传失败，文件不能为空");
        }
        long size = file.getSize();
        if(size>maxSize){
            throw new ParamException("上传失败，大小不能超过"+maxSize/1000*1000+"M");
        }
        String dateDir = File.separator + subDir + File.separator+ DateKit.getFormatDate(new Date(),"yyyyMM");
        dateDir += File.separator+DateKit.getFormatDate(new Date(),"dd");
        if(saveDir.endsWith(File.separator)){
            saveDir = saveDir.substring(0,saveDir.length()-1) + dateDir;
        }else{
            saveDir += dateDir;
        }
        //检查目录
        File uploadDir = new File(saveDir);
        boolean flg = true;
        if(!uploadDir.exists()){
            flg = uploadDir.mkdirs();
        }
        if(flg && !uploadDir.isDirectory()){
            throw new ParamException("上传目录不存在"+saveDir);
        }
        //检查目录写权限
        if(!uploadDir.canWrite()){
            throw new ParamException("上传目录没有写权限"+saveDir);
        }
        String originalFileName = file.getOriginalFilename();
        // 文件扩展名
        String extName = originalFileName.substring(originalFileName.lastIndexOf(".") + 1, originalFileName
                .length());

        //重命名文件
        String newFileName = UUID.randomUUID().toString()+"."+extName;
        File newFile = new File(saveDir+File.separator+newFileName);
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(),newFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("上传文件失败，写文件出现异常");
        }
        //检查文件类型
        if(ArrayKit.asList(extNames).indexOf(extName)==-1 || !JudgeFileTypeKit.checkFileType(newFile,extNames)){
            newFile.delete();
            throw new ParamException("不支持上传该类型的文件 ."+extName);
        }
        return dateDir+File.separator+newFileName;
    }

}
