# DormLink

> 面向宿舍管理场景的前后端分离系统，后端基于 Spring Boot + MyBatis-Plus，前端基于 Vue3 + Element Plus，支持管理员/宿管/学生三类角色。

## 1. 项目定位

`DormLink` 用于宿舍业务流程数字化管理，覆盖学生住宿、宿舍楼与房间、报修、调宿、访客、公告等核心场景。

## 2. 已实现功能

- 三角色登录：管理员、宿管、学生
- 学生管理、宿管管理、宿舍楼管理、房间管理
- 公告管理
- 报修申请与处理
- 调宿申请与处理
- 访客登记管理
- 首页统计与可视化组件（ECharts、天气、日历等）
- 头像与文件上传接口

## 3. 技术栈

- 后端：Spring Boot 2.6.3、MyBatis-Plus、MySQL
- 前端：Vue 3、Vue Router、Vuex、Element Plus、Axios、ECharts
- Java 版本：11

## 4. 项目结构

```text
DormLink
├── Dormitory_business/                 # 后端工程
│   ├── src/main/java/com/example/springboot
│   │   ├── controller/                 # admin/stu/dormManager/room/... 接口
│   │   ├── service/
│   │   ├── mapper/
│   │   ├── entity/
│   │   └── common/
│   └── src/main/resources/application.properties
├── vue/                                # 前端工程
│   ├── src/views/
│   ├── src/components/
│   ├── src/router/
│   └── package.json
└── doc/img/                            # 演示图片
```

## 5. 本地运行

### 5.1 环境准备

- JDK 11
- Maven 3.8+
- Node.js 16+
- MySQL 8.x

### 5.2 后端配置与启动

修改 [application.properties](/Users/liuzhuoran/Documents/Playground/readme-batch/DormLink/Dormitory_business/src/main/resources/application.properties)：

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

默认端口：`9090`

启动命令：

```bash
cd Dormitory_business
mvn spring-boot:run
```

### 5.3 前端启动

```bash
cd vue
npm install
npm run serve
```

## 6. 数据库说明

后端默认连接数据库：`wms`。

说明：仓库中未附带完整 `.sql` 初始化脚本，需根据后端实体与 Mapper 自行建库建表，或从历史环境导入结构数据。

## 7. 关键接口分组（示例）

- 登录：`/admin/login`、`/stu/login`、`/dormManager/login`
- 学生：`/stu/*`
- 宿管：`/dormManager/*`
- 房间与楼栋：`/room/*`、`/building/*`
- 报修与调宿：`/repair/*`、`/adjustRoom/*`
- 公告与访客：`/notice/*`、`/visitor/*`

## 8. 常见问题

- 前端请求失败：确认后端 `9090` 端口已启动，且跨域配置有效
- 登录无数据：先确认数据库已有角色用户数据
- 上传头像失败：检查后端文件目录权限与路径配置

## 9. 开发建议

- 补充数据库初始化脚本并纳入版本管理
- 增加接口鉴权 token 方案（当前以 session 为主）
- 增加单元测试与端到端测试

## 12.1 贡献建议

欢迎通过 Issue / PR 提交：

- SQL 初始化脚本与测试数据
- 前端页面优化与交互增强
- 业务规则完善（床位分配、审批流）
- 部署文档与运维脚本补充

## 12.2 许可说明

本仓库采用 MIT License，详见 [LICENSE](/Users/liuzhuoran/Documents/Playground/readme-batch/DormLink/LICENSE)。
