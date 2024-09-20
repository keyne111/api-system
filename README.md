#  API

> 一个丰富的API开放调用系统，为开发者提供便捷、实用、安全的API调用体验； Java + React 全栈项目，包括网站前台+后台
>
>
> 在线体验地址：[http://api.keyne.love/]
>
> 账号：xiaofan  密码：12345678
>
> 目前只实现一个模拟接口供使用，请求参数：{"userName":"adsad"}





## 项目展示


- 登录页
![image](https://github.com/user-attachments/assets/283e0603-f801-4d3e-a034-927ec3fd423d)




- 首页
![image](https://github.com/user-attachments/assets/1d1b705e-d5f8-41c1-bb91-5411849e71a4)




- 接口详情
![image](https://github.com/user-attachments/assets/8571efce-b825-4aae-85b2-91b23fb8ace4)

 



- 接口管理
![image](https://github.com/user-attachments/assets/0e6dbe72-9ebf-4a8c-a9cc-a6711c17e4f8)



- 接口分析
![image](https://github.com/user-attachments/assets/7dbe86d2-794a-41bd-949a-7c7b0d61833f)







## 系统架构

![image](https://github.com/user-attachments/assets/65fd4955-2008-448a-be9d-7ee1c2b216ae)






## 技术栈

### 前端技术栈

- 开发框架：React、Umi
- 脚手架：Ant Design Pro
- 组件库：Ant Design、Ant Design Components
- 语法扩展：TypeScript、Less
- 打包工具：Webpack
- 代码规范：ESLint、StyleLint、Prettier



### 后端技术栈

- 主语言：Java
- 框架：SpringBoot 2.7.12、Mybatis-plus、Spring Cloud
- 数据库：Mysql8.0、Redis
- 注册中心：Nacos
- 配置中心：Nacos
- 服务调用：Dubbo、OpenFeign
- 网关：Spring Cloud Gateway
- 负载均衡：Spring cloud Loadbalancer



## 项目模块

- api-frontend ：项目前端代码
- api-back：项目后端的单体架构
- api-back-microservice：项目后端的微服务架构
- api-sdk：提供给开发者的SDK
- api-common ：公共模块（公共常量，统一响应实体，统一异常处理）
- api-gateway ：网关服务，**实现路由转发、身份校验、统一跨域、统一业务处理、流量染色、聚合文档**
- api-interface：模拟接口服务，提供可供调用的接口
- feign-common：feign模块，提供公共接口让其它模块调用
- interfaceInfo-service：接口服务
- user-service：用户服务



## 功能模块

- 用户、管理员
  - 用户：登录注册
  - 管理员：用户管理
  - 管理员：接口管理
  - 管理员：接口分析



## 扩展点：

​	1：单体架构拆分为微服务，nacos作为配置中心，openfeign在服务间调用

​	2：管理员接口分析拆用定时任务每天扫描一次，以后需要接口分析中有缓存就走缓存，无缓存就走数据库，得到数据后也会存入缓存。优化了接口时间




## 欢迎贡献

项目需要大家的支持，期待更多小伙伴的贡献，你可以：

- 对于项目中的Bug和建议，能够在Issues区提出建议，我会积极响应





