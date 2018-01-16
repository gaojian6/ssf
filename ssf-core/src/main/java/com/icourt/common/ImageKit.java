/**   
* @Title: ImageUtil.java
* @Package com.haier.core.common
* @Description: 图片处理工具类
* @author june   
* @date 2013-7-22 下午2:09:36
* @version V1.0   
*/
package com.icourt.common;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.MogrifyCmd;


/**
 * @ClassName: ImageUtil
 * @Description: 图片处理工具类，提供剪切缩略图等功能
 * @author june
 *
 */
public class ImageKit {
	/** 
     * ImageMagick的路径 
     */
//    public static String imageMagickPath = null;
    static {  
        /** 
         *  
         * 获取ImageMagick的路径 
         */  
        //linux下不要设置此值，不然会报错  
      //  imageMagickPath = "D:\\ImageMagick-6.8.6-Q16";
    }  
  
    /** 
     *  
     * 根据坐标裁剪图片 
     *  
     * @param srcPath   要裁剪图片的路径 
     * @param newPath   裁剪图片后的路径 
     * @param x         起始横坐标 
     * @param y         起始纵坐标 
     * @param x1        结束横坐标 
     * @param y1        结束纵坐标 
     */  
  
    public static void cutImage(String srcPath, String newPath, int x, int y, int x1, int y1) throws Exception {
        int width = x1 - x;  
        int height = y1 - y;  
        IMOperation op = new IMOperation();  
        op.addImage(srcPath);  
        /** 
         * width：  裁剪的宽度 
         * height： 裁剪的高度 
         * x：       裁剪的横坐标 
         * y：       裁剪的挫坐标 
         */  
        op.crop(width, height, x, y);  
        op.addImage(newPath);  
        ConvertCmd convert = new ConvertCmd();  
  
        // linux下不要设置此值，不然会报错  
        //convert.setSearchPath(imageMagickPath);
  
        convert.run(op);  
    }  
  
    /** 
     *  
     * 根据尺寸缩放图片 
     * @param width             缩放后的图片宽度 
     * @param height            缩放后的图片高度 
     * @param srcPath           源图片路径 
     * @param newPath           缩放后图片的路径 
     */  
    public static void cutImage(int width, int height, String srcPath, String newPath) throws Exception {
        IMOperation op = new IMOperation();  
        op.addImage(srcPath);  
        op.resize(width, height);  
        op.addImage(newPath);  
        ConvertCmd convert = new ConvertCmd();  
        // linux下不要设置此值，不然会报错  
       // convert.setSearchPath(imageMagickPath);  
        convert.run(op);  
  
    }  
  
    /** 
     * 根据宽度缩放图片 
     *  
     * @param width            缩放后的图片宽度 
     * @param srcPath          源图片路径 
     * @param newPath          缩放后图片的路径 
     */  
    public static void cutImage(int width, String srcPath, String newPath)  throws Exception {
        IMOperation op = new IMOperation();  
        op.addImage(srcPath);  
        op.resize(width, null);  
        op.addImage(newPath);  
        ConvertCmd convert = new ConvertCmd();  
        // linux下不要设置此值，不然会报错  
        //convert.setSearchPath(imageMagickPath);  
        convert.run(op);  
    }  
  

    /** 
     * 根据高度缩放图片
     *  
     * @param height            缩放后的图片高度
     * @param srcPath          源图片路径 
     * @param newPath          缩放后图片的路径 
     */  
    public static void cutImageHeight(int height, String srcPath, String newPath)  throws Exception {
        IMOperation op = new IMOperation();  
        op.addImage(srcPath);  
        op.resize(null, height);  
        op.addImage(newPath);  
        ConvertCmd convert = new ConvertCmd();  
        // linux下不要设置此值，不然会报错  
        //convert.setSearchPath(imageMagickPath);  
        convert.run(op);  
    }  
   
    
    /**
	 * 给图片加文字水印
	 * 
	 * @param font
	 *            字体
	 * @param location
	 *            位置 如:southeast右下角 northwest左上角
	 * @param fontSize
	 *            字大小
	 * @param color
	 *            字颜色
	 * @param xy
	 *            坐标
	 * @param text
	 *            文字
	 * @param srcPath
	 *            源图片
	 * @param endPath
	 *            生成图片
	 * @throws Exception
	 */
	public static void addImgText(String font, String location,
                                  Integer fontSize, String color, String xy, String text,
                                  String srcPath, String endPath) throws Exception {
		IMOperation op = new IMOperation();
		op.font(font).gravity(location).pointsize(fontSize).fill(color).draw(
				"text " + xy + " " + text);
		op.addImage();
		op.addImage();
		ConvertCmd convert = new ConvertCmd();
		// linux下不要设置此值，不然会报错
		// convert.setSearchPath(imageMagickPath);
		convert.run(op, srcPath, endPath);
	}
	/**
	 *  给图片加文字水印（支持中文） 不保留原图
	 * @param font 字体
	 * @param xiedu  整行字倾斜度   越大 右侧越低
	 * @param zixiedu  字的倾斜度 越大 往右偏
	 * @param x,y 字最下角的坐标
	 * @param fontSize 字体大小
	 * @param color 颜色
	 * @param text 文字
	 * @param srcPath 图片地址
	 */
	public static  void addTmgText(String font, Integer xiedu,
                                   Integer zixiedu, Integer x, Integer y,
                                   Integer fontSize, String color, String text, String srcPath)throws Exception {
		IMOperation  ap = new IMOperation ();
    	ap.transparent("transparent");
    	ap.font(font);
    	ap.fill(color);
    	ap.pointsize(fontSize);
        ap.annotate(xiedu, zixiedu, x, y, text);
        ap.addImage(srcPath);
        MogrifyCmd mogrify = new MogrifyCmd();
        // linux下不要设置此值，不然会报错
        //mogrify.setSearchPath(imageMagickPath); 
        mogrify.run(ap);
	}
	/**
	 * 给图片加图片水印
	 * 
	 * @param location
	 *            位置 如:southeast右下角 northwest左上角
	 * @param xy
	 *            坐标
	 * @param imageSize
	 *            图片大小
	 * @param logoSrc
	 *            logo地址
	 * @param srcPath
	 *            源图片地址
	 * @param endPath
	 *            生成图片地址
	 * @throws Exception
	 */
	public static void addImgImage(String location, String xy,
                                   String imageSize, String logoSrc, String srcPath, String endPath)
			throws Exception {
		IMOperation op = new IMOperation();
		op.gravity(location).draw(
				"image Over " + xy + " " + imageSize + " '" + logoSrc + "'");
		op.addImage();
		op.addImage();
		ConvertCmd convert = new ConvertCmd();
		// linux下不要设置此值，不然会报错
		// convert.setSearchPath(imageMagickPath);
		convert.run(op, srcPath, endPath);
	}

  
    public static void main(String[] args) throws Exception {
        //cutImage("D:\\test.jpg", "D:\\new.jpg", 98, 48, 370,320);  
      //  cutImage(205,259, "D:\\aaaaaaaaaaaaaaa\\QQ20130516150213.png", "D:\\aaaaaaaaaaaaaaa\\aa_205.JPG");  
       // addImgText("D:\\1.JPG");  
    }  
}
