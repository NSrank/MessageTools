# MessageTools Unicode字符支持功能

## 🎯 功能概述

MessageTools现在支持在消息中使用Unicode字符，包括货币符号（￥、₦、€等）、特殊符号、表情符号等。这个功能让你的服务器消息更加丰富和专业。

## 🔧 使用方法

### 1. 命名Unicode占位符
使用 `{unicode:name}` 格式来插入预定义的Unicode字符：

```yaml
messages:
  join:
    messages:
      - "欢迎 %player_name%！ {unicode:star}"
      - "你的余额: {unicode:yen}1000"
      - "方向: {unicode:arrow_right} 商店"
```

**显示效果**：
```
欢迎 PlayerName！ ★
你的余额: ￥1000
方向: → 商店
```

### 2. 十六进制Unicode占位符
使用 `{u:XXXX}` 格式来插入任意Unicode字符：

```yaml
messages:
  join:
    messages:
      - "货币: {u:FFE5}100"  # ￥100
      - "星星: {u:2605}"      # ★
      - "红心: {u:2665}"      # ♥
```

## 📋 可用的Unicode符号

### 💰 货币符号
| 占位符 | 符号 | 描述 |
|--------|------|------|
| `{unicode:yen}` 或 `{unicode:yuan}` | ￥ | 日元/人民币 |
| `{unicode:euro}` | € | 欧元 |
| `{unicode:dollar}` | $ | 美元 |
| `{unicode:pound}` | £ | 英镑 |
| `{unicode:naira}` | ₦ | 奈拉 |
| `{unicode:won}` | ₩ | 韩元 |
| `{unicode:rupee}` | ₹ | 卢比 |
| `{unicode:ruble}` | ₽ | 卢布 |

### ➡️ 箭头符号
| 占位符 | 符号 | 描述 |
|--------|------|------|
| `{unicode:arrow_right}` | → | 右箭头 |
| `{unicode:arrow_left}` | ← | 左箭头 |
| `{unicode:arrow_up}` | ↑ | 上箭头 |
| `{unicode:arrow_down}` | ↓ | 下箭头 |
| `{unicode:arrow_double_right}` | ⇒ | 双右箭头 |
| `{unicode:arrow_double_left}` | ⇐ | 双左箭头 |

### ⭐ 特殊符号
| 占位符 | 符号 | 描述 |
|--------|------|------|
| `{unicode:star}` | ★ | 实心星星 |
| `{unicode:star_empty}` | ☆ | 空心星星 |
| `{unicode:heart}` | ♥ | 红心 |
| `{unicode:heart_empty}` | ♡ | 空心红心 |
| `{unicode:diamond}` | ♦ | 钻石 |
| `{unicode:club}` | ♣ | 梅花 |
| `{unicode:spade}` | ♠ | 黑桃 |
| `{unicode:music}` | ♪ | 音符 |
| `{unicode:note}` | ♫ | 音符 |

### 😊 表情符号
| 占位符 | 符号 | 描述 |
|--------|------|------|
| `{unicode:smile}` | ☺ | 笑脸 |
| `{unicode:frown}` | ☹ | 哭脸 |
| `{unicode:check}` | ✓ | 对勾 |
| `{unicode:cross}` | ✗ | 叉号 |
| `{unicode:warning}` | ⚠ | 警告 |
| `{unicode:info}` | ℹ | 信息 |

### 🧭 方向符号
| 占位符 | 符号 | 描述 |
|--------|------|------|
| `{unicode:north}` | ⬆ | 北 |
| `{unicode:south}` | ⬇ | 南 |
| `{unicode:east}` | ➡ | 东 |
| `{unicode:west}` | ⬅ | 西 |

### ⚔️ 游戏相关符号
| 占位符 | 符号 | 描述 |
|--------|------|------|
| `{unicode:sword}` | ⚔ | 剑 |
| `{unicode:shield}` | 🛡 | 盾牌 |
| `{unicode:bow}` | 🏹 | 弓箭 |
| `{unicode:pickaxe}` | ⛏ | 镐子 |
| `{unicode:hammer}` | 🔨 | 锤子 |

