package com.icourt.common;

import com.icourt.core.json.JsonKit;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Substitution;
import org.apache.oro.text.regex.Util;
import org.springframework.web.util.HtmlUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author june
 */
public class XSSKit {
    static final Pattern SCRIPT_TAG_PATTERN = Pattern.compile(
            "<script[^>]*>.*</script[^>]*>", Pattern.CASE_INSENSITIVE);

//    static final PatternCompiler pc = new Perl5Compiler();
//    static final Perl5Matcher matcher = new Perl5Matcher();


    public static String antiXSS(String content) {
        if(content == null || "".equals(content)) {
            return "";
        }
        String old = content;
        String ret = _antiXSS(content);
        while (!ret.equals(old)) {
            old = ret;
            ret = _antiXSS(ret);
        }
        return ret;
    }

    private static String _antiXSS(String content) {
        try {
            return stripAllowScriptAccess(stripProtocol(stripCssExpression(stripAsciiAndHex(stripEvent(stripScriptTag(content))))));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String stripScriptTag(String content) {
        Matcher m = SCRIPT_TAG_PATTERN.matcher(content);
        content = m.replaceAll("");
        return content;
    }

    private static String stripEvent(String content) throws Exception {
        String[] events = { "onmouseover", "onmouseout", "onmousedown",
                "onmouseup", "onmousemove", "onclick", "ondblclick",
                "onkeypress", "onkeydown", "onkeyup", "ondragstart",
                "onerrorupdate", "onhelp", "onreadystatechange", "onrowenter",
                "onrowexit", "onselectstart", "onload", "onunload",
                "onbeforeunload", "onblur", "onerror", "onfocus", "onresize",
                "onscroll", "oncontextmenu" };
        for (String event : events) {
            PatternCompiler pc = new Perl5Compiler();
            Perl5Matcher matcher = new Perl5Matcher();
            org.apache.oro.text.regex.Pattern p = pc.compile("(<[^>]*)("
                    + event + ")([^>]*>)", Perl5Compiler.CASE_INSENSITIVE_MASK);
            if (null != p)
                content = Util.substitute(matcher, p, new Perl5Substitution(
                                "$1" + event.substring(2) + "$3"), content,
                        Util.SUBSTITUTE_ALL);

        }
        return content;
    }

    private static String stripAsciiAndHex(String content) throws Exception {
        // filter &# \00xx
        PatternCompiler pc = new Perl5Compiler();
        Perl5Matcher matcher = new Perl5Matcher();
        org.apache.oro.text.regex.Pattern p = pc.compile(
                "(<[^>]*)(&#|\\\\00)([^>]*>)",
                Perl5Compiler.CASE_INSENSITIVE_MASK);
        if (null != p)
            content = Util
                    .substitute(matcher, p, new Perl5Substitution("$1$3"),
                            content, Util.SUBSTITUTE_ALL);
        return content;
    }

    private static String stripCssExpression(String content) throws Exception {
        PatternCompiler pc = new Perl5Compiler();
        Perl5Matcher matcher = new Perl5Matcher();
        org.apache.oro.text.regex.Pattern p = pc.compile(
                "(<[^>]*style=.*)/\\*.*\\*/([^>]*>)",
                Perl5Compiler.CASE_INSENSITIVE_MASK);
        if (null != p)
            content = Util
                    .substitute(matcher, p, new Perl5Substitution("$1$2"),
                            content, Util.SUBSTITUTE_ALL);

        p = pc
                .compile(
                        "(<[^>]*style=[^>]+)(expression|javascript|vbscript|-moz-binding)([^>]*>)",
                        Perl5Compiler.CASE_INSENSITIVE_MASK);
        if (null != p)
            content = Util
                    .substitute(matcher, p, new Perl5Substitution("$1$3"),
                            content, Util.SUBSTITUTE_ALL);

        p = pc.compile("(<style[^>]*>.*)/\\*.*\\*/(.*</style[^>]*>)",
                Perl5Compiler.CASE_INSENSITIVE_MASK);
        if (null != p)
            content = Util
                    .substitute(matcher, p, new Perl5Substitution("$1$2"),
                            content, Util.SUBSTITUTE_ALL);

        p = pc
                .compile(
                        "(<style[^>]*>[^>]+)(expression|javascript|vbscript|-moz-binding)(.*</style[^>]*>)",
                        Perl5Compiler.CASE_INSENSITIVE_MASK);
        if (null != p)
            content = Util
                    .substitute(matcher, p, new Perl5Substitution("$1$3"),
                            content, Util.SUBSTITUTE_ALL);
        return content;
    }

    private static String stripProtocol(String content) throws Exception {
        String[] protocols = { "javascript","alert", "vbscript", "livescript",
                "ms-its", "mhtml", "data", "firefoxurl", "mocha" };
        for (String protocol : protocols) {
            PatternCompiler pc = new Perl5Compiler();
            Perl5Matcher matcher = new Perl5Matcher();
            org.apache.oro.text.regex.Pattern p = pc.compile("(<[^>]*)"
                            + protocol + ":([^>]*>)",
                    Perl5Compiler.CASE_INSENSITIVE_MASK);
            if (null != p)
                content = Util.substitute(matcher, p, new Perl5Substitution(
                        "$1/$2"), content, Util.SUBSTITUTE_ALL);
        }
        return content;
    }

    private static String stripAllowScriptAccess(String content)
            throws Exception {
        PatternCompiler pc = new Perl5Compiler();
        Perl5Matcher matcher = new Perl5Matcher();
        org.apache.oro.text.regex.Pattern p = pc.compile(
                "(<[^>]*)AllowScriptAccess([^>]*>)",
                Perl5Compiler.CASE_INSENSITIVE_MASK);
        if (null != p)
            content = Util.substitute(matcher, p, new Perl5Substitution(
                    "$1Allow_Script_Access$2"), content, Util.SUBSTITUTE_ALL);
        return content;
    }

    /**
     * html反转义
     * @param input
     * @return
     */
    public static String htmlUnescape(String input){
        return HtmlUtils.htmlUnescape(input);
    }

    /**
     * html转义
     * @param input
     * @return
     */
    public static String htmlEscape(String input){
        return HtmlUtils.htmlEscape(input);
    }

    /**
     * 对已经转义的json 反转义
     * @param paramJson 转义的json
     * @return 正常的json
     */
    public static String jsonHtmlUnescape(String paramJson){
        assert paramJson != null;
        String json = htmlUnescape(paramJson);
        Map<String,Object> map;
        try {
            map = JsonKit.parseObject(json);
        } catch (Exception e) {
            throw new RuntimeException("json参数解析异常",e);
        }
        replaceJsonValue(map);
        return JsonKit.toJSONString(map);
    }

    /**
     * 递归转换json的value
     * @param map map
     */
    private static void replaceJsonValue(Map<String,Object> map){
        if(map == null || map.isEmpty()){
            return;
        }
        for (String valueKey : map.keySet()) {
            Object obj = map.get(valueKey);
            if(obj == null){
                return;
            }
            if(ClassKit.isBaseDataType(obj.getClass())){
                if(obj instanceof String){
                    obj = htmlEscape(obj.toString());
                }
                map.put(valueKey,obj);
            }else{
                String jsonStr = JsonKit.toJSONString(obj);
                Map<String,Object> map2 = JsonKit.parseObject(jsonStr);
                replaceJsonValue(map2);
                map.put(valueKey,JsonKit.toJSONString(map2));
            }
        }
    }

}
