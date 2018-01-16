package com.icourt.core;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

/**
 * 启动banner
 *
 * @author june
 */
public class ApplicationBanner implements Banner {

    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
        String version = ApplicationVersion.getVersion();
//        String version = "1.0-SNAPSHOT";
        version = (version == null ? "" : " (v" + version + ")");
        out.println(AnsiOutput.toString(AnsiColor.GREEN,"█▀▀ █▀▀█ █▀▀ █▀▀ ▀▀█▀▀ █░░█ ",AnsiColor.RED," █▀▀ ▀▀█▀▀ █▀▀█ █▀▀▄ █░░ █▀▀ ", AnsiColor.GREEN," █▀▀ █▀▀█ █▀▀ ▀▀█▀▀"));
        out.println(AnsiOutput.toString(AnsiColor.GREEN,"▀▀█ █▄▄█ █▀▀ █▀▀ ░░█░░ █▄▄█ ",AnsiColor.RED," ▀▀█ ░░█░░ █▄▄█ █▀▀▄ █░░ █▀▀ ", AnsiColor.GREEN," █▀▀ █▄▄█ ▀▀█ ░░█░░"));
        out.println(AnsiOutput.toString(AnsiColor.GREEN,"▀▀▀ ▀░░▀ ▀░░ ▀▀▀ ░░▀░░ ▄▄▄█ ",AnsiColor.RED," ▀▀▀ ░░▀░░ ▀░░▀ ▀▀▀░ ▀▀▀ ▀▀▀ ", AnsiColor.GREEN," ▀░░ ▀░░▀ ▀▀▀ ░░▀░░"));
        out.println(AnsiOutput.toString(AnsiColor.GREEN," :: safety stable fast :: ", AnsiColor.DEFAULT, AnsiStyle.FAINT," Spring Boot", SpringBootVersion.getVersion()));
        out.println(AnsiOutput.toString(AnsiColor.GREEN," :: http://www.icourt.cc   :: ", AnsiColor.DEFAULT, AnsiColor.RED," SSF ",version));

        out.println();
    }
}
