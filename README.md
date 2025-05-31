# MessageTools

一个功能强大的Velocity代理服务器插件，用于自定义玩家加入/退出消息，支持PlaceholderAPI变量解析。
> **注意**：本插件由 AI 开发，旨在简化跨服传送流程并优化玩家体验。

## 功能特性

### 🎯 核心功能
- **玩家加入消息** - 当玩家加入群组服时向全服聊天发送自定义消息
- **玩家退出消息** - 当玩家退出群组服时向全服聊天发送自定义消息
- **私人欢迎消息** - 向新加入的玩家发送私人欢迎消息
- **首次加入/退出** - 为第一次加入/退出的玩家显示特殊消息
- **控制台日志** - 在控制台输出玩家加入/退出信息

### 🎨 消息格式支持
- **原版颜色代码** - 支持 `&a`, `&b`, `&c` 等颜色代码
- **原版格式代码** - 支持 `&l`, `&n`, `&o`, `&k` 等格式代码
- **Unicode字符** - 支持货币符号（￥、₦、€）、特殊符号、表情符号等
- **PlaceholderAPI** - 通过PAPIProxyBridge支持PlaceholderAPI变量
- **内置变量** - 提供丰富的内置变量替换

### ⏰ Tick定时发送
- **精确时机控制** - 使用tick值控制每种消息的发送时机
- **统一延迟发送** - 同一类别的所有消息在指定tick后同时发出
- **避免割裂感** - 消息组作为完整信息块出现，不被其他类别打断
- **避免消息冲突** - 不同类型消息按设定顺序发送
- **灵活配置** - 支持0-200+ tick的延迟范围
- **调试支持** - 详细的调度日志帮助优化配置

### 📊 数据管理
- **玩家数据持久化** - 自动记录玩家首次加入状态
- **自动保存** - 定期自动保存玩家数据
- **YAML配置** - 易于编辑的YAML配置文件

## 安装要求

- **Velocity** 3.4.0 或更高版本
- **Java** 17 或更高版本
- **PAPIProxyBridge** (可选，用于PlaceholderAPI支持)

## 安装步骤

1. 下载最新版本的MessageTools插件
2. 将插件文件放入Velocity服务器的 `plugins` 文件夹
3. (可选) 安装PAPIProxyBridge插件以获得PlaceholderAPI支持
4. 重启Velocity服务器
5. 编辑生成的配置文件 `plugins/MessageTools/config.yml`
6. 使用 `/velocity reload` 重新加载配置

## 配置说明

### 消息配置

```yaml
messages:
  # 玩家加入消息
  join:
    enabled: true
    tick: 20  # 1秒后发送
    messages:
      - "&a[+] &f%player_name% &7加入了服务器"
      - "&7欢迎 &a%player_name% &7来到我们的服务器！"

  # 玩家退出消息
  quit:
    enabled: true
    tick: 0  # 立即发送
    messages:
      - "&c[-] &f%player_name% &7离开了服务器"

  # 私人欢迎消息
  message:
    enabled: true
    tick: 60  # 3秒后发送
    messages:
      - "&a欢迎来到服务器，%player_name%！ {unicode:smile}"
      - "&7当前在线玩家数：&a%server_online%"
      - "&7你的余额：&e{unicode:yen}1000 {unicode:star}"

  # 首次加入消息
  first_join:
    enabled: true
    tick: 10  # 0.5秒后发送，优先于普通加入消息
    messages:
      - "&6★ &f%player_name% &e第一次加入了服务器！"

  # 首次退出消息
  first_quit:
    enabled: true
    tick: 0  # 立即发送
    messages:
      - "&6★ &f%player_name% &e第一次离开了服务器"
```

### Tick定时配置说明

- `tick: 0` - 立即发送
- `tick: 10` - 0.5秒后发送 (10 × 50ms)
- `tick: 20` - 1秒后发送 (20 × 50ms)
- `tick: 60` - 3秒后发送 (60 × 50ms)

通过不同的tick值，可以控制消息的发送顺序，避免消息互相干扰。

