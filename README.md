# 项目初始化与路径规划文档

## 一、项目总目录结构规划
建议在本地创建统一父文件夹 `manage_web` 作为项目根目录，统一管理后端、前端、数据库脚本、设计文档等资源，便于项目维护与协作。

**完整目录结构**：
```
manage_web/
├── backend/                 # Spring Boot 后端项目
├── frontend/                # Vue3 前端项目
├── database/                # 数据库脚本
│   ├── schema.sql           # 建表语句
│   └── data.sql             # 初始数据（如默认用户）
├── docs/                    # 设计文档及相关截图
│   ├── design.md            # 最终提交的设计文档
│   └── screenshots/         # AI使用心得、问题解决截图
└── README.md                # 整体说明（如何启动前后端）
```

## 二、目录说明
| 目录/文件 | 用途 |
| ---- | ---- |
| `backend/` | Spring Boot 后端工程，提供接口服务、业务逻辑、数据交互 |
| `frontend/` | Vue3 + Vite 前端工程，实现页面渲染、交互、接口请求 |
| `database/` | 存放数据库建表语句与初始化数据，保证环境一致性 |
| `docs/` | 项目设计文档、功能说明、问题记录与截图归档 |
| `README.md` | 项目快速启动手册，包含前后端环境准备、启动命令 |

## 三、前端项目初始化（Vue3 + Vite + Element Plus）
### 3.1 环境要求
确保本地已安装：
- Node.js 16+ 版本
- npm / yarn 包管理工具

### 3.2 创建 Vite 项目
进入根目录 `manage_web`，执行以下命令创建前端项目：
```bash
# 进入 frontend 目录
cd frontend

# 使用 Vite 创建 Vue3 项目
npm create vite@latest . -- --template vue
```

### 3.3 前端标准目录结构规划
```
frontend/
├── index.html              # 项目入口 HTML
├── vite.config.js          # Vite 配置文件（代理、端口等）
├── package.json            # 依赖管理与脚本配置
├── src/
│   ├── main.js             # 项目入口，全局注册组件/插件
│   ├── App.vue             # 根组件
│   ├── router/
│   │   └── index.js        # 路由配置（登录、任务列表、仪表盘）
│   ├── store/
│   │   └── user.js         # Pinia 状态管理（用户信息、Token）
│   ├── api/                # 接口请求封装
│   │   ├── request.js      # Axios 实例与请求拦截器
│   │   ├── auth.js         # 登录/注册相关接口
│   │   ├── task.js         # 任务管理相关接口
│   │   └── news.js         # 新闻/通知相关接口
│   ├── views/              # 页面级组件
│   │   ├── Login.vue       # 登录页面
│   │   ├── TaskList.vue    # 任务列表页面
│   │   └── Dashboard.vue   # 数据仪表盘页面
│   ├── components/         # 公共可复用组件
│   │   ├── TaskCard.vue
│   │   ├── TaskFormDialog.vue
│   │   ├── NewsSidebar.vue
│   │   └── StatsChart.vue
│   ├── utils/              # 工具函数
│   │   └── formatDate.js   # 日期格式化工具
│   └── assets/             # 静态资源（图片、样式）
└── .env.development        # 开发环境变量配置
```

### 3.4 关键配置文件
#### 3.4.1 vite.config.js（跨域代理配置）
用于解决前端请求后端接口的跨域问题，将 `/api` 请求转发至后端服务。

```javascript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    // 前端服务端口
    port: 5173,
    // 跨域代理配置
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端接口地址
        changeOrigin: true,             // 开启跨域
      }
    }
  }
})
```
### 3.5 初始化命令（在 frontend/ 目录下运行）
```bash
# 安装依赖
npm install
# 启动
npm run dev
```

## 四、后端项目初始化（Spring Boot 3.x + MyBatis-Plus + MySQL）

