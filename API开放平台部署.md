# API开放平台 —— 项目部署（使用宝塔面板+docker，一步一步教学）

先总结下自己遇到的坑

1：nacos始终弄不好，打开的时候总是报错（这个我是通过docker logs -f 容器id去查看nacos报错提示，然后一点一点分析原因的，但是主要就要先配置好mysql,mysql里面要有个nacos相关的配置数据库表，然后确定好nacos关联的是不是刚才在mysql中与nacos相关的数据库表，别使用错。）

![image-20240831153841006](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831153841006.png)

2：部署到服务器后，前端访问后端的时候，明明api-back的日志有响应，可是页面始终跳不过去，还提示登录失败。（按F12的时候看见控制台说请求头多出来了，原来是后端项目有个跨域类，宝塔在弄域名跨域的时候又加了个跨域，导致多出来一个请求头，注释掉一个就好了）



3：本地运行项目，用的都是服务器的nacos和mysql,测试后没问题，打成jar包后到服务器上有地方运行失败

，运行失败的时候日志，说是多了个totalNum字段。（最后找到是api-back有依赖那个api-common模块，那个api-common里面一个实体类把totalNum字段删除的时候没重新把api-common模块package再刷新整个maven项目）

![image-20240831153217060](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831153217060.png)

先解答下自己最开始疑惑为什么只要部署api-interface,api-back,api-gateway三个模块就行，其它的像api-common,api-sdk不用部署吗？

这是因为打成jar包的过程，这个项目模块会把依赖的模块也加载上去，我们只需要有端口号的那些模块的jar包就可以



接下来，我从前端项目教大家怎么部署。  （个人不会react，所以直接下载鱼皮哥的源码的）

先说下部署前端流程是什么：主要是要把前端项目执行npm run build命令，生成一个dist文件夹，再dist文件夹内的东西上传到宝塔。



首先在src/requestConfig.ts中 第17行左右

把baseURL: 'http://localhost:7529'这行注释掉或修改成baseURL: process.env.NODE_ENV === 'production' ? '自己的域名或者服务器IP' : 'http://localhost:7529',

![image-20240831150723325](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831150723325.png)



config/config.ts，在末尾加上  exportStatic: {}  ，别人说是如果不配置，可能访问某页面时会出现404

![image-20240831151054100](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831151054100.png)



接下来打开前端项目的目录，然后在目录这个红线处输入cmd ,然后执行npm run build命令![image-20240831151217164](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831151217164.png)

这时打开宝塔

![image-20240831151545958](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831151545958.png)

![image-20240831151616926](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831151616926.png)

在这个输入框写入你的域名，或者服务器IP，接着按确定，再点开你那个站点的根目录，例如下图，圈出来的根目录

![image-20240831151749663](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831151749663.png)

点开之后先把全部文件删除

再上传dist内的全部文件（**注意不是上传dist文件夹，是它里面的全部文件**）

![image-20240831151951219](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831151951219.png)

上传完毕后，如下图

![image-20240831151848388](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831151848388.png)

这时，前端项目已经全部部署完





后端项目

流程的话：先配置好环境（比如这个项目的mysql,nacos，其它项目可能还有redis,mq之类的），接着也是把Java的有端口的多个模块打成jar包，然后上传到宝塔，再把前后端联调一下就可以了，哪里有报错就去查看对应的项目日志

配置环境：先在宝塔面板下载docker，这样比在命令行的时候少出错，点docker，右侧有个让你下载docker的东西，去下载

![image-20240831154138333](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831154138333.png)

下载完，打开设置，先确保docker是开启的，然后打开配置文件

![image-20240831154326654](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831154326654.png)

输入以下这些，这些是配置docker加速下载东西的，有可能会失效，失效的话，docker拉取镜像拉不下来，就要自己重新网上找了。

{

"registry-mirrors" : [
"https://docker.m.daocloud.io",
"https://hub-mirror.c.163.com",
"https://registry.docker-cn.com",
"http://hub-mirror.c.163.com",
"https://docker.mirrors.ustc.edu.cn",
"https://docker.rainbond.cc"

]

}

弄完后，[‍⁠⁠‌‬‬‌‍⁠﻿‍﻿‌‌⁠⁠‍‍﻿‌﻿‌‬‬‌day03-微服务01 - 飞书云文档 (feishu.cn)](https://b11et3un53m.feishu.cn/wiki/R4Sdwvo8Si4kilkSKfscgQX0niB)，这是黑马的文档，跟着圈起来的这两节教程下载MySQL和nacos就好了，其它不用看

![image-20240831154640678](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831154640678.png)

![image-20240831154722361](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831154722361.png)



这时，环境就下载好了，你先回到idea，确定一下各个模块的application文件是否有使用服务器的MySQL和nacos。

最好像我一样，区分出各个环境，这样方便去测试

![image-20240831155019285](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831155019285.png)

你确定好环境之后，不要着急把各个项目打jar包，先联调前后端项目是否还能正常运行（这次后端的mysql和nacos都用的服务器的地址了，不是本机了）。

正常运行没问题后，把api-back,api-interface,api-gateway三个项目package打成jar包

![image-20240831155452083](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831155452083.png)

打完后，去文件那里找个地方把三个jar包上传上去

![image-20240831155555225](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831155555225.png)

![image-20240831155619112](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831155619112.png)

上传后，回到下图

![image-20240831155657468](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831155657468.png)

主要是选择刚才项目的jar路径和端口号，然后放行端口

![image-20240831155745495](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831155745495.png)

上传项目最好用api-interface,api-back,api-gateway的顺序，免得dubbo那边又报什么奇奇怪怪的错误



以下是我的实例，各位可以参考，现在主要选了jar的路径和端口，命令都给你生成好了，超级方便

![image-20240831155925924](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831155925924.png)

![image-20240831160040696](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831160040696.png)

![image-20240831160100107](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831160100107.png)

最后就是配置后端项目域名访问了，配置好你的后端项目域名

![image-20240831160310257](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831160310257.png)

然后点开刚配置的这个站点的设置，在配置文件这里加上，如下图，解决跨域问题用的，如果后端项目有跨域类了，可以像我一样注释掉那些不需要的，如果后端项目没有跨域类，把注释打开。

![image-20240831160418186](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20240831160418186.png)



到此，部署完成，有其余问题需要自己去查看日志解决了