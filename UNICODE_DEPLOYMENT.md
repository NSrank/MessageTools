# MessageTools v1.7 - Unicode字符支持部署指南

## 🌟 新功能亮点

### Unicode字符支持
- **货币符号** - ￥、₦、€、$、£等，完美支持经济插件
- **特殊符号** - ★、♥、⚔、🛡等，让消息更加生动
- **箭头符号** - →、←、↑、↓等，清晰的方向指示
- **表情符号** - ☺、☹、✓、✗等，丰富的情感表达
- **游戏符号** - ⚔、🛡、⛏、🏹等，专为Minecraft设计

## 📦 部署步骤

### 1. 使用最新插件文件
```
target/MessageTools-1.7-SNAPSHOT.jar
```

### 2. 选择配置文件
根据需求选择合适的配置：

- **`unicode-config.yml`** - 完整的Unicode功能演示
- **`unified-delay-config.yml`** - 统一延迟配置
- **`simple-config.yml`** - 简化配置

### 3. 配置Unicode字符
在配置文件中使用Unicode占位符：

```yaml
messages:
  join:
    enabled: true
    tick: 20
    messages:
      - "&a[+] &f%player_name% &7加入了服务器 {unicode:arrow_right}"
      - "&7欢迎回来！ {unicode:smile}"
      - "&7你的余额：&e{unicode:yen}%vault_balance%"
```

## 🎯 Unicode使用指南

### 基础语法

#### 1. 命名Unicode占位符
```yaml
# 格式：{unicode:name}
"你的余额: {unicode:yen}1000"     # → 你的余额: ￥1000
"评分: {unicode:star}"           # → 评分: ★
"方向: {unicode:arrow_right}"    # → 方向: →
```

#### 2. 十六进制Unicode占位符
```yaml
# 格式：{u:XXXX}
"货币: {u:FFE5}100"              # → 货币: ￥100
"星星: {u:2605}"                 # → 星星: ★
"红心: {u:2665}"                 # → 红心: ♥
```

### 常用Unicode符号速查

#### 💰 经济系统
```yaml
"余额: {unicode:yen}%balance%"           # ￥
"商店: {unicode:dollar}价格"             # $
"欧服: {unicode:euro}货币"               # €
"英服: {unicode:pound}金币"              # £
```

#### 🎮 游戏元素
```yaml
"武器: {unicode:sword} 钻石剑"           # ⚔
"防具: {unicode:shield} 附魔盔甲"        # 🛡
"工具: {unicode:pickaxe} 效率镐"         # ⛏
"远程: {unicode:bow} 力量弓"             # 🏹
```

#### 📍 方向指示
```yaml
"传送: {unicode:arrow_right} 主城"       # →
"返回: {unicode:arrow_left} 上一页"       # ←
"上层: {unicode:arrow_up} 二楼"          # ↑
"地下: {unicode:arrow_down} 矿洞"        # ↓
```

#### ⭐ 评级系统
```yaml
"评分: {unicode:star}{unicode:star}{unicode:star}{unicode:star}{unicode:star_empty}"
# 显示: 评分: ★★★★☆
```

## 🎨 实际应用场景

### 1. 经济插件集成
```yaml
messages:
  join:
    messages:
      - "&a欢迎回来，%player_name%！ {unicode:smile}"
      - "&7你的余额：&e{unicode:yen}%vault_balance%"
      - "&7银行存款：&b{unicode:dollar}%vault_bank_balance%"
      - "&7商店位置：{unicode:arrow_right} /warp shop"
```

### 2. 等级系统显示
```yaml
messages:
  level_up:
    messages:
      - "&6{unicode:star} 恭喜升级！ {unicode:star}"
      - "&e等级：&f%player_level% {unicode:arrow_up}"
      - "&e经验：&f%player_exp% {unicode:infinity}"
      - "&e技能点：&f+5 {unicode:plus_minus}"
```

### 3. PvP系统提示
```yaml
messages:
  pvp_zone:
    messages:
      - "&c{unicode:warning} 进入PvP区域 {unicode:warning}"
      - "&7武器：{unicode:sword} 已启用"
      - "&7护甲：{unicode:shield} 建议穿戴"
      - "&7方向：{unicode:north} 北方战场"
```

