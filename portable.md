# 利用portable-config-maven-plugin插件实现多环境部署

[portable-config-maven-plugin](https://github.com/juven/portable-config-maven-plugin)是一个maven插件,
在打war包的时候,替换war包中的配置文件（支持`.xml` `.properties` `.sh` `yml`）,利用它我们可以非常方便无侵入性的实现各种环境的打包。
具体的使用方法请参照[GitHub](https://github.com/juven/portable-config-maven-plugin)上的介绍,我们这介绍我们项目的用法。

> 官方版本不支持yml，1.1.60-SNAPSHOT版本是我自己开发的 支持yml

## 配置pom.xml

添加`portable-config-maven-plugin`插件

```xml
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
```

## 添加各环境的配置文件

在`resources`目录添加`portable`目录,新建两个xml文件 `test.xml` 和 `product.xml`

![portable](images/portable.png)

`test.xml`配置测试环境,`product.xml`配置生产环境,内容就是配置需要替换的配置文件。

例如如下配置,就是替换`redis.properties`和`rabbitmq.properties`两个配置文件中的相应key

```xml
<?xml version="1.0" encoding="utf-8" ?>
<portable-config>
    <config-file path="WEB-INF/classes/cache/redis.properties">
        <replace key="redis.hostname">10.159.191.108</replace>
        <replace key="redis.port">6380</replace>
        <replace key="redis.password"></replace>
        <replace key="redis.db">3</replace>
        <replace key="redis.timeout">20000</replace>
    </config-file>
    <config-file path="WEB-INF/classes/rabbitmq.properties">
        <replace key="season.rabbitmq.userName">wcm</replace>
        <replace key="season.rabbitmq.password">W1HG*BXT</replace>
        <replace key="season.rabbitmq.host">10.159.33.143</replace>
    </config-file>
</portable-config>
```

## 如何使用

在maven打包的时候添加portable插件的参数即可。

```bash
    mvn clean package -DportableConfig=src/main/resources/portable/test.xml
```

`portableConfig`参数就是指定在执行替换的时候需要使用哪个配置文件,在打war包的时候就会使用该配置文件进行相应的替换。


