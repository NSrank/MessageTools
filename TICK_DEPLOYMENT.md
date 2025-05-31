# MessageTools v1.4 - Tick定时发送功能部署指南

## 🎉 新功能亮点

### ⏰ Tick定时发送
- **精确控制** - 使用tick值精确控制每种消息的发送时机
- **避免冲突** - 不同类型消息按设定顺序发送，不再互相干扰
- **灵活配置** - 支持0-200+ tick的延迟范围
- **专业体验** - 创造有序、专业的玩家加入体验

## 📦 部署步骤

### 1. 使用最新插件文件
```
target/MessageTools-1.0-SNAPSHOT.jar
```

### 2. 选择配置文件
根据需求选择合适的配置：

- **`tick-test-config.yml`** - 完整的tick功能演示
- **`simple-config.yml`** - 简化配置，不使用tick功能
- **`production-config.yml`** - 生产环境完整配置

### 3. 配置Tick延迟
在配置文件中为每种消息类型设置tick值：

```yaml
messages:
  first_join:
    enabled: true
    tick: 10  # 0.5秒后发送
    messages:
      - "&6★ 新玩家 %player_name% 加入了服务器！"
  
  join:
    enabled: true
    tick: 20  # 1秒后发送
    messages:
      - "&a[+] %player_name% 加入了服务器"
  
  message:
    enabled: true
    tick: 60  # 3秒后发送
    messages:
      - "&a欢迎来到服务器，%player_name%！"
```

## 🎯 推荐配置方案

### 方案1：快速响应型
```yaml
first_join: { tick: 5 }   # 0.25秒 - 立即欢迎
join: { tick: 15 }        # 0.75秒 - 快速显示
message: { tick: 40 }     # 2秒 - 适中延迟
quit: { tick: 0 }         # 立即 - 即时反馈
```

### 方案2：渐进展示型
```yaml
first_join: { tick: 10 }  # 0.5秒 - 优先显示
join: { tick: 30 }        # 1.5秒 - 稍后显示
message: { tick: 80 }     # 4秒 - 详细信息
quit: { tick: 0 }         # 立即 - 即时反馈
```

### 方案3：详细欢迎型
```yaml
first_join: { tick: 0 }   # 立即 - 最高优先级
join: { tick: 20 }        # 1秒 - 标准延迟
message: { tick: 100 }    # 5秒 - 充分延迟
quit: { tick: 0 }         # 立即 - 即时反馈
```

## 🧪 测试和验证

### 1. 启用调试模式
```yaml
debug:
  enabled: true
  verbose_events: true
```

### 2. 观察调度日志
```
[INFO] 处理玩家加入事件: PlayerName (第一次: true)
[INFO] 安排 first_join 消息发送: 10tick (500ms) 后执行
[INFO] 安排 message 消息发送: 60tick (3000ms) 后执行
```

### 3. 验证消息顺序
玩家加入时应该看到：
```
[0.5秒] ★ 新玩家 PlayerName 加入了服务器！
[1.0秒] [+] PlayerName 加入了服务器
[3.0秒] 欢迎来到服务器，PlayerName！
```

## ⚙️ Tick配置详解

### Tick时间换算
| Tick值 | 实际延迟 | 适用场景 |
|--------|----------|----------|
| 0 | 立即发送 | 退出消息、紧急通知 |
| 5 | 0.25秒 | 快速响应 |
| 10 | 0.5秒 | 优先消息 |
| 20 | 1秒 | 标准延迟 |
| 40 | 2秒 | 中等延迟 |
| 60 | 3秒 | 详细信息 |
| 100 | 5秒 | 长延迟 |

### 配置原则
1. **首次加入消息** - 使用较小的tick值（5-15）
2. **普通加入消息** - 使用中等tick值（15-30）
3. **私人消息** - 使用较大的tick值（40-100）
4. **退出消息** - 通常使用0（立即发送）

## 🎮 实际效果演示

### 首次加入玩家的体验：
```
时间轴：
0.0秒 - 玩家连接到服务器
0.5秒 - ★ 新玩家 PlayerName 加入了服务器！
1.0秒 - [+] PlayerName 加入了服务器
3.0秒 - 欢迎来到服务器，PlayerName！
       当前在线玩家：5人
       服务器时间：21:30:15
```

### 普通加入玩家的体验：
```
时间轴：
0.0秒 - 玩家连接到服务器
1.0秒 - [+] PlayerName 加入了服务器
3.0秒 - 欢迎来到服务器，PlayerName！
```

## 🔧 高级配置技巧

### 1. 创建欢迎序列
```yaml
first_join:
  tick: 5
  messages: ["&6★ 新玩家来了！"]

join:
  tick: 25
  messages: ["&a[+] %player_name% 加入了服务器"]

message:
  tick: 70
  messages:
    - "&6=== 欢迎来到我们的服务器 ==="
    - "&a你好，%player_name%！"
    - "&7当前时间：&e%server_time_HH:mm:ss%"
    - "&6=========================="
```

### 2. 差异化处理
```yaml
# 新玩家获得更多关注
first_join:
  tick: 0    # 立即欢迎新玩家
  
# 老玩家使用标准流程
join:
  tick: 20   # 标准延迟
```

### 3. 服务器类型优化
```yaml
# 生存服务器 - 注重沉浸感
first_join: { tick: 15 }
join: { tick: 40 }
message: { tick: 100 }

# 小游戏服务器 - 注重快节奏
first_join: { tick: 5 }
join: { tick: 15 }
message: { tick: 30 }
```

## 📊 性能影响

### Tick调度的性能特点
- **轻量级** - 每个tick调度只占用极少资源
- **异步执行** - 不会阻塞主线程
- **内存友好** - 调度任务在执行后自动清理

### 推荐设置
- **小型服务器** (1-20人) - 可以使用较长的tick值
- **中型服务器** (20-100人) - 使用中等tick值
- **大型服务器** (100+人) - 使用较短的tick值或禁用部分消息

## 🔍 故障排除

### 问题1：消息没有按预期时间发送
**检查**：
1. 确认tick值配置正确
2. 启用调试模式查看调度日志
3. 检查服务器性能是否正常

### 问题2：消息顺序混乱
**解决**：
1. 确保不同消息类型使用不同的tick值
2. 避免tick值过于接近
3. 考虑服务器延迟因素

### 问题3：调试日志过多
**解决**：
```yaml
debug:
  enabled: false  # 生产环境关闭调试
```

## 🚀 部署检查清单

部署前确认：
- [ ] 使用最新的JAR文件
- [ ] 配置了合适的tick值
- [ ] 测试了消息发送顺序
- [ ] 调试模式设置正确

部署后验证：
- [ ] 插件正常加载
- [ ] 消息按预期时间发送
- [ ] 消息顺序正确
- [ ] 性能表现良好

---

通过Tick定时发送功能，MessageTools现在可以提供更加专业和有序的玩家体验。合理配置tick值，让每条消息都在最合适的时机出现！
