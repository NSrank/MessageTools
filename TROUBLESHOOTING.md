# MessageTools 故障排除指南

## 问题：插件加载后没有消息和控制台输出

### 步骤1：检查插件是否正确加载

1. 查看Velocity控制台启动日志，确认看到以下信息：
   ```
   [INFO] MessageTools 插件正在启动...
   [INFO] 配置文件加载成功
   [INFO] 已加载 X 个玩家的数据
   [INFO] PAPIProxyBridge 已检测到/未检测到...
   [INFO] 事件监听器已注册
   [INFO] MessageTools 插件启动成功！
   [INFO] 当前配置 - 加入消息启用: true
   [INFO] 当前配置 - 私人消息启用: true
   [INFO] 当前配置 - 控制台输出启用: true
   ```

2. 如果没有看到这些信息，检查：
   - 插件文件是否正确放在 `plugins` 文件夹中
   - Velocity版本是否为3.4.0或更高
   - Java版本是否为17或更高

### 步骤2：检查事件是否被触发

当玩家加入服务器时，应该看到以下日志：
```
[INFO] 检测到玩家登录事件: PlayerName
[INFO] 玩家 PlayerName (第一次) 加入服务器
[INFO] 处理玩家加入事件: PlayerName (第一次: true)
[INFO] 设置消息发送延迟: 1000ms
```

如果没有看到这些日志：
1. 确认你是通过Velocity代理服务器加入的，而不是直接连接后端服务器
2. 检查Velocity配置中的服务器设置是否正确

### 步骤3：检查配置文件

1. 确认配置文件位置：`plugins/MessageTools/config.yml`
2. 检查配置文件内容是否正确：

```yaml
messages:
  join:
    enabled: true
    messages:
      - "&a[+] &f%player_name% &7加入了服务器"
```

3. 常见配置问题：
   - YAML语法错误（缩进不正确）
   - `enabled: false` 导致消息被禁用
   - `messages` 列表为空

### 步骤4：启用调试日志

在Velocity配置文件 `velocity.toml` 中设置：
```toml
[advanced]
log-level = "DEBUG"
```

重启服务器后，你应该看到更详细的调试信息。

### 步骤5：手动测试配置

创建一个最简单的测试配置：

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
  between_messages: 100
  after_join: 500
```

### 步骤6：检查权限和文件权限

确保：
1. Velocity进程有权限读写插件目录
2. 配置文件没有被其他程序锁定
3. 插件JAR文件没有损坏

### 步骤7：检查服务器架构

确认你的服务器架构：
```
玩家 -> Velocity代理 -> Paper/Spigot后端服务器
```

MessageTools只在Velocity代理层工作，不会在后端服务器上工作。

## 常见错误和解决方案

### 错误1：ClassNotFoundException
```
java.lang.ClassNotFoundException: net.william278.papiproxybridge.api.PlaceholderAPI
```
**解决方案**：这是正常的，如果没有安装PAPIProxyBridge插件会出现此错误。插件会自动禁用PlaceholderAPI功能并使用内置变量。

### 错误2：配置文件加载失败
```
[ERROR] 加载配置文件时发生错误
```
**解决方案**：
1. 删除 `plugins/MessageTools/config.yml`
2. 重启服务器让插件重新生成默认配置
3. 检查YAML语法是否正确

### 错误3：事件监听器注册失败
```
[ERROR] MessageTools 插件启动失败
```
**解决方案**：
1. 检查Velocity版本兼容性
2. 查看完整的错误堆栈信息
3. 确认没有其他插件冲突

### 错误4：消息发送失败
```
[ERROR] 处理玩家加入事件时发生错误
```
**解决方案**：
1. 检查消息内容是否包含无效字符
2. 确认Adventure API兼容性
3. 检查PlaceholderAPI变量是否有效

## 调试命令

### 检查插件状态
```
/velocity plugins
```
确认MessageTools插件显示为已加载状态。

### 重新加载配置
```
/velocity reload
```
重新加载所有插件配置。

### 查看日志
在服务器控制台中查看实时日志，或检查日志文件。

## 联系支持

如果以上步骤都无法解决问题，请提供以下信息：

1. **服务器信息**：
   - Velocity版本
   - Java版本
   - 操作系统

2. **配置文件**：
   - `velocity.toml` 相关部分
   - `plugins/MessageTools/config.yml` 完整内容

3. **日志信息**：
   - 插件启动日志
   - 玩家加入时的日志
   - 任何错误信息

4. **测试步骤**：
   - 详细描述你的测试过程
   - 期望的结果 vs 实际结果

## 临时解决方案

如果插件仍然无法正常工作，你可以：

1. **使用其他插件**：考虑使用其他类似功能的插件作为临时替代
2. **手动测试**：在控制台手动执行命令来测试基本功能
3. **简化配置**：使用最简单的配置来隔离问题

## 版本兼容性

| Velocity版本 | MessageTools兼容性 | 备注 |
|-------------|------------------|------|
| 3.4.0+ | ✅ 完全支持 | 推荐版本 |
| 3.3.x | ⚠️ 部分支持 | 可能有兼容性问题 |
| 3.2.x及以下 | ❌ 不支持 | 需要升级Velocity |

## 性能监控

如果插件工作正常但性能有问题：

1. **监控内存使用**：检查插件是否导致内存泄漏
2. **检查延迟设置**：适当调整消息发送延迟
3. **优化配置**：减少不必要的消息和功能
4. **监控日志大小**：避免过多的调试日志
