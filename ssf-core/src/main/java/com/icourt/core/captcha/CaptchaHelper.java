/*
 * @date 2016年12月12日 20:48
 */
package com.icourt.core.captcha;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import com.icourt.common.StrKit;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * 验证码工具类
 * @author june
 */
public class CaptchaHelper {

    /**
     * 输出验证码到前台
     * @param req request
     * @param resp response
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    public void captcha(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        captcha(new CaptchaConfig(),req,resp);
    }

    /**
     * 输出验证码到前台
     * @param captchaConfig 验证码配置
     * @param req request
     * @param resp response
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    public void captcha(CaptchaConfig captchaConfig, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Config config = buildConfig(captchaConfig);
        Producer kaptchaProducer = config.getProducerImpl();
        String sessionKeyValue = config.getSessionKey();
        String sessionKeyDateValue = config.getSessionDate();

        // Set standard HTTP/1.1 no-cache headers.
        resp.setHeader("Cache-Control", "no-store, no-cache");

        // return a jpeg
        resp.setContentType("image/jpeg");

        // create the text for the image
        String capText = kaptchaProducer.createText();

        // store the text in the session
        req.getSession().setAttribute(sessionKeyValue, capText);

        // store the date in the session so that it can be compared
        // against to make sure someone hasn't taken too long to enter
        // their kaptcha
        req.getSession().setAttribute(sessionKeyDateValue, new Date());

        // create the image with the text
        BufferedImage bi = kaptchaProducer.createImage(capText);

        ServletOutputStream out = resp.getOutputStream();

        // write the data out
        ImageIO.write(bi, "jpg", out);

        // set the attributes after we write the image in case
        // the image writing fails.
        // store the text in the session
        req.getSession().setAttribute(sessionKeyValue, capText);

        // store the date in the session so that it can be compared
        // against to make sure someone hasn't taken too long to enter
        // their kaptcha
        req.getSession().setAttribute(sessionKeyDateValue, new Date());
    }

    /**
     * 生成验证码的Base64码
     * @param req request
     * @return Base64码
     */
    public String generateImageBase64(HttpServletRequest req){
        return generateImageBase64(new CaptchaConfig(),req);
    }

    /**
     *  生成验证码的Base64码
     * @param captchaConfig 验证码配置
     * @param req request
     * @return Base64码
     */
    public String generateImageBase64(CaptchaConfig captchaConfig,HttpServletRequest req){
        Config config = buildConfig(captchaConfig);
        Producer kaptchaProducer = config.getProducerImpl();
        String sessionKeyValue = config.getSessionKey();
        String sessionKeyDateValue = config.getSessionDate();
        // create the text for the image
        String capText = kaptchaProducer.createText();

        // store the text in the session
        req.getSession().setAttribute(sessionKeyValue, capText);

        // store the date in the session so that it can be compared
        // against to make sure someone hasn't taken too long to enter
        // their kaptcha
        req.getSession().setAttribute(sessionKeyDateValue, new Date());

        // create the image with the text
        BufferedImage bi = kaptchaProducer.createImage(capText);

        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpeg", bos);
            // set the attributes after we write the image in case
            // the image writing fails.
            // store the text in the session
            req.getSession().setAttribute(sessionKeyValue, capText);
            byte[] imageBytes = bos.toByteArray();
            Base64 encoder = new Base64();
            //生成验证码的base64码
            return encoder.encodeToString(imageBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if(bos != null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 构造Config
     * @param captchaConfig 验证码配置
     * @return Config
     */
    private Config buildConfig(CaptchaConfig captchaConfig){
        if(captchaConfig==null){
            captchaConfig = new CaptchaConfig();
        }
        Properties props = new Properties();
        props.setProperty(Constants.KAPTCHA_IMAGE_WIDTH,captchaConfig.getWidth()+"");
        props.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT,captchaConfig.getHeight()+"");
        props.setProperty(Constants.KAPTCHA_BORDER,captchaConfig.getBorder()?"yes":"no");
        props.setProperty(Constants.KAPTCHA_BORDER_COLOR,captchaConfig.getBorderColor());
        props.setProperty(Constants.KAPTCHA_BORDER_THICKNESS,captchaConfig.getBorderThickness()+"");
        props.setProperty(Constants.KAPTCHA_NOISE_COLOR,captchaConfig.getNoiseColor());
        props.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING,captchaConfig.getCharString());
        props.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH,captchaConfig.getCharLength()+"");
        props.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR,captchaConfig.getFontColor());
        props.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE,captchaConfig.getFontSize()+"");
        props.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE,captchaConfig.getCharSpace()+"");
        props.setProperty(Constants.KAPTCHA_BACKGROUND_CLR_FROM,captchaConfig.getClearFrom());
        props.setProperty(Constants.KAPTCHA_BACKGROUND_CLR_TO,captchaConfig.getClearTo());
        return new Config(props);
    }

    /**
     * 获取存放验证码的sessionKey
     * @return sessionKey
     */
    public String getSessionKey(){
        return Constants.KAPTCHA_SESSION_KEY;
    }

    /**
     * 获取验证码
     * @param req request
     * @return Captcha
     */
    public String getCaptcha(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return (String) session
                .getAttribute(Constants.KAPTCHA_SESSION_KEY);
    }

    /**
     * 验证验证码是否正确
     * @param code 用户输入验证码
     * @param req request
     * @return true 验证码正确 false不正确
     */
    public boolean validate(String code,HttpServletRequest req) {
        return !StrKit.isEmpty(code) && code.equals(getCaptcha(req));
    }

}
