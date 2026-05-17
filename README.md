# 任务管理系统 (Task Management System)

一个基于 **Spring Boot 3 + Vue 3 + Element Plus** 构建的内部任务管理平台，支持导师与实习生之间的任务分配、跟踪与协作。

---

## 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 3.2.5 |
| ORM | MyBatis-Plus | 3.5.7 |
| 数据库 | MySQL | 8.0 |
| 认证 | JWT (jjwt) | 0.11.5 |
| 工具库 | Hutool | 5.8.26 |
| 前端框架 | Vue 3 (Composition API) | 3.x |
| 构建工具 | Vite | 5.x |
| UI 框架 | Element Plus | 2.x |
| 状态管理 | Pinia | 3.x |
| HTTP 客户端 | Axios | 1.x |

---

## 功能特性

### 核心功能
- **用户认证** — JWT 登录/注册，24小时 Token 有效期
- **任务 CRUD** — 创建、查看、编辑、删除任务
- **状态流转** — 待办 → 进行中 → 已完成，一键切换
- **优先级管理** — 高/中/低三级，颜色区分
- **截止日期** — 日期选择 + 过期红色提醒
- **状态筛选** — 按待办/进行中/已完成过滤

### 角色权限
| 角色 | 权限 |
|------|------|
| **导师 (TUTOR)** | 查看所有任务、所有成员；可为任意成员创建/编辑任务；成员管理页面 |
| **实习生 (INTERN)** | 仅查看自己的任务；创建任务自动绑定自身 |

### 数据仪表盘
- 待办 / 进行中 / 已完成 / 全部 统计卡片
- 任务完成率圆环图
- 优先级分布条形图

### 资讯模块
- HackerNews 实时技术资讯（免费公开 API）
- 搜索 + 刷新功能
- 卡片式网格布局 + 随机封面图

---

## 项目结构

```
manage_web/
├── backend/                         # Spring Boot 后端
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/icinfo/task/
│       │   ├── TaskApplication.java          # 启动类
│       │   ├── config/                       # 配置 (CORS, JWT, MyBatis-Plus)
│       │   ├── controller/                   # 控制器
│       │   │   ├── AuthController.java       # 登录/注册
│       │   │   ├── TaskController.java       # 任务 CRUD
│       │   │   ├── UserController.java       # 用户列表
│       │   │   └── NewsController.java       # 资讯聚合
│       │   ├── service/                      # 业务逻辑层
│       │   ├── mapper/                       # MyBatis Mapper
│       │   ├── entity/                       # 实体类 (User, Task)
│       │   ├── dto/                          # 数据传输对象
│       │   └── common/                       # 工具类 (JWT, 全局异常)
│       └── resources/
│           ├── application.yml               # 主配置 (mysql/dev 双 profile)
│           ├── db/                            # H2 初始化脚本
│           └── mapper/                        # MyBatis XML
├── frontend/                        # Vue 3 前端
│   └── src/
│       ├── api/                              # Axios 封装 (auth, task, user, news)
│       ├── views/                            # 页面
│       │   ├── Login.vue                     # 登录注册
│       │   ├── TaskList.vue                  # 任务管理
│       │   ├── Dashboard.vue                 # 数据仪表盘
│       │   ├── Members.vue                   # 成员管理 (仅导师)
│       │   ├── News.vue                      # 资讯浏览
│       │   └── About.vue                     # 关于我们
│       ├── components/                       # 组件 (TaskFormDialog, TaskCard...)
│       ├── router/                           # 路由 + 权限守卫
│       └── store/                            # Pinia 状态管理
├── database/                        # MySQL 建库建表 + 初始数据
└── docs/                            # 设计文档
```

---

## 快速开始

### 环境要求

| 工具 | 最低版本 |
|------|----------|
| JDK | 17+ |
| Maven | 3.6+ |
| MySQL | 8.0+ |
| Node.js | 16+ |
| npm | 8+ |

### 1. 数据库初始化

确保 MySQL 服务运行中，使用 `root` 用户执行：

```bash
# Windows
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root < database\schema.sql
```

默认用户（密码统一为 `123456`，已 MD5 加密存储）：

| 用户名 | 密码 | 角色 |
|--------|------|------|
| teacher | 123456 | 导师 |
| stu1 | 123456 | 实习生 |
| stu2 | 123456 | 实习生 |

### 2. 启动后端

```bash
cd backend
mvn clean package -DskipTests
mvn spring-boot:run
```

后端启动于 **http://localhost:8081**

> 如需使用 H2 内存数据库快速验证，修改 `application.yml` 中 `spring.profiles.active` 为 `dev`

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端启动于 **http://localhost:5173**，API 请求自动代理至后端。

---

## API 接口

### 认证

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/auth/login` | 登录 | 否 |
| POST | `/api/auth/register` | 注册 | 否 |

### 任务

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/tasks?status=&assigneeId=` | 获取任务列表 | 是 |
| POST | `/api/tasks` | 创建任务 | 是 |
| PUT | `/api/tasks/{id}` | 更新任务 | 是 |
| DELETE | `/api/tasks/{id}` | 删除任务 | 是 |

### 用户

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/users` | 获取所有用户 | 是 |

### 资讯

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/news?q=` | 获取资讯列表 | 否 |

### 统一返回格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

---

## 页面导航

```
┌─────────────────────────────────────────────────────────┐
│  任务管理系统   任务管理 | 仪表盘 | 成员管理 | 资讯 | 关于  │
└─────────────────────────────────────────────────────────┘
```

- **任务管理** — 任务表格、状态切换、新建/编辑弹窗、筛选
- **数据仪表盘** — 统计卡片 + 完成率 + 优先级分布
- **成员管理** — 仅导师可见，查看所有成员及其任务
- **资讯** — 卡片流展示、搜索、刷新
- **关于我们** — 技术栈介绍、简历下载

---

## 配置说明

### 后端配置 (`application.yml`)

```yaml
spring:
  profiles:
    active: mysql          # mysql 或 dev (H2)

# MySQL 配置
spring.datasource:
  url: jdbc:mysql://localhost:3306/task_db
  username: root
  password:                # 留空使用 Windows 认证

# JWT 配置
jwt:
  secret: "your-256-bit-secret-key..."
  expire: 86400000         # 24小时
```

### 前端代理 (`vite.config.ts`)

```ts
proxy: {
  '/api': {
    target: 'http://localhost:8081',
    changeOrigin: true,
  }
}
```

---

## 开发者

- **Author**: wanan1018
- **GitHub**: https://github.com/wanan1018/manage_web
- **Date**: 2026-05-17

---

## 许可证

仅用于学习与面试演示。
