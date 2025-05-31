# MessageTools 插件最终部署指南

## 🔧 已修复的问题

### v1.1 修复内容
- ✅ **类型转换错误修复** - 修复了 `Integer cannot be cast to Long` 错误
- ✅ **调试日志优化** - 添加了调试开关，避免控制台日志过多
- ✅ **配置类型安全** - 改进了配置文件数字类型的处理
- ✅ **日志分级管理** - 通过配置控制详细日志输出

### v1.0 修复内容
- ✅ NullPointerException - 添加了缺失的 `main` 字段
- ✅ 事件监听 - 使用正确的 PostLoginEvent
- ✅ Maven构建 - 修复了velocity-plugin.json格式问题

## 📦 部署步骤

### 1. 获取最新插件文件
```
target/MessageTools-1.0-SNAPSHOT.jar
```

### 2. 安装到Velocity服务器
1. 将JAR文件复制到 `plugins` 文件夹
2. 重启Velocity服务器

### 3. 验证安装
启动后应该看到：
```
[INFO] MessageTools 插件正在启动...
[INFO] 配置文件加载成功
[INFO] 事件监听器已注册
[INFO] MessageTools 插件启动成功！
```

**不应该再看到**：
- `ClassCastException` 错误
- 过多的调试日志（除非启用调试模式）

## 🎛️ 调试配置

### 启用调试模式
在 `plugins/MessageTools/config.yml` 中：

```yaml
debug:
  enabled: true              # 启用基本调试信息
  verbose_events: true       # 启用详细事件日志
  verbose_config: true       # 启用配置加载日志
```

### 调试日志级别

#### 基本调试 (`debug.enabled: true`)
```
[INFO] 处理玩家加入事件: PlayerName (第一次: true)
```

#### 详细事件日志 (`verbose_events: true`)
```
[INFO] 设置消息发送延迟: 1000ms
[INFO] 开始发送消息给玩家: PlayerName
[INFO] 发送首次加入消息
[INFO] 普通加入消息启用状态: true
[INFO] 获取到 2 条普通加入消息
```

#### 详细配置日志 (`verbose_config: true`)
```
[INFO] 当前配置 - 加入消息启用: true
[INFO] 当前配置 - 私人消息启用: true
[INFO] 当前配置 - 控制台输出启用: true
```

### 生产环境配置
```yaml
debug:
  enabled: false             # 关闭调试信息
  verbose_events: false      # 关闭详细日志
  verbose_config: false      # 关闭配置日志
```

## 🧪 测试步骤

### 1. 使用测试配置
将 `test-config.yml` 重命名为 `config.yml`，该配置已启用调试模式。

### 2. 测试玩家加入
1. 玩家通过Velocity代理加入服务器
2. 应该看到调试日志（如果启用）
3. 玩家应该收到欢迎消息
4. 全服应该看到加入消息
5. 控制台应该输出加入信息

### 3. 验证功能
- [ ] 插件正常加载，无错误
- [ ] 玩家加入时显示消息
- [ ] 玩家退出时显示消息
- [ ] 首次加入检测正常
- [ ] 控制台输出正常
- [ ] 调试开关工作正常

## 📋 配置示例

### 生产环境配置
```yaml
messages:
  join:
    enabled: true
    messages:
      - "&a[+] &f%player_name% &7加入了服务器"
  
  quit:
    enabled: true
    messages:
      - "&c[-] &f%player_name% &7离开了服务器"
  
  message:
    enabled: true
    messages:
      - "&a欢迎来到服务器，%player_name%！"
      - "&7当前在线：&b%server_online%&7人"

console:
  enabled: true
  format:
    join: "[MessageTools] %player_name% 加入服务器"
    quit: "[MessageTools] %player_name% 离开服务器"

delays:
  between_messages: 500
  after_join: 1000

debug:
  enabled: false
  verbose_events: false
  verbose_config: false
```

### 调试环境配置
```yaml
# 基础消息配置（同上）
messages:
  join:
    enabled: true
    messages:
      - "TEST: %player_name% joined"

debug:
  enabled: true               # 启用调试
  verbose_events: true        # 详细事件日志
  verbose_config: true        # 详细配置日志
```

## 🔍 故障排除

### 问题1：类型转换错误
**错误**: `Integer cannot be cast to Long`
**状态**: ✅ 已修复
**说明**: 配置文件中的数字现在会自动转换为正确类型

### 问题2：日志过多
**问题**: 控制台输出太多调试信息
**解决**: 设置 `debug.enabled: false`

### 问题3：消息不显示
**检查**:
1. 确认配置中 `enabled: true`
2. 检查是否通过Velocity代理加入
3. 启用调试模式查看详细日志

### 问题4：首次加入检测错误
**解决**: 删除 `plugins/MessageTools/playerdata.yml`

## 📊 性能说明

### 调试模式影响
- **关闭调试**: 最小性能影响
- **基本调试**: 轻微日志开销
- **详细调试**: 适中日志开销，仅用于测试

### 推荐设置
- **生产环境**: 关闭所有调试选项
- **测试环境**: 启用基本调试
- **开发环境**: 启用所有调试选项

## 🎯 预期行为

### 正常模式（debug.enabled: false）
- 插件静默运行
- 只显示启动/关闭日志
- 玩家正常收到消息
- 控制台输出玩家加入/退出信息

### 调试模式（debug.enabled: true）
- 显示事件处理过程
- 显示配置加载状态
- 显示消息发送详情
- 便于问题排查

## 📞 技术支持

如果遇到问题：
1. 首先启用调试模式：`debug.enabled: true`
2. 查看详细日志定位问题
3. 参考 `TROUBLESHOOTING.md`
4. 检查配置文件语法

## 🔄 版本历史

### v1.1 (当前版本)
- 修复类型转换错误
- 添加调试开关系统
- 优化日志输出
- 改进配置处理

### v1.0
- 初始版本
- 基本功能实现
- 修复插件加载问题

---

**重要**: 现在插件应该能够稳定运行，不再出现类型转换错误。调试信息可以通过配置文件控制。