### Unicode字符支持

MessageTools支持在消息中使用Unicode字符，让你的消息更加丰富：

#### 命名Unicode占位符
```yaml
messages:
  join:
    messages:
      - "欢迎 %player_name%！ {unicode:star}"
      - "你的余额: {unicode:yen}1000"
      - "方向: {unicode:arrow_right} 商店"
```

#### 常用Unicode符号
- **货币**: `{unicode:yen}` → ￥, `{unicode:euro}` → €, `{unicode:dollar}` → $
- **箭头**: `{unicode:arrow_right}` → →, `{unicode:arrow_left}` → ←
- **符号**: `{unicode:star}` → ★, `{unicode:heart}` → ♥, `{unicode:smile}` → ☺
- **游戏**: `{unicode:sword}` → ⚔, `{unicode:shield}` → 🛡, `{unicode:pickaxe}` → ⛏

#### 十六进制Unicode
```yaml
messages:
  special:
    messages:
      - "特殊符号: {u:FFE5}"  # ￥
      - "星星: {u:2605}"      # ★
```

详细的Unicode字符列表请查看 `UNICODE_FEATURE.md` 文档。

### 内置变量

| 变量 | 描述 |
|------|------|
| `%player_name%` | 玩家用户名 |
| `%player_uuid%` | 玩家UUID |
| `%player_ip%` | 玩家IP地址 |
| `%server_online%` | 当前在线玩家数 |
| `%server_max%` | 服务器最大玩家数 |
| `%server_time_HH:mm:ss%` | 当前时间 (时:分:秒) |
| `%server_time_yyyy-MM-dd%` | 当前日期 (年-月-日) |
| `%server_time_yyyy-MM-dd HH:mm:ss%` | 当前日期时间 |

### 延迟配置

```yaml
delays:
  # 消息之间的发送间隔 (毫秒)
  between_messages: 500
  # 玩家加入后的延迟发送时间 (毫秒)
  after_join: 1000
```

### 控制台输出配置

```yaml
console:
  enabled: true
  format:
    join: "[MessageTools] 玩家 %player_name% 加入了服务器 (IP: %player_ip%)"
    quit: "[MessageTools] 玩家 %player_name% 离开了服务器"
    first_join: "[MessageTools] 新玩家 %player_name% 第一次加入服务器"
    first_quit: "[MessageTools] 新玩家 %player_name% 第一次离开服务器"
```

## PlaceholderAPI集成

如果安装了PAPIProxyBridge插件，MessageTools将自动支持PlaceholderAPI变量：

```yaml
placeholderapi:
  enabled: true
  timeout: 1000  # 变量解析超时时间 (毫秒)
```

支持的PlaceholderAPI变量示例：
- `%player_displayname%`
- `%player_level%`
- `%vault_eco_balance%`
- 以及其他所有PlaceholderAPI扩展提供的变量

## 颜色代码参考

### 颜色代码
- `&0` - 黑色
- `&1` - 深蓝色
- `&2` - 深绿色
- `&3` - 深青色
- `&4` - 深红色
- `&5` - 深紫色
- `&6` - 金色
- `&7` - 灰色
- `&8` - 深灰色
- `&9` - 蓝色
- `&a` - 绿色
- `&b` - 青色
- `&c` - 红色
- `&d` - 粉色
- `&e` - 黄色
- `&f` - 白色

### 格式代码
- `&k` - 随机字符
- `&l` - 粗体
- `&m` - 删除线
- `&n` - 下划线
- `&o` - 斜体
- `&r` - 重置格式

## 故障排除

### 插件没有工作？

如果插件加载后没有出现预期的消息和控制台输出，请按以下步骤排查：

1. **检查插件启动日志**
   ```
   [INFO] MessageTools 插件正在启动...
   [INFO] MessageTools 插件启动成功！
   [INFO] 事件监听器已注册
   ```

2. **使用测试配置**
   - 将项目中的 `test-config.yml` 重命名为 `config.yml`
   - 放在 `plugins/MessageTools/` 目录下
   - 重启服务器测试

