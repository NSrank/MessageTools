# MessageTools 使用示例

本文档提供了MessageTools插件的详细使用示例和配置案例。

## 基础配置示例

### 简单的加入/退出消息

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
```

### 多行欢迎消息

```yaml
messages:
  message:
    enabled: true
    messages:
      - "&6=== &e欢迎来到我们的服务器 &6==="
      - "&a你好，%player_name%！"
      - "&7当前在线玩家：&b%server_online%&7/&b%server_max%"
      - "&7服务器时间：&e%server_time_HH:mm:ss%"
      - "&6==========================="
```

## 高级配置示例

### 带有特殊效果的首次加入消息

```yaml
messages:
  first_join:
    enabled: true
    messages:
      - "&6&l★ &e&l新玩家加入 &6&l★"
      - "&f%player_name% &a第一次来到了我们的服务器！"
      - "&7让我们热烈欢迎这位新朋友！"
      - "&d&o愿你在这里度过愉快的时光~"
```

### 个性化的退出消息

```yaml
messages:
  quit:
    enabled: true
    messages:
      - "&8[&c-&8] &7%player_name% &8离开了游戏"
      - "&8希望你很快回来！"
  
  first_quit:
    enabled: true
    messages:
      - "&6★ &e%player_name% &7第一次离开了服务器"
      - "&7期待你的再次光临！"
```

## PlaceholderAPI 集成示例

如果安装了PAPIProxyBridge，可以使用更多变量：

```yaml
messages:
  join:
    enabled: true
    messages:
      - "&a[+] &f%player_displayname% &7加入了服务器"
      - "&7等级：&e%player_level% &7| 金币：&6%vault_eco_balance%"
  
  message:
    enabled: true
    messages:
      - "&6欢迎回来，%player_displayname%！"
      - "&7你的等级：&a%player_level%"
      - "&7账户余额：&e$%vault_eco_balance%"
      - "&7游戏时间：&b%statistic_play_one_minute%"
```

## 延迟配置示例

### 快速消息发送

```yaml
delays:
  between_messages: 200    # 消息间隔200毫秒
  after_join: 500         # 加入后500毫秒发送
```

### 慢速消息发送（营造氛围）

```yaml
delays:
  between_messages: 1500   # 消息间隔1.5秒
  after_join: 2000        # 加入后2秒发送
```

## 控制台日志配置示例

### 详细日志记录

```yaml
console:
  enabled: true
  format:
    join: "[MessageTools] 玩家 %player_name% 从 %player_ip% 加入服务器"
    quit: "[MessageTools] 玩家 %player_name% 离开服务器 (在线时长: %session_time%)"
    first_join: "[MessageTools] 🎉 新玩家 %player_name% 首次加入！"
    first_quit: "[MessageTools] 👋 新玩家 %player_name% 首次离开"
```

### 简洁日志记录

```yaml
console:
  enabled: true
  format:
    join: "[+] %player_name%"
    quit: "[-] %player_name%"
    first_join: "[NEW] %player_name%"
    first_quit: "[NEW-] %player_name%"
```

## 主题化配置示例

### 科幻主题

```yaml
messages:
  join:
    enabled: true
    messages:
      - "&b&l>>> &f%player_name% &b已连接到网络 &b&l<<<"
      - "&7系统状态：&a在线 &7| 连接数：&e%server_online%"
  
  quit:
    enabled: true
    messages:
      - "&c&l>>> &f%player_name% &c已断开连接 &c&l<<<"
  
  first_join:
    enabled: true
    messages:
      - "&d&l⚡ &5新用户 &f%player_name% &5已注册到系统 &d&l⚡"
      - "&7正在初始化用户配置文件..."
      - "&a✓ &7初始化完成！欢迎加入网络！"
```

### 中世纪主题

```yaml
messages:
  join:
    enabled: true
    messages:
      - "&6⚔ &e骑士 &f%player_name% &e踏入了王国 &6⚔"
      - "&7愿荣耀与你同在！"
  
  quit:
    enabled: true
    messages:
      - "&8🛡 &7骑士 &f%player_name% &7离开了王国"
  
  first_join:
    enabled: true
    messages:
      - "&6&l👑 &e新的勇士 &f%player_name% &e加入了我们的行列！ &6&l👑"
      - "&7愿你的冒险充满传奇！"
```

### 现代简约主题

```yaml
messages:
  join:
    enabled: true
    messages:
      - "&f%player_name% &8joined the server"
  
  quit:
    enabled: true
    messages:
      - "&f%player_name% &8left the server"
  
  message:
    enabled: true
    messages:
      - "&fWelcome, &a%player_name%&f!"
      - "&8Online: &7%server_online%&8/&7%server_max%"
```

## 特殊场景配置

### 仅首次加入有消息

```yaml
messages:
  join:
    enabled: false          # 禁用普通加入消息
  
  quit:
    enabled: false          # 禁用普通退出消息
  
  first_join:
    enabled: true           # 只有首次加入才显示
    messages:
      - "&6🎉 欢迎新玩家 &f%player_name% &6加入服务器！"
  
  first_quit:
    enabled: false          # 不显示首次退出消息
```

### 仅私人消息

```yaml
messages:
  join:
    enabled: false          # 不向全服发送加入消息
  
  quit:
    enabled: false          # 不向全服发送退出消息
  
  message:
    enabled: true           # 只向玩家本人发送欢迎消息
    messages:
      - "&a欢迎来到服务器！"
      - "&7输入 &e/help &7查看帮助"
```

## 数据存储配置示例

### 高频保存（适合高流量服务器）

```yaml
storage:
  player_data_file: "playerdata.yml"
  auto_save: true
  auto_save_interval: 1     # 每分钟保存一次
```

### 低频保存（适合小型服务器）

```yaml
storage:
  player_data_file: "playerdata.yml"
  auto_save: true
  auto_save_interval: 30    # 每30分钟保存一次
```

### 手动保存

```yaml
storage:
  player_data_file: "playerdata.yml"
  auto_save: false          # 禁用自动保存，只在服务器关闭时保存
```

## 故障排除配置

### 调试模式配置

在Velocity的配置文件中设置：

```toml
[advanced]
log-level = "DEBUG"
```

然后在MessageTools配置中：

```yaml
console:
  enabled: true
  format:
    join: "[DEBUG] Player %player_name% joined from %player_ip% at %server_time_yyyy-MM-dd HH:mm:ss%"
    quit: "[DEBUG] Player %player_name% quit at %server_time_yyyy-MM-dd HH:mm:ss%"
```

### 禁用PAPI集成

如果遇到PAPI相关问题：

```yaml
placeholderapi:
  enabled: false            # 禁用PlaceholderAPI集成
  timeout: 1000
```

## 性能优化建议

1. **减少消息数量**：避免在高流量服务器上发送过多消息
2. **增加延迟**：适当增加消息间隔以减少服务器负载
3. **禁用不需要的功能**：关闭不使用的消息类型
4. **优化自动保存**：根据服务器规模调整保存频率

```yaml
# 高性能配置示例
messages:
  join:
    enabled: true
    messages:
      - "&a%player_name% joined"    # 简短消息
  
  quit:
    enabled: true
    messages:
      - "&c%player_name% left"      # 简短消息
  
  message:
    enabled: false                  # 禁用私人消息以减少负载
  
  first_join:
    enabled: true                   # 保留首次加入消息
    messages:
      - "&6Welcome %player_name%!"
  
  first_quit:
    enabled: false                  # 禁用首次退出消息

delays:
  between_messages: 100             # 减少延迟
  after_join: 500

storage:
  auto_save_interval: 10            # 适中的保存频率
```
