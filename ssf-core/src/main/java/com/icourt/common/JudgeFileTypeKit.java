package com.icourt.common;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
常用文件的文件头如下：
JPEG (jpg)，文件头：FFD8FF
PNG (png)，文件头：89504E47
GIF (gif)，文件头：47494638
TIFF (tif)，文件头：49492A00
Windows Bitmap (bmp)，文件头：424D
Adobe Photoshop (psd)，文件头：38425053
Rich Text Format (rtf)，文件头：7B5C7274
XML (xml)，文件头：3C3F786D
HTML (html)，文件头：3C68746D
Email [thorough only] (eml)，文件头：52656365
MS Word/Excel (xls.or.doc)，文件头：D0CF11E0
MS Access (mdb)，文件头：5374616E64617264204A
WordPerfect (wpd)，文件头：FF575043
Postscript. (eps.or.ps)，文件头：252150532D41646F6265
Adobe Acrobat (pdf)，文件头：255044462D312E
Quicken (qdf)，文件头：AC9EBD8F
Windows Password (pwl)，文件头：E3828596
ZIP Archive (zip)，文件头：504B0304
RAR Archive (rar)，文件头：52617221
Wave (wav)，文件头：57415645
AVI (avi)，文件头：41564920
Real Audio (ram)，文件头：2E7261FD
Real Media (rm)，文件头：2E524D46
MPEG (mpg)，文件头：000001BA
MPEG (mpg)，文件头：000001B3
Quicktime (mov)，文件头：667479707
Windows Media (asf)，文件头：3026B2758E66CF11
MIDI (mid)，文件头：4D546864
*/

/**
 * Title:判断文件类型
 */
public class JudgeFileTypeKit {

    private static Logger logger = Logger.getLogger(JudgeFileTypeKit.class);
    //不校验真实文件类型的后缀
    private static String[] uncheckFileRealtypeSuffix = {"txt","docx","xlsx","pptx"};
    /**
     * 根据文件读取文件真实类型
     * @param file 文件
     * @return
     */
    public static String getTypeByFile(File file) {
        try {
            FileInputStream is = new FileInputStream(file);
            return getTypeByStream(is);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据文件路径读取文件真实类型
     * @param filePath 文件绝对路径
     * @return
     */
    public static String getTypeByFilePath(String filePath) {
        try {
            FileInputStream is = new FileInputStream(filePath);
            return getTypeByStream(is);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据文件流读取图片文件真实类型
     *  txt文件后缀返回不确定
     *  "docx","xlsx","pptx" 后缀返回zip
     * @param is
     * @return 文件类型
     */
    public static String getTypeByStream(FileInputStream is) {
        byte[] b = new byte[16];
        try {
            is.read(b, 0, b.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String type = bytesToHexString(b).toUpperCase();
        //System.out.println("type:" + type);
        if (type.startsWith("FFD8FF")) {
            return "jpg";
        } else if (type.startsWith("89504E47")) {
            return "png";
        } else if (type.startsWith("47494638")) {
            return "gif";
        } else if (type.contains("49492A00")) {
            return "tif";
        } else if (type.startsWith("424D")) {
            return "bmp";
        } else if (type.contains("38425053")) {
            return "psd";
        } else if (type.contains("7B5C7274")) {
            return "rtf";
        } else if (type.contains("3C3F786D")) {
            return "xml";
        } else if (type.contains("3C68746D")) {
            return "html";
        } else if (type.contains("3C21444F")) {
            return "htm";
        } else if (type.contains("52656365")) {
            return "eml";
        } else if (type.contains("D0CF11E0")) {//office 文件类型 xls,doc,ppt
            return "office";
        } else if (type.contains("5374616E64617264204A")) {
            return "mdb";
        } else if (type.contains("255044462D312E")) {
            return "pdf";
        } else if (type.contains("504B0304")) {
            return "zip";
        } else if (type.contains("52617221")) {
            return "rar";
        } else if (type.contains("57415645")) {
            return "wav";
        } else if (type.contains("41564920")) {
            return "avi";
        } else if (type.contains("2E524D46")) {
            return "rm";
        } else if (type.contains("000001BA") || type.contains("000001B3")) {
            return "mpg";
        } else if (type.contains("667479707")) {
            return "mov";
        } else if (type.contains("4D546864")) {
            return "mid";
        }
        return type;
    }


    /**
     *  校验上传的文件是否符合要求的文件类型
     * @param filePath 文件绝对路径
     * @param fileSuffix
     * @return
     */
    public static boolean checkFileType(String filePath, String... fileSuffix){
        return checkFileType(new File(filePath),fileSuffix);
    }
    /**
     * 校验上传的文件是否符合要求的文件类型
     * "txt","docx","xlsx","pptx" 文件 只会校验后缀名,不校验头信息
     * @param file       上传的文件对象
     * @param fileSuffix 要求的文件类型
     */
    public static boolean checkFileType(File file, String... fileSuffix){
        //fileSuffix参数为null返回
        if (fileSuffix == null||fileSuffix.length==0) {
            return false;
        }
        //file参数为null 返回
        if (file == null) {
            return false;
        }
        boolean isCorrectExt = false;//后缀名是否正确 true 正确 false 错误
        String originalFileName = file.getName();
        // 文件扩展名
        String fileExt = originalFileName.substring(originalFileName.lastIndexOf(".") + 1, originalFileName
                .length());
        //校验后缀名
        for (int i = 0; i < fileSuffix.length; i++) {
            String eachExt = fileSuffix[i];
            if (eachExt == null || eachExt.equals("")) {
                continue;
            }
            if (eachExt.equalsIgnoreCase(fileExt)) {
                isCorrectExt = true;//后缀满足条件
                break;
            }
        }
        if (!isCorrectExt) {
            return false;
        }
        //校验文件真实类型
        //排除掉不校验真实文件类型
        if(uncheckFileRealtypeSuffix!=null){
            for(int i=0;i<uncheckFileRealtypeSuffix.length;i++){
                String uncheckSuffix=uncheckFileRealtypeSuffix[i];
                if(uncheckSuffix==null||uncheckSuffix.equals("")){
                    continue;
                }
                if(fileExt.equalsIgnoreCase(uncheckSuffix)){//上传文件后缀名在不校验真实类型中，直接返回
                    return true;
                }
            }
        }
        boolean isCorrectRealExt = false;
        //根据文件头信息获取文件类型
        String fileRealType = getTypeByFile(file);
        for (int i = 0; i < fileSuffix.length; i++) {
            String eachExt = fileSuffix[i];
            if (eachExt == null || eachExt.equals("")) {
                continue;
            }
            //传入office类型，转为获取的真实类型，用于后面比较
            if("doc".equalsIgnoreCase(eachExt)||"xls".equalsIgnoreCase(eachExt)||"ppt".equalsIgnoreCase(eachExt)){
                eachExt="office";
            }
            if (eachExt.equalsIgnoreCase(fileRealType)) {
                isCorrectRealExt = true;//文件类型满足条件
                break;
            }
        }
        if (!isCorrectRealExt) {
            logger.error("文件后缀为：" + fileExt + " 文件真实类型为:" + fileRealType + " 文件路径为:" + file.getAbsolutePath());
            return false;
        }
        return true;
    }

    /**
     * byte数组转换成16进制字符串
     *
     * @param src
     * @return
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