3. **启用调试日志**
   在 `velocity.toml` 中设置：
   ```toml
   [advanced]
   log-level = "DEBUG"
   ```

4. **检查事件触发**
   玩家加入时应该看到：
   ```
   [INFO] 检测到玩家登录事件: PlayerName
   [INFO] 处理玩家加入事件: PlayerName (第一次: true)
   ```

详细的故障排除指南请查看 `TROUBLESHOOTING.md` 文件。

### 常见问题

**Q: 消息没有显示颜色？**
A: 确保使用了正确的颜色代码格式 (`&` 符号)，并检查配置文件语法是否正确。

**Q: PlaceholderAPI变量没有被替换？**
A: 确保已安装PAPIProxyBridge插件，并在配置中启用了PlaceholderAPI支持。

**Q: 首次加入检测不准确？**
A: 删除 `plugins/MessageTools/playerdata.yml` 文件重置玩家数据，或手动编辑该文件。

**Q: 消息发送延迟太长？**
A: 调整配置文件中的 `delays` 设置，减少延迟时间。

**Q: 插件加载但没有任何输出？**
A: 检查是否通过Velocity代理加入，而不是直接连接后端服务器。确认配置文件中的 `enabled` 选项为 `true`。

## 开发信息

- **版本**: 1.0-SNAPSHOT
- **兼容性**: Velocity 3.4.0+
- **Java版本**: 17+
- **许可证**: MIT

## 更新日志

### v1.7-SNAPSHOT (当前版本)
- 🌟 **Unicode字符支持** - 支持货币符号（￥、₦、€）、特殊符号、表情符号等
- 📝 **命名Unicode占位符** - 使用 `{unicode:name}` 格式插入预定义Unicode字符
- 🔢 **十六进制Unicode** - 使用 `{u:XXXX}` 格式插入任意Unicode字符
- 🎮 **丰富符号库** - 内置80+常用Unicode符号，涵盖货币、箭头、游戏、天气等

### v1.6-SNAPSHOT
- 🎯 **真正的统一延迟** - 同一类别的所有消息在指定tick后同时发出
- 📦 **消除割裂感** - 移除消息组内部延迟，消息作为完整信息块出现
- 🔧 **优化显示效果** - 每个消息类别保持完整性，不被其他类别打断
- 📊 **性能优化** - 减少调度任务，提高发送效率

### v1.5-SNAPSHOT
- 🎯 **改进Tick定时发送** - 同一类别消息作为整体发送，避免割裂感
- 📦 **整体消息调度** - 消息组在指定tick后连续发送，保持连贯性
- 🔧 **优化用户体验** - 逻辑相关的消息不再被其他类别打断
- 📊 **批量消息处理** - 改进的异步处理机制

### v1.4-SNAPSHOT
- 🎯 **新增Tick定时发送功能** - 精确控制各种消息的发送时机
- ⏰ **消息顺序控制** - 避免不同类型消息互相干扰
- 🔧 **灵活配置** - 支持0-200+ tick的延迟范围
- 📊 **调试支持** - 详细的调度日志帮助优化配置

### v1.3-SNAPSHOT
- 修复PlaceholderAPI调用时的NullPointerException
- 使用正确的PAPIProxyBridge API调用方式
- 提供了禁用PlaceholderAPI的选项以获得最佳性能

### v1.2-SNAPSHOT
- 修复调度器插件注册问题 (`plugin is not registered`)
- 确保所有异步任务正常工作
- 完善错误处理机制

### v1.1-SNAPSHOT
- 修复类型转换错误 (`Integer cannot be cast to Long`)
- 添加调试开关系统
- 优化日志输出控制

### v1.0-SNAPSHOT
- 初始版本发布
- 支持基本的加入/退出消息
- 集成PlaceholderAPI支持
- 首次加入/退出检测
- 自动数据保存功能

## 支持

如果您遇到问题或有功能建议，请：
1. 检查配置文件语法是否正确
2. 查看控制台日志获取错误信息
3. 确保所有依赖插件已正确安装
4. 联系插件开发者获取技术支持
