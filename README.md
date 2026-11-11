# 苍穹外卖（Sky Take Out）

基于黑马程序员《苍穹外卖》课程实现的外卖系统，包含**管理端 Web**、**用户端微信小程序**和**后端服务**，覆盖从菜品管理、用户下单、订单流转到营业数据统计的完整链路。

## 功能概览

**管理端**
- 员工、分类、菜品、套餐管理
- 订单管理：接单、拒单、派送、完成、取消、搜索、各状态数量统计
- 店铺营业状态设置
- 数据统计：营业额、用户、订单统计，销量 Top10
- 工作台：今日营业数据、订单/菜品/套餐总览
- 运营数据报表导出（Excel）

**用户端小程序**
- 微信登录（默认 mock，可切换真实接口）
- 菜品/套餐分类浏览、菜品详情
- 购物车增删改、清空
- 下单、支付（默认 mock）、订单历史/详情/取消/催单/再来一单
- 收货地址簿
- 订单超时自动处理、WebSocket 来单语音提醒

## 技术栈

| 层 | 技术 |
| --- | --- |
| 后端 | Spring Boot 2.7.3、MyBatis、PageHelper、MySQL 8、Redis、JWT、WebSocket、Knife4j、Apache POI |
| 管理端 | Vue 2 + TypeScript + Element UI（使用课程提供的构建产物） |
| 小程序 | uni-app（Vue 2） |
| 构建 | Maven、JDK 8+（实测 JDK 17） |

## 模块结构

```
sky-take-out
├── sky-common       # 常量、工具类、异常、JWT、阿里云 OSS 配置
├── sky-pojo         # DTO / VO / Entity
├── sky-server       # 启动类、Controller、Service、Mapper、拦截器、WebSocket、定时任务
├── sky-mini-program # 用户端 uni-app 小程序源码
├── sql              # 本地演示造数脚本
└── nginx            # 管理端静态服务（不入库，见下文）
```

## 快速开始

### 1. 准备数据库

```sql
CREATE DATABASE sky_take_out DEFAULT CHARACTER SET utf8mb4;
```

导入课程资料中的建表 SQL（`sky_take_out.sql`）。如需报表/工作台演示数据，可再执行：

```bash
mysql -uroot -p sky_take_out < sql/demo_data.sql
```

### 2. 启动 Redis

默认连接 `localhost:6379`，无密码。

### 3. 配置本地私有参数

复制示例文件并填写真实值：

```bash
cp sky-server/src/main/resources/application-local.yml.example \
   sky-server/src/main/resources/application-local.yml
```

`application-local.yml` 已被 `.gitignore` 忽略，不会提交。至少需要填写数据库密码；如果需要图片上传，再填写阿里云 OSS 密钥。

也可以不用本地文件，直接通过环境变量注入，见下方“环境变量”。

### 4. 启动后端

```bash
mvn -DskipTests install
java -jar sky-server/target/sky-server-1.0-SNAPSHOT.jar
```

或在 IDEA 中运行 `com.sky.SkyApplication`。

- 服务地址：http://localhost:8080
- 接口文档：http://localhost:8080/doc.html

### 5. 启动管理端

管理端使用课程资料中提供的 **nginx + 已构建 dist**（仓库未包含 nginx 可执行文件与 dist，避免把二进制产物提交到仓库）：

1. 将 nginx 复制到项目根目录的 `nginx/`（例如从课程资料或本地 `D:\javaApp\nginx-1.20.2` 复制）；
2. 确保静态文件位于 `nginx/html/sky/`（即管理端 dist）；
3. 确认 `nginx/conf/nginx.conf` 中：
   - `root html/sky;`
   - `location /api/ { proxy_pass http://localhost:8080/admin/; }`
   - `location /ws/ { proxy_pass http://localhost:8080/ws/; ... }`
4. 双击 `nginx/start.bat` 启动，双击 `nginx/stop.bat` 停止（若没有这两个脚本，直接执行 `nginx.exe` / `nginx.exe -s stop`）；
5. 浏览器访问 http://localhost/ （80 端口被占用时修改 `listen` 为 90 等空闲端口）。

### 6. 运行小程序

1. 用 HBuilderX 导入 `sky-mini-program/`；
2. 确认 `sky-mini-program/utils/env.js` 中的 `baseUrl` 指向后端，例如 `http://localhost:8080`；
3. 运行到微信开发者工具，勾选“不校验合法域名”。

## 默认账号

管理端员工账号（密码为 MD5 存储）：

| 用户名 | 密码 | 说明 |
| --- | --- | --- |
| `admin` | `123456` | 管理员 |
| `guigui` | `123456` | 演示员工 |
| `xiaopan` | `123456` | 演示员工 |

## Mock 说明

- **微信登录**：`sky.wechat.mock-enabled: true` 时，任意 `code` 都映射为固定 `openid`（`mock_openid_001`），首次登录自动注册，无需真实 appid/secret。
- **支付**：`payment()` 为 mock 实现，请求成功即视为支付成功；后端直接把订单置为已支付/待接单，并通过 WebSocket 推送来单提醒。取消/拒单退款只修改状态，不调用微信退款接口。

## 环境变量

| 变量 | 说明 | 默认值 |
| --- | --- | --- |
| `SKY_DB_HOST` / `SKY_DB_PORT` | MySQL 地址/端口 | `localhost` / `3306` |
| `SKY_DB_NAME` | 数据库名 | `sky_take_out` |
| `SKY_DB_USERNAME` / `SKY_DB_PASSWORD` | 数据库账号/密码 | `root` / 空 |
| `SKY_REDIS_HOST` / `SKY_REDIS_PORT` | Redis 地址/端口 | `localhost` / `6379` |
| `SKY_REDIS_PASSWORD` / `SKY_REDIS_DB` | Redis 密码/库 | 空 / `0` |
| `SKY_JWT_ADMIN_SECRET` / `SKY_JWT_USER_SECRET` | 管理端/用户端 JWT 密钥 | 占位值，生产必须修改 |
| `SKY_ALIOSS_ENDPOINT` | OSS Endpoint | `oss-cn-beijing.aliyuncs.com` |
| `SKY_ALIOSS_ACCESS_KEY_ID` / `SKY_ALIOSS_ACCESS_KEY_SECRET` | OSS AccessKey | 占位值 |
| `SKY_ALIOSS_BUCKET_NAME` | OSS Bucket | 占位值 |
| `SKY_WECHAT_APPID` / `SKY_WECHAT_SECRET` | 微信小程序 appid/secret | 占位值 |

## 上线前必改

1. 修改 `SKY_JWT_ADMIN_SECRET`、`SKY_JWT_USER_SECRET`；
2. 使用独立的数据库/Redis 账号密码，不要沿用本地默认值；
3. 填写真实的 OSS、微信小程序 appid/secret；
4. 将 `sky.wechat.mock-enabled` 改为 `false`，接入真实微信登录；
5. 将订单支付/退款从 mock 切换为真实微信支付；
6. 将 `sky.jwt.user-ttl` 从 30 天（`2592000000`）改回 2 小时（`7200000`）；
7. 为管理端配置 HTTPS 与访问控制。

## 说明

- 仓库不包含 `target/`、`nginx/`、小程序 `unpackage/` 等编译产物与二进制文件。
- 本地私有配置 `application-local.yml` 已被忽略，请勿把真实密钥提交到仓库。
