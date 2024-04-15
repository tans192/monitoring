## 监控任务规划

1. 创建定时任务以每隔几分钟检查服务器资源状况，包括磁盘、内存使用率，应用程序状态等，并通过`jiankong.pl`脚本将检查日志发送至消息队列MQ。

2. 另一个定时任务同样每隔几分钟通过`start.pl`脚本来检查特定程序进程状态，若发现未运行则自动启动相应程序。

3. 每天早上8点运行`ok.pl`脚本进行全面巡检，确保服务器正常运行并发送邮件提醒。

## 安装环境准备

### 1. 安装ActiveMQ
  如果尚未安装ActiveMQ，按照先前指南将其安装在`/usr/soft/activemq`路径下。

### 2.下载监控程序

从Gitee仓库或CSDN资源下载并解压监控程序到`/usr/soft/activemqmonitoring`目录下。

[CSDN](https://download.csdn.net/download/u012554661/10723678?spm=1001.2014.3001.5503)
[Gitee]([CSDN](https://download.csdn.net/download/u012554661/10723678?spm=1001.2014.3001.5503)

文件目录：

![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/ef32a53f7c0c4ca9a759cca97b1dc7c6.png)

   ```bash
   chmod -R 755 /usr/soft/activemqmonitoring/Class-Accessor-0.34
   chmod -R 755 /usr/soft/activemqmonitoring/Net-Stomp-0.42
   chmod -R 755 /usr/soft/activemqmonitoring/Net-STOMP-Client-1.2
   ```
![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/e6fea0fe52e541cbb4912d4c5e9d59d4.png)


### 3. 修改MQ地址

将 `monitorApp/classes/applicationContext.xml`，更新其中的MQ服务器地址为实际服务器地址。

![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/5add336bb7b74037aa64348bd5d41c79.png)


### 4. 修改`conf.properties`

修改`monitorApp/classes/conf.properties`文件中的相关配置项。

![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/f22542e3e95b41e0b5ce1687bda3896c.png)

### 5. `run.sh`文件

修改`monitorApp/run.sh`文件，指定服务器上的JDK路径。


![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/d92a4ee0c91b437dbfdd8feee23ce6d6.png)

### 6.修改`jiankong.pl`、`start.pl`和`ok.pl`脚本

修改`pl`文件夹下的`jiankong.pl`、`start.pl`和`ok.pl`脚本，根据实际情况调整其内容。


#### jiankong.pl

![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/d5ef01f73ad64f84a415d7e46fa8f989.png)

![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/b958a1653f024a7b869b44928138d77e.png)


#### start.pl


![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/de969223aba44ae8955ccfeb2544a84c.png)


![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/d9c40568e3cb429980584c81e2bb9d78.png)


若在执行`start.pl`时报错，可能需要编辑ActiveMQ的`bin/env`配置文件以添加缺失的环境变量,编辑  `env` 配置文件后面增加：

![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/70a0858996c04180908d0f53ce7d1f1f.png)

#### ok.pl


![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/5822b83c379a4dc988c91acb3c27fe1a.png)


### 7. 编译安装

分别进入`Class-Accessor-0.34`、`Net-Stomp-0.42`和`Net-STOMP-Client-1.2`目录下执行编译安装操作：

```bash
cd /usr/soft/activemqmonitoring/<package_name>
perl ./Makefile.PL
```
# 如遇错误提示缺少依赖，先执行：

```bash
yum install perl-ExtUtils-CBuilder perl-ExtUtils-MakeMaker
```

![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/461662fce4b84175be9fb9c19ed8ea4b.png)

再：

```shell
make
```

![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/372bc6d86468444f8e964f0eef4a2412.png)

```shell
make install
```


![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/490e7032b3ab41cf8b5331a39beb3449.png)


剩下的两个类似操作。


### 8. 启动ActiveMQ和Java应用程序。


```shell
./usr/soft/activemqmonitoring/monitorApp/run.sh 
```

(记得给run.sh 授权  chmod -R 777 run.sh)  

```bash
chmod -R 777 run.sh
```

`monitorApp` 在最前面有gitee的源码下载链接 需要扩展监控维度的可以去修改

查看java应用启动日志：
```shell
tail -f /usr/soft/activemqmonitoring/monitorApp/run.log
```

### 9. 测试配置好的Perl脚本

测试配置好的Perl脚本，并解决可能出现的问题，比如阿里云ECS环境中SMTP端口限制导致的邮件发送失败，需要修改代码以使用465端口并通过安全协议发送邮件。


接下来测试刚才配置的三个 perl脚本：

```shell
cd /usr/soft/activemqmonitoring/pl
perl ok.pl
```

(此处需要友情提醒一下，如果您发送邮件的程序monitorApp 放在了阿里云的ecs上， run.log 可能会报错：)

![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/e19cbb931bdf4a79b1495b568953f981.png)


阿里云给出如下解释：ECS基于安全考虑，目前已禁用25端口。
所以我们不能使用默认的25端口，但是可以使用：465 安全协议端口进行。此时就需要修改源码.


![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/438ac1ce092f4330bc55b8bff18a8eb6.png)

修改后运行 EmailListener 里面的main方法，会在当前项目的工作目录 bin 目录下生成对应的class文件


将生成的class文件替换到 /usr/soft/activemqmonitoring/monitorApp  对应的包下，


![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/d28a286794e348dda218a66930fffc94.png)

然后将  com.hyaroma.blog.jiankong 对应的进程结束 
ps aux|grep com.hyaroma.blog
kill -9 pid

重新运行 run.sh 然后执行  perl ok.pl 等待接收邮件提醒。

![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/87eedc19b0e24cd2bce574cd87921b66.png)


### 10. 配置Linux定时任务

配置Linux定时任务（crontab）以定时执行上述脚本。
	```bash
	vi /etc/crontab
	```

    ```bash
    # 添加到/etc/crontab
    */3 * * * * root /usr/bin/perl /usr/soft/activemqmonitoring/pl/jiankong.pl
    */3 * * * * root /usr/bin/perl /usr/soft/activemqmonitoring/pl/start.pl
    0 8 * * * root /usr/bin/perl /usr/soft/activemqmonitoring/pl/ok.pl
    ```

含义：
每一分钟执行jiankong.pl脚本
每五分钟执行#start.pl脚本
每天8点定时查看服务器状态并发送邮件

![image.png](http://liweiwstv.oss-cn-beijing.aliyuncs.com/DOC/9b1e84d705f54552a0a69bea12048bae.png)


### 11、启用定时任务
```bash
service crond restart
```