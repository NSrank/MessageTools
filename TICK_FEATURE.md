# MessageTools Tick定时发送功能

## 🎯 功能概述

新增的Tick定时发送功能允许你精确控制各种消息的发送时机，避免消息之间互相干扰，创造更好的玩家体验。

## ⏰ Tick配置说明

### 什么是Tick？
- 1 tick = 50毫秒 (0.05秒)
- 20 ticks = 1秒
- 这是Minecraft服务器的标准时间单位

### 配置格式
```yaml
messages:
  message_type:
    enabled: true
    tick: 20  # 延迟20tick (1秒) 后发送
    messages:
      - "消息内容"
```

## 📋 配置示例

### 基础配置
```yaml
messages:
  # 首次加入消息 - 优先显示
  first_join:
    enabled: true
    tick: 10  # 0.5秒后发送
    messages:
      - "&6★ 新玩家 %player_name% 加入了服务器！"
  
  # 普通加入消息 - 其次显示
  join:
    enabled: true
    tick: 20  # 1秒后发送
    messages:
      - "&a[+] %player_name% 加入了服务器"
  
  # 私人消息 - 最后发送
  message:
    enabled: true
    tick: 60  # 3秒后发送
    messages:
      - "&a欢迎来到服务器，%player_name%！"
  
  # 退出消息 - 立即发送
  quit:
    enabled: true
    tick: 0  # 立即发送
    messages:
      - "&c[-] %player_name% 离开了服务器"
```

### 高级配置 - 创造欢迎序列
```yaml
messages:
  first_join:
    enabled: true
    tick: 5   # 0.25秒 - 立即欢迎
    messages:
      - "&6★ &e新玩家来了！"
  
  join:
    enabled: true
    tick: 20  # 1秒 - 显示玩家信息
    messages:
      - "&a[+] &f%player_name% &7加入了服务器"
  
  message:
    enabled: true
    tick: 80  # 4秒 - 详细欢迎信息
    messages:
      - "&6=== &e欢迎来到我们的服务器 &6==="
      - "&a你好，%player_name%！"
      - "&7当前时间：&e%server_time_HH:mm:ss%"
      - "&7在线玩家：&b%server_online%&7/&b%server_max%"
      - "&6=========================="
```

## 🎮 实际效果演示

### 玩家首次加入时的消息顺序：
```
[0.25秒] ★ 新玩家来了！
[1.0秒]  [+] PlayerName 加入了服务器
[4.0秒]  === 欢迎来到我们的服务器 ===
         你好，PlayerName！
         当前时间：21:30:15
         在线玩家：5/100
         ============================
```

### 玩家普通加入时的消息顺序：
```
[1.0秒]  [+] PlayerName 加入了服务器
[4.0秒]  === 欢迎来到我们的服务器 ===
         你好，PlayerName！
         ...
```

## ⚙️ Tick时间对照表

| Tick值 | 延迟时间 | 适用场景 |
|--------|----------|----------|
| 0 | 立即发送 | 退出消息、紧急通知 |
| 5 | 0.25秒 | 快速响应消息 |
| 10 | 0.5秒 | 首次加入优先消息 |
| 20 | 1秒 | 标准加入消息 |
| 40 | 2秒 | 中等延迟消息 |
| 60 | 3秒 | 详细欢迎信息 |
| 100 | 5秒 | 延迟提示信息 |
| 200 | 10秒 | 长延迟消息 |

## 🎨 推荐配置方案

### 方案1：简洁模式
```yaml
first_join:
  tick: 10  # 首次加入消息
join:
  tick: 20  # 普通加入消息
message:
  tick: 40  # 私人消息
quit:
  tick: 0   # 立即退出
```

### 方案2：详细模式
```yaml
first_join:
  tick: 5   # 立即欢迎新玩家
join:
  tick: 30  # 稍后显示加入信息
message:
  tick: 80  # 详细欢迎信息
quit:
  tick: 0   # 立即退出
```

### 方案3：渐进模式
```yaml
first_join:
  tick: 0   # 立即显示
join:
  tick: 20  # 1秒后
message:
  tick: 60  # 3秒后
quit:
  tick: 0   # 立即退出
```

## 🔧 调试功能

启用调试模式可以看到tick调度的详细信息：

```yaml
debug:
  enabled: true
  verbose_events: true
```

调试输出示例：
```
[INFO] 处理玩家加入事件: PlayerName (第一次: true)
[INFO] 安排 first_join 消息发送: 10tick (500ms) 后执行
[INFO] 安排 message 消息发送: 60tick (3000ms) 后执行
```

## 💡 最佳实践

### 1. 消息优先级设计
- **首次加入** (tick: 5-10) - 最高优先级
- **普通加入** (tick: 20-30) - 中等优先级
- **私人消息** (tick: 60-100) - 最低优先级
- **退出消息** (tick: 0) - 立即发送

### 2. 避免消息冲突
- 不同类型消息使用不同的tick值
- 确保重要消息不被覆盖
- 考虑聊天滚动速度

### 3. 用户体验优化
- 首次加入玩家需要更多关注
- 避免消息过于密集
- 给玩家时间阅读每条消息

### 4. 性能考虑
- 避免使用过大的tick值
- 合理安排消息数量
- 考虑服务器负载

## 🔄 与旧版本的兼容性

- 如果配置文件中没有`tick`选项，默认使用`tick: 0`（立即发送）
- 原有的`delays.after_join`配置仍然有效，但会被tick配置覆盖
- `delays.between_messages`仍然控制同一类型消息内部的间隔

## 📊 配置文件示例

查看以下配置文件获取完整示例：
- `tick-test-config.yml` - 完整的tick配置演示
- `simple-config.yml` - 简化配置（不使用tick）
- `production-config.yml` - 生产环境配置

## 🚀 部署建议

1. **测试环境**：使用`tick-test-config.yml`测试各种延迟效果
2. **生产环境**：根据服务器特点调整tick值
3. **监控效果**：启用调试模式观察消息发送时机
4. **用户反馈**：根据玩家反馈调整延迟时间

---

通过Tick定时发送功能，你可以创造出专业、有序的玩家加入体验，让每条消息都在最合适的时机出现！
