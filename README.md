# 为什么要用框架

* 重用代码
* 统一规范
* 简化开发
* 降低维护难度
* 知识积累

ssf 特点

* 100% spring boot原味 
* 开箱即用

# 开始使用

pom.xml 配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.example</groupId>
	<artifactId>myproject</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	
	<!-- Inherit defaults from ssf -->
	<parent>
        <artifactId>ssf-parent</artifactId>
        <groupId>com.icourt</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    	
	<packaging>war</packaging>

	<!-- 添加ssf-core依赖 -->
	<dependencies>
		<dependency>
			<groupId>com.icourt</groupId>
			<artifactId>ssf-core</artifactId>
		</dependency>
	</dependencies>


	<build>
        <finalName>example</finalName>
        <plugins>
			<!-- 多环境打包插件【必须在spring boot插件前面】 -->
			<plugin>
				<groupId>com.juvenxu.portable-config-maven-plugin</groupId>
				<artifactId>portable-config-maven-plugin</artifactId>
				<version>1.1.60-SNAPSHOT</version>
				<executions>
					<execution>
						<goals>
							<goal>replace-package</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
			<!-- spring boot 打包插件 -->
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
	<!-- icourt maven 仓库地址 -->
	<repositories>
        <repository>
			<id>icourt-snapshot</id>
			<url>http://nexus.alphalawyer.cn/repository/icourt-snapshots/</url>
		</repository>
        <repository>
            <id>icourt-release</id>
            <url>http://nexus.alphalawyer.cn/repository/icourt-release/</url>
        </repository>
	</repositories>
	<pluginRepositories>
		<pluginRepository>
			<id>icourt-maven-plugin-snapshot-repository</id>
			<url>http://nexus.alphalawyer.cn/repository/icourt-snapshots/</url>
		</pluginRepository>
        <pluginRepository>
            <id>icourt-maven-plugin-snapshot-release</id>
            <url>http://nexus.alphalawyer.cn/repository/icourt-release/</url>
        </pluginRepository>
	</pluginRepositories>

</project>

```
其他和spring boot都是一致的，可以[参考官方文档](http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/)。

ssf 所有开发功能完全遵循spring boot的规范，都是条件式配置。ssf提供如下功能：

* `ApplicationAutoConfiguration` ssf的基本一些配置
* `ApplicationDataSourceAutoConfiguration` 多数据源自动配置
* `ApplicationDaoAutoConfiguration` 基于jdbcTemplate的通用dao
* `ApplicationRedisAutoConfiguration` redis配置
* `ApplicationSwaggerAutoConfiguration` swagger配置
* `ApplicationErrorAutoConfiguration` 异常处理
* `ApplicationQuartzAutoConfiguration` 定时任务
* `JwtAutoConfiguration` jwt相关
* `@EnableJsonLogger` 输出json格式日志

# ApplicationAutoConfiguration

ssf的一些基本配置

`XssConfiguration` xss过滤

`CacheFilter` 缓存拦截器，用于清空线程缓存

注册 `FastJsonHttpMessageConverter4` 