### 4.1 使用 Spring Initializr 创建项目
访问 [https://start.spring.io/](https://start.spring.io/)

选择以下配置：
- **Project**：Maven
- **Language**：Java
- **Spring Boot**：3.2.x（最新稳定版）
- **Group**：com.icinfo
- **Artifact**：task-backend
- **Package name**：com.icinfo.task
- **Packaging**：Jar
- **Java**：17+

**Dependencies 至少勾选**：
- Spring Web
- MySQL Driver
- Lombok（可选，强烈推荐）

> 说明：由于 MyBatis-Plus 不在官方 starter 列表中，生成项目后需手动在 pom.xml 添加。

### 4.2 手动调整 pom.xml（添加 MyBatis-Plus、JWT、工具库）
在 `<dependencies>` 标签中加入以下依赖：

```xml
<!-- MyBatis-Plus for Spring Boot 3.x -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
    <version>3.5.5</version>
</dependency>

<!-- JWT 依赖 -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>

<!-- Hutool 工具类库（可选，用于简化代码） -->
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>5.8.26</version>
</dependency>
```

### 4.3 后端包结构规划（重要）
```text
backend/src/main/java/com/icinfo/task/
├── TaskApplication.java                 # 项目启动类
├── config/                              # 配置类目录
│   ├── CorsConfig.java                  # 全局跨域配置
│   ├── JwtInterceptor.java              # JWT 认证拦截器
│   ├── WebConfig.java                   # Web 配置（注册拦截器）
│   └── MybatisPlusConfig.java           # MyBatis-Plus 配置（分页插件等）
├── controller/                          # 控制器层（接收请求）
│   ├── AuthController.java              # 认证相关接口（登录、注册）
│   ├── TaskController.java              # 任务管理相关接口
│   └── NewsController.java              # 资讯相关接口
├── service/                             # 业务逻辑层
│   ├── UserService.java
│   ├── TaskService.java
│   └── impl/                            # 业务逻辑实现类
│       ├── UserServiceImpl.java
│       └── TaskServiceImpl.java
├── mapper/                              # MyBatis-Plus Mapper 接口
│   ├── UserMapper.java
│   └── TaskMapper.java
├── entity/                              # 数据库实体类
│   ├── User.java
│   └── Task.java
├── dto/                                 # 数据传输对象（请求/响应）
│   ├── LoginRequest.java
│   ├── TaskDTO.java
│   └── Result.java                      # 统一返回结果
├── common/                              # 通用工具类
│   ├── StatusCode.java                  # 状态码常量
│   └── JwtUtil.java                     # JWT 生成与验证工具类
└── resources/
    ├── application.yml                  # 主配置文件
    └── mapper/                          # MyBatis XML 映射文件
        ├── UserMapper.xml
        └── TaskMapper.xml
```

### 4.4 application.yml 配置示例
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/task_db?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: 123456  # 修改为你的 MySQL 密码
    driver-class-name: com.mysql.cj.jdbc.Driver
  jackson:
    time-zone: Asia/Shanghai
    date-format: yyyy-MM-dd HH:mm:ss

mybatis-plus:
  mapper-locations: classpath*:mapper/**/*.xml
  global-config:
    db-config:
      id-type: auto  # 主键自增
      logic-delete-field: deleted  # 逻辑删除字段
      logic-delete-value: 1
      logic-not-delete-value: 0
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl  # 开发环境打印SQL日志
    map-underscore-to-camel-case: true  # 下划线转驼峰

jwt:
  secret: "your-256-bit-secret-key-please-change-in-production-123456"  # 至少32字符
  expire: 86400000  # Token 过期时间：24小时（毫秒）

# 资讯 API 配置（以 GNews 为例）
news:
  api-key: "your-gnews-api-key"
  base-url: "https://gnews.io/api/v4/search"

server:
  port: 8080  # 后端服务端口，与前端代理配置一致
```

### 4.5 初始化命令（在 backend/ 目录下运行）
```bash
# 清理并安装依赖
mvn clean install

# 启动 Spring Boot 应用
mvn spring-boot:run
```


## 五、数据库初始化

### 5.1 创建数据库（MySQL）
首先连接到你的MySQL服务器，执行以下命令创建数据库：

```sql
CREATE DATABASE IF NOT EXISTS task_db 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE task_db;
```

### 5.2 执行建表脚本（database/schema.sql）
将以下内容保存到项目根目录下的 `database/schema.sql` 文件中，然后在MySQL中执行：

```sql
-- 用户表
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(128) NOT NULL COMMENT '密码（加密存储）',
  `role` varchar(16) NOT NULL DEFAULT 'INTERN' COMMENT '角色：TUTOR(导师)/INTERN(实习生)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`) COMMENT '用户名唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 任务表
CREATE TABLE `task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `title` varchar(100) NOT NULL COMMENT '任务标题',
  `description` text COMMENT '任务描述',
  `status` varchar(20) NOT NULL DEFAULT 'TODO' COMMENT '任务状态：TODO/IN_PROGRESS/DONE',
  `priority` varchar(10) DEFAULT 'MEDIUM' COMMENT '优先级：LOW/MEDIUM/HIGH',
  `assignee_id` bigint NOT NULL COMMENT '负责人ID',
  `creator_id` bigint NOT NULL COMMENT '创建人ID',
  `due_date` date DEFAULT NULL COMMENT '截止日期',
  `related_news_url` varchar(512) DEFAULT NULL COMMENT '相关资讯链接',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_assignee` (`assignee_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_task_assignee` FOREIGN KEY (`assignee_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_task_creator` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';
```

### 5.3 插入初始测试数据（database/data.sql）
将以下内容保存到项目根目录下的 `database/data.sql` 文件中，然后在MySQL中执行：

```sql
-- 插入默认用户（密码均为123456，实际生产环境请使用加密密码）
INSERT INTO `user` (`username`, `password`, `role`) VALUES
('tutor1', '123456', 'TUTOR'),
('intern1', '123456', 'INTERN');

-- 插入测试任务
INSERT INTO `task` (`title`, `description`, `status`, `priority`, `assignee_id`, `creator_id`, `due_date`) VALUES
('学习Spring Boot基础', '完成Spring Boot官方入门教程，了解核心概念', 'TODO', 'HIGH', 2, 1, '2026-05-20'),
('设计数据库表结构', '根据需求文档设计完整的ER图并生成建表语句', 'IN_PROGRESS', 'MEDIUM', 2, 1, '2026-05-18'),
('编写前端登录页面', '使用Vue3和Element Plus实现登录界面', 'TODO', 'MEDIUM', 2, 1, '2026-05-22'),
('开发后端登录接口', '实现JWT认证的登录接口', 'TODO', 'HIGH', 2, 1, '2026-05-21');
```

### 5.4 执行说明
1. 可以使用MySQL命令行、Navicat、DBeaver等工具执行上述SQL脚本
2. 执行顺序：先执行`schema.sql`创建表结构，再执行`data.sql`插入初始数据
3. **重要提示**：实际生产环境中，密码必须使用BCrypt等加密方式存储，不能明文保存
4. 后续如果需要修改表结构，请创建新的SQL脚本并记录版本号，便于团队协作