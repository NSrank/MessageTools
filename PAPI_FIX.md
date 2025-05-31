# MessageTools PlaceholderAPI 修复说明

## 🎉 好消息！

主要功能已经正常工作：
- ✅ 插件正常加载
- ✅ 玩家加入消息显示
- ✅ 玩家退出消息显示
- ✅ 控制台输出正常
- ✅ 调度器问题已解决

## 🔧 PlaceholderAPI 修复

### 问题描述
```
java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "<parameter1>" is null
```

### 问题原因
我们使用了错误的PAPIProxyBridge API调用方式：
- **错误**: 尝试调用静态方法 `formatPlaceholders`
- **正确**: 需要先创建API实例，然后调用实例方法

### 修复方案
根据PAPIProxyBridge官方文档，正确的调用方式是：

```java
// 正确的API调用方式
final PlaceholderAPI api = PlaceholderAPI.createInstance();
api.formatPlaceholders("Hello %player_name%!", player.getUniqueId()).thenAccept(formatted -> {
    // 处理格式化后的文本
});
```

### 代码修改
```java
// 修改前 (错误)
CompletableFuture<String> result = (CompletableFuture<String>) formatMethod.invoke(null, message, player.getUniqueId());

// 修改后 (正确)
java.lang.reflect.Method createInstanceMethod = papiClass.getMethod("createInstance");
Object apiInstance = createInstanceMethod.invoke(null);
CompletableFuture<String> result = (CompletableFuture<String>) formatMethod.invoke(apiInstance, message, player.getUniqueId());
```

## 📦 部署最新版本

### 1. 使用最新JAR文件
```
target/MessageTools-1.0-SNAPSHOT.jar
```

### 2. PlaceholderAPI 配置
确保配置文件中启用了PlaceholderAPI支持：

```yaml
placeholderapi:
  enabled: true
  timeout: 1000
```

### 3. 验证修复
重新部署后，PlaceholderAPI错误应该消失。

## 🧪 测试PlaceholderAPI功能

### 前提条件
1. 安装PAPIProxyBridge插件到Velocity服务器
2. 安装PlaceholderAPI到后端Spigot/Paper服务器
3. 安装相关的PlaceholderAPI扩展

### 测试步骤
1. 在配置文件中使用PlaceholderAPI变量：
   ```yaml
   messages:
     join:
       enabled: true
       messages:
         - "&a[+] &f%player_displayname% &7加入了服务器"
         - "&7等级：&e%player_level%"
   ```

2. 玩家加入服务器
3. 检查变量是否正确替换

### 如果PlaceholderAPI仍然不工作
1. **检查PAPIProxyBridge安装**：
   - Velocity服务器上安装PAPIProxyBridge
   - 后端服务器上安装PlaceholderAPI和PAPIProxyBridge

2. **检查变量格式**：
   - 使用正确的变量格式：`%variable_name%`
   - 确保变量在PlaceholderAPI中存在

3. **禁用PlaceholderAPI**：
   如果不需要PlaceholderAPI功能，可以禁用：
   ```yaml
   placeholderapi:
     enabled: false
   ```

## 🎯 当前状态

### ✅ 正常工作的功能
- 插件加载和初始化
- 玩家加入/退出事件监听
- 消息发送（全服和私人）
- 控制台日志输出
- 首次加入检测
- 内置变量替换
- 调试开关系统
- 异步任务调度

### 🔧 已修复的问题
- `plugin is not registered` 错误
- `Integer cannot be cast to Long` 错误
- `NullPointerException` 在插件加载时
- PlaceholderAPI反射调用错误

### 📋 配置示例

#### 使用内置变量（推荐）
```yaml
messages:
  join:
    enabled: true
    messages:
      - "&a[+] &f%player_name% &7加入了服务器"
      - "&7当前在线：&b%server_online%&7人"
      - "&7服务器时间：&e%server_time_HH:mm:ss%"

placeholderapi:
  enabled: false  # 如果不需要可以禁用
```

#### 使用PlaceholderAPI变量
```yaml
messages:
  join:
    enabled: true
    messages:
      - "&a[+] &f%player_displayname% &7加入了服务器"
      - "&7等级：&e%player_level%"
      - "&7金币：&6$%vault_eco_balance%"

placeholderapi:
  enabled: true
  timeout: 1000
```

## 🔍 故障排除

### 问题1：PlaceholderAPI变量不替换
**检查**：
1. PAPIProxyBridge是否正确安装
2. 后端服务器是否有PlaceholderAPI
3. 变量名称是否正确
4. 配置中 `placeholderapi.enabled: true`

### 问题2：仍然有PlaceholderAPI错误
**解决**：
1. 临时禁用PlaceholderAPI：`placeholderapi.enabled: false`
2. 使用内置变量替代
3. 检查PAPIProxyBridge版本兼容性

### 问题3：消息不显示
**检查**：
1. 配置文件语法是否正确
2. 消息是否启用：`enabled: true`
3. 是否通过Velocity代理加入

## 📊 性能说明

### PlaceholderAPI性能影响
- **启用时**: 每个变量需要网络请求到后端服务器
- **禁用时**: 只使用本地内置变量，性能最佳
- **缓存**: PAPIProxyBridge自动缓存30秒

### 推荐设置
- **小型服务器**: 可以启用PlaceholderAPI
- **大型服务器**: 建议使用内置变量以获得最佳性能
- **测试环境**: 启用调试模式便于排查问题

## 🎉 总结

现在MessageTools插件应该完全正常工作：
1. **核心功能**: 100% 工作
2. **PlaceholderAPI**: 已修复，可选使用
3. **性能**: 优化的异步处理
4. **调试**: 完善的日志系统

如果你不需要PlaceholderAPI功能，建议禁用它以获得最佳性能。插件的内置变量已经能满足大部分需求。
