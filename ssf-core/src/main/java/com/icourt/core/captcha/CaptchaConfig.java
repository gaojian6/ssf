/*
 * @date 2016年12月12日 20:38
 */
package com.icourt.core.captcha;

/**
 * @author june
 */
public class CaptchaConfig {

    //验证码图片宽度
    private int width = 200;
    //验证码图片高度
    private int height = 50;
    //是否有边框
    private boolean border = true;
    //边框颜色
    private String borderColor = "black";
    //边框粗细度  默认为1
    private int borderThickness = 1;
    //验证码文本字符内容范围
    private String charString = "abcde2345678gfynmnpwx";
    //验证码文本字符长度  默认为5
    private int charLength = 5;
    //验证码文本字符大小  默认为40
    private int fontSize = 40;
    //验证码文本字符颜色
    private String fontColor = "black";
    //验证码文本字符间距  默认为2
    private int charSpace = 2;
    //验证码噪点颜色
    private String noiseColor = "black";
    // 验证码背景颜色渐进
    private String clearFrom = "lightGray";
    //验证码背景颜色渐进
    private String clearTo = "white";

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean getBorder() {
        return border;
    }

    public void setBorder(boolean border) {
        this.border = border;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
    }

    public String getCharString() {
        return charString;
    }

    public void setCharString(String charString) {
        this.charString = charString;
    }

    public int getCharLength() {
        return charLength;
    }

    public void setCharLength(int charLength) {
        this.charLength = charLength;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public int getCharSpace() {
        return charSpace;
    }

    public void setCharSpace(int charSpace) {
        this.charSpace = charSpace;
    }

    public String getNoiseColor() {
        return noiseColor;
    }

    public void setNoiseColor(String noiseColor) {
        this.noiseColor = noiseColor;
    }

    public String getClearFrom() {
        return clearFrom;
    }

    public void setClearFrom(String clearFrom) {
        this.clearFrom = clearFrom;
    }

    public String getClearTo() {
        return clearTo;
    }

    public void setClearTo(String clearTo) {
        this.clearTo = clearTo;
    }
}
