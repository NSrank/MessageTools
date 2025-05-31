package org.plugin.messagetools.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unicode字符处理工具类
 * 负责处理Unicode字符的转换和替换
 */
public class UnicodeUtil {
    
    // Unicode字符映射表
    private static final Map<String, String> UNICODE_MAP = new HashMap<>();
    
    // Unicode占位符正则表达式 - 匹配 {unicode:name} 格式
    private static final Pattern UNICODE_PATTERN = Pattern.compile("\\{unicode:([^}]+)\\}");
    
    // 十六进制Unicode正则表达式 - 匹配 {u:XXXX} 格式
    private static final Pattern HEX_UNICODE_PATTERN = Pattern.compile("\\{u:([0-9a-fA-F]+)\\}");
    
    static {
        initializeUnicodeMap();
    }
    
    /**
     * 初始化Unicode字符映射表
     */
    private static void initializeUnicodeMap() {
        // 货币符号
        UNICODE_MAP.put("yen", "￥");           // 日元符号
        UNICODE_MAP.put("yuan", "￥");          // 人民币符号
        UNICODE_MAP.put("naira", "₦");         // 奈拉符号
        UNICODE_MAP.put("euro", "€");          // 欧元符号
        UNICODE_MAP.put("pound", "£");         // 英镑符号
        UNICODE_MAP.put("dollar", "$");        // 美元符号
        UNICODE_MAP.put("cent", "¢");          // 美分符号
        UNICODE_MAP.put("won", "₩");           // 韩元符号
        UNICODE_MAP.put("rupee", "₹");         // 卢比符号
        UNICODE_MAP.put("ruble", "₽");         // 卢布符号
        
        // 箭头符号
        UNICODE_MAP.put("arrow_right", "→");   // 右箭头
        UNICODE_MAP.put("arrow_left", "←");    // 左箭头
        UNICODE_MAP.put("arrow_up", "↑");      // 上箭头
        UNICODE_MAP.put("arrow_down", "↓");    // 下箭头
        UNICODE_MAP.put("arrow_double_right", "⇒"); // 双右箭头
        UNICODE_MAP.put("arrow_double_left", "⇐");  // 双左箭头
        
        // 数学符号
        UNICODE_MAP.put("infinity", "∞");      // 无穷大
        UNICODE_MAP.put("plus_minus", "±");    // 正负号
        UNICODE_MAP.put("multiply", "×");      // 乘号
        UNICODE_MAP.put("divide", "÷");        // 除号
        UNICODE_MAP.put("not_equal", "≠");     // 不等号
        UNICODE_MAP.put("less_equal", "≤");    // 小于等于
        UNICODE_MAP.put("greater_equal", "≥"); // 大于等于
        
        // 特殊符号
        UNICODE_MAP.put("star", "★");          // 实心星星
        UNICODE_MAP.put("star_empty", "☆");    // 空心星星
        UNICODE_MAP.put("heart", "♥");         // 红心
        UNICODE_MAP.put("heart_empty", "♡");   // 空心红心
        UNICODE_MAP.put("diamond", "♦");       // 钻石
        UNICODE_MAP.put("club", "♣");          // 梅花
        UNICODE_MAP.put("spade", "♠");         // 黑桃
        UNICODE_MAP.put("music", "♪");         // 音符
        UNICODE_MAP.put("note", "♫");          // 音符
        
        // 表情符号
        UNICODE_MAP.put("smile", "☺");         // 笑脸
        UNICODE_MAP.put("frown", "☹");         // 哭脸
        UNICODE_MAP.put("check", "✓");         // 对勾
        UNICODE_MAP.put("cross", "✗");         // 叉号
        UNICODE_MAP.put("warning", "⚠");       // 警告
        UNICODE_MAP.put("info", "ℹ");          // 信息
        
        // 方向符号
        UNICODE_MAP.put("north", "⬆");         // 北
        UNICODE_MAP.put("south", "⬇");         // 南
        UNICODE_MAP.put("east", "➡");          // 东
        UNICODE_MAP.put("west", "⬅");          // 西
        
        // 游戏相关符号
        UNICODE_MAP.put("sword", "⚔");         // 剑
        UNICODE_MAP.put("shield", "🛡");        // 盾牌
        UNICODE_MAP.put("bow", "🏹");           // 弓箭
        UNICODE_MAP.put("pickaxe", "⛏");       // 镐子
        UNICODE_MAP.put("hammer", "🔨");        // 锤子
        
        // 天气符号
        UNICODE_MAP.put("sun", "☀");           // 太阳
        UNICODE_MAP.put("cloud", "☁");         // 云
        UNICODE_MAP.put("rain", "☔");          // 雨
        UNICODE_MAP.put("snow", "❄");          // 雪花
        UNICODE_MAP.put("lightning", "⚡");     // 闪电
    }
    
