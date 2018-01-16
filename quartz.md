
# ssf-quartz

ssf集成了quartz，并提供了整个的管理定时任务的功能。

## 配置

### 1.配置pom

添加依赖
    
 ```xml
 <dependency>
     <groupId>org.quartz-scheduler</groupId>
     <artifactId>quartz</artifactId>
 </dependency>
 ```


### 2.执行SQL

`quartz`支持集群部署方式，集群使用数据库锁和触发器来实现，
所以需要先创建表。如果不需要集群部署，不需要执行。

**建表的SQL必须和quartz的版本是一致的**。
`ssf-quartz`依赖quartz 2.2，[点击下载建表SQL（MySQL）](ssf-core/DB/qrtz.sql)

里面还包含了`ssf-quartz`的管理的一个表`ssf_quartz_job_config`。
如果需要其他版本的quartz或其他类型的数据库，请前往官网自行下载。

### 3.配置quartz.properties

`quartz.properties`为quartz的配置文件，参考官方文档即可，该配置放在classpath根目录。

我添加了一个自定义配置项，用于指定quartz要使用的数据源

    #quartz要使用的数据源名称,没有配置使用默认数据源
    ssf.quartz.dataSource.name=master


如果使用集群的部署方式，`ssf-quartz`提供了数据源提供者。

    org.quartz.dataSource.myDS.connectionProvider.class = com.icourt.quartz.ConnectionPoolConnectionProvider

使用集群部署方式，需要设置当前应用的名称，每个不同的应用必须不一样。

**该项必须配置**

    org.quartz.scheduler.instanceName = 应用唯一标识
    
`quartz.properties`配置示例

```properties
    
#============================================================
# Configure Main Scheduler Properties
#===========================================================

org.quartz.scheduler.instanceName = myproject
org.quartz.scheduler.instanceId = AUTO

#===========================================================
# Configure ThreadPool
#===========================================================

org.quartz.threadPool.class = org.quartz.simpl.SimpleThreadPool
org.quartz.threadPool.threadCount = 25
org.quartz.threadPool.threadPriority = 5

#===========================================================
# Configure JobStore
#===========================================================

org.quartz.jobStore.misfireThreshold = 60000

org.quartz.jobStore.class = org.quartz.impl.jdbcjobstore.JobStoreTX
org.quartz.jobStore.useProperties = false
org.quartz.jobStore.dataSource = myDS
#    org.quartz.jobStore.nonManagedTXDataSource = myDS
org.quartz.jobStore.tablePrefix = qrtz_

org.quartz.jobStore.isClustered = true
org.quartz.jobStore.clusterCheckinInterval = 30000

#===========================================================
# Configure Datasources
#===========================================================


#ConnectionProvider
org.quartz.dataSource.myDS.connectionProvider.class = com.icourt.quartz.ConnectionPoolConnectionProvider

#quartz要使用的数据源名称,没有配置使用默认数据源
#ssf.quartz.dataSource.name=master


```

### 4.实现权限管理接口

实现接口 `IQuartzAuthService` 控制管理定时任务相关接口的权限

```java

public class QuartzAuthService implements IQuartzAuthService{
        /**
         * 获取当前用户名或id
         *
         * @return 用户标识
         */
        public String getCurrUser(){
            return "当前登录用户名";
        }
    
        /**
         * 是否拥有操作权限
         * @return 是否
         */
        public boolean hasRight(){
            return true;//有权限操作定时任务
        }
}

``` 

## 开发一个定时任务

### 1.编写代码

实现接口`IApplicationJob`

```java
public class TestJob implements IApplicationJob{
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("我是定时任务......");
    }
}
```   

### 2.配置定时任务

调用`QuartzController`相关方法来操作定时任务。

**调度的配置都是实时生效的,不需要重启应用**。

> 新增定时任务，直接往表`ssf_quartz_job_config`添加数据，重启下应用也可以
    
如果需要自己开发管理也很简单,`ssf-quartz`提供了一个`JobKit`工具类。主要有三个方法:
    
    addJob
    updateJob
    deleteJob
    
意思都显而易见,在对表`ssf_quartz_job_config`进行增改删的时候,调用`JobKit`中的相应方法即可。