# MessageTools 快速开始指南

## 🚀 快速部署

### 1. 安装插件
1. 将 `target/MessageTools-1.0-SNAPSHOT.jar` 复制到你的Velocity服务器的 `plugins` 文件夹
2. 重启Velocity服务器

### 2. 验证安装
启动服务器后，在控制台中查找以下日志：
```
[INFO] MessageTools 插件正在启动...
[INFO] 配置文件加载成功
[INFO] 事件监听器已注册
[INFO] MessageTools 插件启动成功！
```

**如果看到 NullPointerException 错误**：
这通常是因为 `velocity-plugin.json` 文件格式问题。请确保使用最新编译的JAR文件。

### 3. 快速测试
1. 将 `test-config.yml` 重命名为 `config.yml`
2. 放在 `plugins/MessageTools/` 目录下
3. 执行 `/velocity reload`
4. 让玩家加入服务器测试

## 🔧 基础配置

### 最简配置
```yaml
messages:
  join:
    enabled: true
    messages:
      - "&a%player_name% joined"

  message:
    enabled: true
    messages:
      - "&7Welcome %player_name%!"

console:
  enabled: true
  format:
    join: "[Join] %player_name%"
```

### 完整配置示例
```yaml
messages:
  join:
    enabled: true
    messages:
      - "&a[+] &f%player_name% &7joined the server"
      - "&7Welcome to our community!"

  quit:
    enabled: true
    messages:
      - "&c[-] &f%player_name% &7left the server"

  message:
    enabled: true
    messages:
      - "&6Welcome, &a%player_name%&6!"
      - "&7Online: &b%server_online%&7/&b%server_max%"
      - "&7Time: &e%server_time_HH:mm:ss%"

  first_join:
    enabled: true
    messages:
      - "&6★ &eNew player &f%player_name% &ejoined for the first time!"

  first_quit:
    enabled: true
    messages:
      - "&6★ &f%player_name% &eleft for the first time"

console:
  enabled: true
  format:
    join: "[MessageTools] %player_name% joined from %player_ip%"
    quit: "[MessageTools] %player_name% left"
    first_join: "[MessageTools] NEW: %player_name%"
    first_quit: "[MessageTools] NEW LEFT: %player_name%"

delays:
  between_messages: 500
  after_join: 1000

storage:
  auto_save: true
  auto_save_interval: 5

placeholderapi:
  enabled: true
  timeout: 1000
```

## 🎨 颜色代码速查

| 代码 | 颜色 | 代码 | 格式 |
|------|------|------|------|
| `&0` | 黑色 | `&l` | 粗体 |
| `&1` | 深蓝 | `&m` | 删除线 |
| `&2` | 深绿 | `&n` | 下划线 |
| `&3` | 深青 | `&o` | 斜体 |
| `&4` | 深红 | `&k` | 随机 |
| `&5` | 深紫 | `&r` | 重置 |
| `&6` | 金色 | | |
| `&7` | 灰色 | | |
| `&8` | 深灰 | | |
| `&9` | 蓝色 | | |
| `&a` | 绿色 | | |
| `&b` | 青色 | | |
| `&c` | 红色 | | |
| `&d` | 粉色 | | |
| `&e` | 黄色 | | |
| `&f` | 白色 | | |

## 📝 内置变量

| 变量 | 说明 | 示例 |
|------|------|------|
| `%player_name%` | 玩家名称 | `Steve` |
| `%player_uuid%` | 玩家UUID | `123e4567-e89b-12d3...` |
| `%player_ip%` | 玩家IP | `192.168.1.100` |
| `%server_online%` | 在线玩家数 | `15` |
| `%server_max%` | 最大玩家数 | `100` |
| `%server_time_HH:mm:ss%` | 时间 | `14:30:25` |
| `%server_time_yyyy-MM-dd%` | 日期 | `2024-01-15` |

## 🔍 故障排除

### 问题：插件没有输出任何消息

**解决步骤：**
1. 检查控制台是否有插件启动日志
2. 确认配置文件中 `enabled: true`
3. 启用调试日志：在 `velocity.toml` 中设置 `log-level = "DEBUG"`
4. 确认通过Velocity代理加入，不是直连后端服务器

### 问题：首次加入检测不正确

**解决方案：**
1. 删除 `plugins/MessageTools/playerdata.yml`
2. 重启服务器
3. 重新测试

### 问题：PlaceholderAPI变量不工作

**解决方案：**
1. 安装 PAPIProxyBridge 插件
2. 确认配置中 `placeholderapi.enabled: true`
3. 检查变量名称是否正确

## 📋 测试清单

在部署到生产环境前，请测试以下功能：

- [ ] 插件正确加载并显示启动日志
- [ ] 玩家加入时显示加入消息
- [ ] 玩家退出时显示退出消息
- [ ] 玩家收到私人欢迎消息
- [ ] 首次加入玩家显示特殊消息
- [ ] 控制台输出玩家加入/退出信息
- [ ] 颜色代码正确显示
- [ ] 内置变量正确替换
- [ ] 配置重载功能正常
- [ ] 玩家数据正确保存

## 🎯 性能建议

### 高流量服务器
```yaml
delays:
  between_messages: 100  # 减少延迟
  after_join: 200

messages:
  join:
    messages:
      - "&a%player_name% joined"  # 简短消息
  message:
    enabled: false  # 禁用私人消息减少负载

storage:
  auto_save_interval: 10  # 增加保存间隔
```

### 小型服务器
```yaml
delays:
  between_messages: 1000  # 增加延迟营造氛围
  after_join: 2000

messages:
  join:
    messages:
      - "&6=== &eWelcome &6==="
      - "&a%player_name% &7joined our community!"
      - "&7Enjoy your stay!"

storage:
  auto_save_interval: 1  # 频繁保存
```

## 📞 获取帮助

如果遇到问题：
1. 查看 `TROUBLESHOOTING.md` 详细故障排除指南
2. 检查 `README.md` 完整文档
3. 查看 `USAGE_EXAMPLES.md` 配置示例
4. 启用调试日志获取更多信息

## 🔄 更新插件

1. 备份当前配置文件
2. 替换插件JAR文件
3. 重启服务器
4. 检查配置文件兼容性
5. 测试所有功能