    /**
     * 处理文本中的Unicode占位符
     * 
     * @param text 包含Unicode占位符的文本
     * @return 替换后的文本
     */
    public static String processUnicode(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        String result = text;
        
        // 处理命名Unicode占位符 {unicode:name}
        result = processNamedUnicode(result);
        
        // 处理十六进制Unicode占位符 {u:XXXX}
        result = processHexUnicode(result);
        
        return result;
    }
    
    /**
     * 处理命名Unicode占位符
     * 
     * @param text 包含命名Unicode占位符的文本
     * @return 替换后的文本
     */
    private static String processNamedUnicode(String text) {
        Matcher matcher = UNICODE_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();
        
        while (matcher.find()) {
            String unicodeName = matcher.group(1).toLowerCase();
            String unicodeChar = UNICODE_MAP.get(unicodeName);
            
            if (unicodeChar != null) {
                matcher.appendReplacement(result, Matcher.quoteReplacement(unicodeChar));
            } else {
                // 如果找不到对应的Unicode字符，保留原始占位符
                matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group(0)));
            }
        }
        matcher.appendTail(result);
        
        return result.toString();
    }
    
    /**
     * 处理十六进制Unicode占位符
     * 
     * @param text 包含十六进制Unicode占位符的文本
     * @return 替换后的文本
     */
    private static String processHexUnicode(String text) {
        Matcher matcher = HEX_UNICODE_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();
        
        while (matcher.find()) {
            String hexCode = matcher.group(1);
            
            try {
                int codePoint = Integer.parseInt(hexCode, 16);
                String unicodeChar = new String(Character.toChars(codePoint));
                matcher.appendReplacement(result, Matcher.quoteReplacement(unicodeChar));
            } catch (IllegalArgumentException e) {
                // 如果十六进制代码无效，保留原始占位符
                matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group(0)));
            }
        }
        matcher.appendTail(result);
        
        return result.toString();
    }
    
    /**
     * 添加自定义Unicode映射
     * 
     * @param name Unicode名称
     * @param character Unicode字符
     */
    public static void addUnicodeMapping(String name, String character) {
        UNICODE_MAP.put(name.toLowerCase(), character);
    }
    
    /**
     * 移除Unicode映射
     * 
     * @param name Unicode名称
     */
    public static void removeUnicodeMapping(String name) {
        UNICODE_MAP.remove(name.toLowerCase());
    }
    
    /**
     * 获取所有可用的Unicode映射
     * 
     * @return Unicode映射的副本
     */
    public static Map<String, String> getAvailableUnicode() {
        return new HashMap<>(UNICODE_MAP);
    }
    
    /**
     * 检查是否存在指定名称的Unicode映射
     * 
     * @param name Unicode名称
     * @return 如果存在返回true
     */
    public static boolean hasUnicodeMapping(String name) {
        return UNICODE_MAP.containsKey(name.toLowerCase());
    }
    
    /**
     * 获取指定名称的Unicode字符
     * 
     * @param name Unicode名称
     * @return Unicode字符，如果不存在返回null
     */
    public static String getUnicodeCharacter(String name) {
        return UNICODE_MAP.get(name.toLowerCase());
    }
    
    /**
     * 检查文本是否包含Unicode占位符
     * 
     * @param text 要检查的文本
     * @return 如果包含Unicode占位符返回true
     */
    public static boolean hasUnicodePlaceholders(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        
        return UNICODE_PATTERN.matcher(text).find() || HEX_UNICODE_PATTERN.matcher(text).find();
    }
    
    /**
     * 将Unicode字符转换为十六进制代码
     * 
     * @param character Unicode字符
     * @return 十六进制代码字符串
     */
    public static String unicodeToHex(String character) {
        if (character == null || character.isEmpty()) {
            return "";
        }
        
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < character.length(); i++) {
            int codePoint = character.codePointAt(i);
            hex.append(String.format("%04X", codePoint));
            if (Character.isSupplementaryCodePoint(codePoint)) {
                i++; // 跳过代理对的第二个字符
            }
        }
        
        return hex.toString();
    }
}
