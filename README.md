# DormLink

🔥 A dormitory management system based on Spring Boot, Vue3, Element Plus, and MyBatis-Plus.  
🚀 Built for multi-role campus workflows including rooms, repairs, transfers, visitors, and announcements.  
⭐ Supports admin, dorm manager, and student operations with statistics and visualization modules.

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

建议先复制环境变量模板：

```bash
cp .env.example .env
```

然后运行环境检查（会校验关键变量和占位密码）：

```bash
./scripts/dev.sh check-env
```

后端读取以下配置（见 [application.properties](Dormitory_business/src/main/resources/application.properties)）：

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

默认端口：`9090`

启动命令：

```bash
./scripts/dev.sh backend
```

### 5.3 前端启动

```bash
./scripts/dev.sh frontend
```

### 5.4 推荐启动顺序

```bash
# 1) 启动 MySQL/Redis
./scripts/dev.sh infra-up

# 2) 启动后端
./scripts/dev.sh backend

# 3) 启动前端
./scripts/dev.sh frontend
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

## 10. 设计与实现思路

`DormLink` 采用“角色驱动 + 场景驱动”的方式规划实现顺序：

1. 先划分管理员、宿管、学生三类角色的登录入口与操作边界；
2. 再完成宿舍楼、房间、学生、宿管等基础数据模块；
3. 最后补报修、调宿、访客、公告等更贴近实际管理场景的流程型业务；
4. 前端用 Vue 3 承接页面交互，后端提供对应接口，确保业务流程可以完整串联。

这样做的好处是，可以先保证宿舍核心台账和基础资料可用，再逐步把动态流程补齐。

## 11. 关键难点与优化方向

### 11.1 关键难点

项目的主要复杂度来自多角色与多流程并存：

- 管理员、宿管、学生对应的页面和接口权限不同；
- 报修和调宿属于典型的状态流转业务，不只是简单增删改查；
- 宿舍、房间、学生分配之间存在明显的数据关联与约束关系。

### 11.2 当前处理方式

当前实现主要通过以下方式控制复杂度：

- 在接口层按角色拆分入口，让权限边界更清楚；
- 先梳理宿舍楼、房间、学生、宿管的数据模型，再叠加流程型业务；
- 对报修、调宿、访客等模块按业务动作拆分接口，避免单接口承担过多逻辑；
- 前后端围绕统一的数据对象设计，减少联调阶段字段不一致的问题。

### 11.3 后续优化方向

后续可以优先从以下几个方向继续完善：

- 补数据库初始化脚本和测试数据；
- 引入更明确的 token / JWT 鉴权，而不是以当前 session 方案为主；
- 给调宿、报修增加更完整的审批状态和操作日志；
- 在房间分配和床位使用上增加冲突校验与可视化统计。

## 12.1 贡献建议

欢迎通过 Issue / PR 提交：

- SQL 初始化脚本与测试数据
- 前端页面优化与交互增强
- 业务规则完善（床位分配、审批流）
- 部署文档与运维脚本补充

## 12.2 许可说明

本仓库采用 MIT License，详见 [LICENSE](LICENSE)。

## 简历改造清单

- 追踪文件：[docs/resume-upgrade-checklist.md](docs/resume-upgrade-checklist.md)
- 环境模板：[.env.example](.env.example)
- 开发 compose：[docker-compose.dev.yml](docker-compose.dev.yml)
- CI 配置：[.github/workflows/ci.yml](.github/workflows/ci.yml)

本轮已落地：调宿申请幂等保护 + 默认状态/时间补全 + 对应单元测试。
