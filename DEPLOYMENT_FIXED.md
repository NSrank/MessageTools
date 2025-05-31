# MessageTools 插件部署指南 - 修复版本

## 🔧 问题修复

已修复的问题：
- ✅ NullPointerException - 添加了缺失的 `main` 字段到 velocity-plugin.json
- ✅ 事件监听 - 使用正确的 PostLoginEvent 替代 ServerConnectedEvent
- ✅ 调试日志 - 添加了详细的调试信息

## 📦 部署步骤

### 1. 获取插件文件
使用最新编译的插件文件：
```
target/MessageTools-1.0-SNAPSHOT.jar
```

### 2. 安装到Velocity服务器
1. 将JAR文件复制到Velocity服务器的 `plugins` 文件夹
2. 重启Velocity服务器

### 3. 验证安装成功
启动后应该看到以下日志：
```
[INFO] MessageTools 插件正在启动...
[INFO] 配置文件加载成功
[INFO] 事件监听器已注册
[INFO] MessageTools 插件启动成功！
[INFO] 当前配置 - 加入消息启用: true
[INFO] 当前配置 - 私人消息启用: true
[INFO] 当前配置 - 控制台输出启用: true
```

### 4. 配置插件
1. 插件会自动在 `plugins/MessageTools/` 目录下生成 `config.yml`
2. 可以使用项目中的 `test-config.yml` 作为测试配置
3. 修改配置后使用 `/velocity reload` 重新加载

## 🧪 测试功能

### 基础测试
1. 玩家通过Velocity代理加入服务器
2. 应该看到以下日志：
   ```
   [INFO] 检测到玩家登录事件: PlayerName
   [INFO] 处理玩家加入事件: PlayerName (第一次: true)
   [INFO] 设置消息发送延迟: 1000ms
   [INFO] 开始发送消息给玩家: PlayerName
   [INFO] 发送首次加入消息
   ```

3. 玩家应该收到：
   - 全服聊天中的加入消息
   - 私人欢迎消息
   - 控制台应该输出玩家加入信息

### 调试模式
如果功能不正常，启用调试模式：

1. 在 `velocity.toml` 中设置：
   ```toml
   [advanced]
   log-level = "DEBUG"
   ```

2. 重启服务器，查看详细日志

## 📋 配置示例

### 最简测试配置
```yaml
messages:
  join:
    enabled: true
    messages:
      - "TEST: %player_name% joined"
  
  message:
    enabled: true
    messages:
      - "Welcome %player_name%!"

console:
  enabled: true
  format:
    join: "[TEST] %player_name% joined"

delays:
  between_messages: 200
  after_join: 500
```

### 完整配置
参考项目中的 `config.yml` 文件。

## 🔍 故障排除

### 问题1：插件无法加载
**错误**: `NullPointerException` 或 `Unable to load plugin`
**解决**: 确保使用最新编译的JAR文件，包含正确的 velocity-plugin.json

### 问题2：没有消息输出
**检查**:
1. 确认通过Velocity代理加入，不是直连后端服务器
2. 检查配置文件中 `enabled: true`
3. 查看调试日志确认事件是否触发

### 问题3：首次加入检测不准确
**解决**: 删除 `plugins/MessageTools/playerdata.yml` 重置数据

## 📁 文件结构

部署后的文件结构：
```
velocity-server/
├── plugins/
│   └── MessageTools-1.0-SNAPSHOT.jar
└── plugins/MessageTools/
    ├── config.yml          # 自动生成的配置文件
    └── playerdata.yml      # 玩家数据文件
```

## 🎯 预期行为

### 玩家首次加入
1. 控制台输出：`[MessageTools] 新玩家 PlayerName 第一次加入服务器`
2. 全服消息：`★ PlayerName 第一次加入了服务器！`
3. 私人消息：`欢迎来到服务器，PlayerName！`

### 玩家再次加入
1. 控制台输出：`[MessageTools] 玩家 PlayerName 加入了服务器`
2. 全服消息：`[+] PlayerName 加入了服务器`
3. 私人消息：`欢迎来到服务器，PlayerName！`

### 玩家退出
1. 控制台输出：`[MessageTools] 玩家 PlayerName 离开了服务器`
2. 全服消息：`[-] PlayerName 离开了服务器`

## 🚀 性能说明

- 插件使用异步任务处理消息发送，不会阻塞主线程
- 玩家数据自动保存，支持服务器重启
- 内存占用极小，适合各种规模的服务器

## 📞 技术支持

如果遇到问题：
1. 查看 `TROUBLESHOOTING.md` 详细故障排除指南
2. 启用调试日志获取详细信息
3. 检查Velocity和Java版本兼容性

---

**重要提醒**: 确保使用最新编译的JAR文件，旧版本可能存在velocity-plugin.json格式问题。
