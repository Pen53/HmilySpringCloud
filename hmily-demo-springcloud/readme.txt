Hmily Config
org:
    dromara:
         hmily :
            serializer : kryo
            recoverDelayTime : 128
            retryMax : 30
            scheduledDelay : 128
            scheduledThreadMax :  10
            repositorySupport : db
            started: false
            hmilyDbConfig :
                 driverClassName  : com.mysql.jdbc.Driver
                 #url: jdbc:mysql://rm-bp10o39k37lfxh090.mysql.rds.aliyuncs.com:3306/soul-open?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT&failOverReadOnly=false&autoReconnect=true
                 url: jdbc:mysql://192.168.83.1:3306/tcc?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8&autoReconnect=true
                 username : root
                 password : andy123L

Hmily tcc
查看 hmilyTransactionBootstrap 配置Transaction数据库中
相关服务中是否有正在执行的Hmily tcc Transaction
表名 hmily_XXXX  XXXX为使用hmily Transaction 微服务名 如hmily_order_service
其中order_service为微服务名

所有hmily Transaction 需要执行的confirm_method cancel_method 任务方法 都会记录到相应
hmily_XXXX 表中，如果执行完成则会删除信息

注意事项
1 try方法 抛出异常时 ，会调用cancel_method 方法（此方法执行任务记录数据表中，直到成功才删除数据不执行任务了）






