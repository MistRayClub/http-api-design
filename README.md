# 对外API接口设计

## 三原则
### 安全性
+ 创建appId,appKey和appSecret  ✔️ 
+ Token令牌：过期失效 ✔️
+ 客户端IP白名单（可选）
+ 单个接口针对IP限流（令牌桶限流，漏桶限流，计数器限流）
+ 记录接口请求日志
+ 采用https
+ 数据合法性检验
+ 密码查询（加缓存）
+ 接口调用失败告警
+ 高可用：服务器集群部署

### 幂等性
### 数据规范
+ 版本控制
+ 响应状态码规范
+ 统一响应数据格式

