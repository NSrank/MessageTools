# MessageTools 最终修复说明

## 🔧 修复的问题

### v1.3 - PlaceholderAPI修复
**问题**: PlaceholderAPI调用时的NullPointerException
**原因**: 使用了错误的API调用方式（静态方法而非实例方法）
**修复**:
- 使用正确的PAPIProxyBridge API调用方式
- 先创建API实例，再调用formatPlaceholders方法
- 提供了禁用PlaceholderAPI的选项以获得最佳性能

### v1.2 - 调度器修复
**问题**: `plugin is not registered` 错误
**原因**: MessageService中使用了错误的插件实例传递给调度器
**修复**:
- 在MessageService构造函数中添加了插件实例参数
- 将所有调度器调用从 `this` 改为正确的 `plugin` 实例
- 确保调度器使用正确注册的插件对象

### v1.1 - 类型转换修复
**问题**: `Integer cannot be cast to Long` 错误
**修复**: 改进了ConfigManager中的数字类型处理

### v1.0 - 基础修复
**问题**: NullPointerException 和事件监听问题
**修复**: 添加了正确的velocity-plugin.json和事件监听器

## 📦 部署最新版本

### 1. 使用最新JAR文件
```
target/MessageTools-1.0-SNAPSHOT.jar
```

### 2. 配置文件
推荐使用以下配置之一：
- **简化配置**: `simple-config.yml` (禁用PlaceholderAPI，最稳定)
- **测试环境**: `test-config.yml` (启用调试)
- **生产环境**: `production-config.yml` (完整功能)

### 3. 验证修复
启动服务器后应该看到：
```
[INFO] MessageTools 插件正在启动...
[INFO] 配置文件加载成功
[INFO] 事件监听器已注册
[INFO] MessageTools 插件启动成功！
```

**不应该再看到**：
- ❌ `plugin is not registered` 错误
- ❌ `ClassCastException` 错误
- ❌ `NullPointerException` 错误

## 🧪 测试步骤

### 1. 玩家加入测试
1. 玩家通过Velocity代理加入服务器
2. **应该看到**: 玩家收到欢迎消息
3. **应该看到**: 全服看到加入消息
4. **应该看到**: 控制台输出加入信息
5. **不应该看到**: 任何错误信息

### 2. 玩家退出测试
1. 玩家退出服务器
2. **应该看到**: 全服看到退出消息
3. **应该看到**: 控制台输出退出信息
4. **不应该看到**: 任何错误信息

### 3. 首次加入测试
1. 删除 `plugins/MessageTools/playerdata.yml`
2. 重启服务器
3. 玩家加入应该显示首次加入消息

## 🎛️ 调试配置

### 启用调试模式（排查问题时）
```yaml
debug:
  enabled: true
  verbose_events: true
  verbose_config: true
```

### 生产环境（正常运行时）
```yaml
debug:
  enabled: false
  verbose_events: false
  verbose_config: false
```

## 📋 技术细节

### 调度器修复详情
**问题根源**: Velocity的调度器要求传递已注册的插件实例
**解决方案**:
1. 在MessageService中保存插件实例引用
2. 所有调度器调用使用正确的插件实例
3. 确保插件实例是从主类传递的

**修改的文件**:
- `MessageService.java`: 添加插件实例参数
- `MessageTools.java`: 传递插件实例给MessageService

### 代码变更
```java
// 修改前 (错误)
server.getScheduler().buildTask(this, () -> { ... })

// 修改后 (正确)
server.getScheduler().buildTask(plugin, () -> { ... })
```

## 🎯 预期行为

### 正常运行状态
- 插件静默加载，无错误
- 玩家加入时正常显示消息
- 玩家退出时正常显示消息
- 首次加入检测正常工作
- 控制台输出清洁有序

### 调试模式状态
- 显示详细的事件处理过程
- 显示消息发送状态
- 显示配置加载信息
- 便于问题排查

## 🔍 故障排除

### 如果仍然有问题
1. **确认使用最新JAR文件**: `target/MessageTools-1.0-SNAPSHOT.jar`
2. **启用调试模式**: 设置 `debug.enabled: true`
3. **检查配置文件**: 确保YAML语法正确
4. **查看完整日志**: 检查是否有其他错误信息

### 常见问题
**Q: 消息不显示？**
A: 检查配置中 `enabled: true` 和通过Velocity代理加入

**Q: 首次加入检测错误？**
A: 删除 `playerdata.yml` 文件重置数据

**Q: 仍然有调度器错误？**
A: 确保使用最新编译的JAR文件

## 📊 版本历史

### v1.2 (当前版本)
- ✅ 修复调度器插件注册问题
- ✅ 确保所有异步任务正常工作
- ✅ 完善错误处理

### v1.1
- ✅ 修复类型转换错误
- ✅ 添加调试开关系统

### v1.0
- ✅ 基础功能实现
- ✅ 修复插件加载问题

## 🚀 部署检查清单

部署前请确认：
- [ ] 使用最新的JAR文件
- [ ] 配置文件语法正确
- [ ] 调试模式设置合适
- [ ] 备份了旧版本（如有）

部署后请验证：
- [ ] 插件正常加载，无错误
- [ ] 玩家加入消息正常
- [ ] 玩家退出消息正常
- [ ] 控制台输出正常
- [ ] 首次加入检测正常

---

**重要**: 这个版本应该完全解决了所有已知问题。如果仍然遇到问题，请启用调试模式并提供详细的错误日志。