### 🌤️ 天气符号
| 占位符 | 符号 | 描述 |
|--------|------|------|
| `{unicode:sun}` | ☀ | 太阳 |
| `{unicode:cloud}` | ☁ | 云 |
| `{unicode:rain}` | ☔ | 雨 |
| `{unicode:snow}` | ❄ | 雪花 |
| `{unicode:lightning}` | ⚡ | 闪电 |

### 🔢 数学符号
| 占位符 | 符号 | 描述 |
|--------|------|------|
| `{unicode:infinity}` | ∞ | 无穷大 |
| `{unicode:plus_minus}` | ± | 正负号 |
| `{unicode:multiply}` | × | 乘号 |
| `{unicode:divide}` | ÷ | 除号 |
| `{unicode:not_equal}` | ≠ | 不等号 |
| `{unicode:less_equal}` | ≤ | 小于等于 |
| `{unicode:greater_equal}` | ≥ | 大于等于 |

## 🎮 实际应用示例

### 经济系统消息
```yaml
messages:
  join:
    messages:
      - "&a欢迎回来！ {unicode:smile}"
      - "&7你的余额: &e{unicode:yen}%vault_balance%"
      - "&7商店位置: {unicode:arrow_right} 传送门"
```

### 游戏指引消息
```yaml
messages:
  first_join:
    messages:
      - "&6{unicode:star} 新手指南 {unicode:star}"
      - "&e{unicode:sword} PvP区域: {unicode:north} 北方战场"
      - "&e{unicode:pickaxe} 挖矿世界: {unicode:arrow_down} 地下城"
      - "&e{unicode:shield} 安全区域: {unicode:heart} 主城"
```

### 天气系统消息
```yaml
messages:
  weather_update:
    messages:
      - "&7当前天气: {unicode:sun} 晴朗"
      - "&7温度: 25°C {unicode:arrow_up}"
      - "&7湿度: 60% {unicode:cloud}"
```

## 🔧 高级功能

### 自定义Unicode映射
你可以通过插件API添加自定义Unicode映射：

```java
// 添加自定义符号
UnicodeUtil.addUnicodeMapping("custom_symbol", "🎮");

// 在配置中使用
// "游戏模式: {unicode:custom_symbol}"
```

### 十六进制Unicode
对于任何Unicode字符，你都可以使用其十六进制代码：

```yaml
messages:
  special:
    messages:
      - "特殊符号: {u:1F60A}"  # 😊
      - "火箭: {u:1F680}"      # 🚀
      - "彩虹: {u:1F308}"      # 🌈
```

## 📊 处理顺序

Unicode字符的处理顺序如下：
1. **Unicode处理** - 首先处理所有Unicode占位符
2. **变量替换** - 然后处理内置变量（%player_name%等）
3. **PlaceholderAPI** - 最后处理PAPI变量（如果启用）
4. **颜色代码** - 最终处理颜色代码

## 💡 使用技巧

### 1. 组合使用
```yaml
"评分: {unicode:star}{unicode:star}{unicode:star}{unicode:star}{unicode:star_empty}"
# 显示: 评分: ★★★★☆
```

### 2. 与颜色代码结合
```yaml
"&6{unicode:star} &e黄金会员 &6{unicode:star}"
# 显示: 金色的 ★ 黄金会员 ★
```

### 3. 在不同消息类型中使用
```yaml
console:
  format:
    join: "[MessageTools] {unicode:arrow_right} %player_name% 加入"
    quit: "[MessageTools] {unicode:arrow_left} %player_name% 离开"
```

## 🔍 故障排除

### 问题1：Unicode字符不显示
**原因**：客户端字体不支持该Unicode字符
**解决**：使用更常见的Unicode字符，或提供字体包

### 问题2：占位符没有被替换
**检查**：
1. 占位符格式是否正确：`{unicode:name}` 或 `{u:XXXX}`
2. Unicode名称是否存在（区分大小写）
3. 十六进制代码是否有效

### 问题3：显示为方块或问号
**原因**：字符编码问题
**解决**：确保配置文件使用UTF-8编码保存

## 📋 配置文件示例

查看 `unicode-config.yml` 获取完整的Unicode使用示例，包括：
- 各种Unicode符号的使用方法
- 实际的消息配置示例
- 最佳实践和技巧

---

通过Unicode字符支持，MessageTools现在可以创造更加丰富、专业和有趣的服务器消息体验！