### 4. 天气系统通知
```yaml
messages:
  weather:
    messages:
      - "&7天气更新 {unicode:info}"
      - "&e当前：{unicode:sun} 晴朗"
      - "&7温度：25°C {unicode:arrow_up}"
      - "&7能见度：{unicode:check} 良好"
```

## 🔧 高级配置技巧

### 1. 创建主题消息
```yaml
# 中世纪主题
medieval_theme:
  messages:
    - "&6{unicode:sword} 欢迎来到中世纪王国 {unicode:shield}"
    - "&e国王：{unicode:star} 陛下在线"
    - "&7城堡：{unicode:arrow_north} 北方"
    - "&7市场：{unicode:arrow_east} 东方"

# 现代主题  
modern_theme:
  messages:
    - "&b{unicode:info} 欢迎来到现代都市 {unicode:info}"
    - "&a银行：{unicode:dollar} 24小时营业"
    - "&e地铁：{unicode:arrow_right} 快速交通"
    - "&7天气：{unicode:sun} 今日晴朗"
```

### 2. 多语言支持
```yaml
# 中文服务器
chinese_server:
  currency: "{unicode:yen}"      # ￥
  welcome: "{unicode:smile}"     # ☺
  
# 欧洲服务器
european_server:
  currency: "{unicode:euro}"     # €
  welcome: "{unicode:heart}"     # ♥
```

### 3. 动态内容生成
```yaml
# 根据时间显示不同符号
time_based:
  morning: "{unicode:sun} 早上好"      # ☀
  afternoon: "{unicode:cloud} 下午好"   # ☁
  evening: "{unicode:star} 晚上好"      # ★
  night: "{unicode:moon} 晚安"          # 🌙
```

## 📊 性能和兼容性

### 性能影响
- **处理开销**：极小，Unicode处理在变量替换之前进行
- **内存使用**：增加约50KB（Unicode映射表）
- **网络传输**：Unicode字符通常比文字描述更短

### 客户端兼容性
- **Java版**：完全支持所有Unicode字符
- **基岩版**：支持大部分Unicode字符
- **字体支持**：依赖客户端字体，建议使用常见符号

### 编码要求
- **配置文件**：必须使用UTF-8编码保存
- **控制台**：确保控制台支持Unicode显示
- **数据库**：如果存储消息，确保数据库支持UTF-8

## 🔍 故障排除

### 问题1：Unicode字符显示为方块
**原因**：客户端字体不支持该字符
**解决**：
1. 使用更常见的Unicode字符
2. 提供客户端字体包
3. 检查客户端语言设置

### 问题2：占位符没有被替换
**检查**：
1. 格式是否正确：`{unicode:name}` 或 `{u:XXXX}`
2. 名称是否存在（不区分大小写）
3. 十六进制代码是否有效

### 问题3：配置文件乱码
**解决**：
1. 确保使用UTF-8编码保存配置文件
2. 避免使用记事本编辑，推荐VSCode或Notepad++
3. 检查服务器系统编码设置

## 🎮 测试和验证

### 1. 基础测试
```yaml
test_config:
  messages:
    - "货币测试: {unicode:yen} {unicode:euro} {unicode:dollar}"
    - "符号测试: {unicode:star} {unicode:heart} {unicode:smile}"
    - "箭头测试: {unicode:arrow_right} {unicode:arrow_left}"
```

### 2. 十六进制测试
```yaml
hex_test:
  messages:
    - "十六进制: {u:FFE5} {u:2605} {u:2665}"
```

### 3. 混合测试
```yaml
mixed_test:
  messages:
    - "混合使用: {unicode:yen}100 {u:2605} &a颜色 %player_name%"
```

## 📋 部署检查清单

### 部署前
- [ ] 备份现有配置文件
- [ ] 确认配置文件使用UTF-8编码
- [ ] 测试Unicode字符在客户端的显示效果
- [ ] 选择合适的Unicode符号

### 部署后
- [ ] 验证插件正常加载
- [ ] 测试Unicode字符正确显示
- [ ] 检查不同客户端的兼容性
- [ ] 确认性能表现正常

### 优化调整
- [ ] 根据玩家反馈调整Unicode使用
- [ ] 优化符号选择和搭配
- [ ] 完善主题化设计
- [ ] 更新文档和说明

---

通过Unicode字符支持，MessageTools现在可以创造更加丰富、专业和有趣的服务器消息体验！从简单的货币符号到复杂的主题化设计，让你的服务器消息脱颖而出！
