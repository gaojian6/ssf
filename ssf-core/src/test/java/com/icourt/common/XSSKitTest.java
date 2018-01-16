package com.icourt.common;

import org.junit.Test;

/**
 * @author june
 */
public class XSSKitTest {

    @Test
    public void jsonHtmlUnescape() throws Exception {
        String jsonStr = "{user:{name:'<script>',age:20,person:{name:'<script>',age:20}},aa:'<div>aa</div>',bb:22,cc:'2018-09-11'}";
        String testStr = XSSKit.htmlEscape(jsonStr);
        String result = XSSKit.jsonHtmlUnescape(testStr);
        System.out.println(result);
    }

}