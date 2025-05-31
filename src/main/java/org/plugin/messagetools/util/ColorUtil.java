package org.plugin.messagetools.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.regex.Pattern;

/**
 * 颜色代码处理工具类
 * 负责处理原版颜色代码和格式代码
 */
public class ColorUtil {
    
    // 颜色代码映射
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = 
        LegacyComponentSerializer.legacyAmpersand();
    
    // 颜色代码正则表达式
    private static final Pattern COLOR_PATTERN = Pattern.compile("&[0-9a-fk-or]", Pattern.CASE_INSENSITIVE);
    
    /**
     * 将包含颜色代码的字符串转换为Adventure Component
     * 
     * @param text 包含颜色代码的文本
     * @return 转换后的Component
     */
    public static Component parseColorCodes(String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }
        
        return LEGACY_SERIALIZER.deserialize(text);
    }
    
    /**
     * 移除文本中的所有颜色代码
     * 
     * @param text 包含颜色代码的文本
     * @return 移除颜色代码后的纯文本
     */
    public static String stripColorCodes(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        return COLOR_PATTERN.matcher(text).replaceAll("");
    }
    
    /**
     * 检查文本是否包含颜色代码
     * 
     * @param text 要检查的文本
     * @return 如果包含颜色代码返回true
     */
    public static boolean hasColorCodes(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        
        return COLOR_PATTERN.matcher(text).find();
    }
    
    /**
     * 将Component转换为带颜色代码的字符串
     * 
     * @param component 要转换的Component
     * @return 带颜色代码的字符串
     */
    public static String componentToString(Component component) {
        if (component == null) {
            return "";
        }
        
        return LEGACY_SERIALIZER.serialize(component);
    }
    
    /**
     * 创建带颜色的文本组件
     * 
     * @param text 文本内容
     * @param color 颜色
     * @return 带颜色的Component
     */
    public static Component createColoredText(String text, NamedTextColor color) {
        return Component.text(text).color(color);
    }
    
    /**
     * 创建带格式的文本组件
     * 
     * @param text 文本内容
     * @param color 颜色
     * @param decorations 格式装饰
     * @return 带格式的Component
     */
    public static Component createFormattedText(String text, NamedTextColor color, TextDecoration... decorations) {
        Component component = Component.text(text).color(color);
        
        for (TextDecoration decoration : decorations) {
            component = component.decorate(decoration);
        }
        
        return component;
    }
    
    /**
     * 获取颜色代码对应的NamedTextColor
     * 
     * @param colorCode 颜色代码 (如 'a', 'b', 'c' 等)
     * @return 对应的NamedTextColor，如果无效则返回WHITE
     */
    public static NamedTextColor getColorFromCode(char colorCode) {
        return switch (Character.toLowerCase(colorCode)) {
            case '0' -> NamedTextColor.BLACK;
            case '1' -> NamedTextColor.DARK_BLUE;
            case '2' -> NamedTextColor.DARK_GREEN;
            case '3' -> NamedTextColor.DARK_AQUA;
            case '4' -> NamedTextColor.DARK_RED;
            case '5' -> NamedTextColor.DARK_PURPLE;
            case '6' -> NamedTextColor.GOLD;
            case '7' -> NamedTextColor.GRAY;
            case '8' -> NamedTextColor.DARK_GRAY;
            case '9' -> NamedTextColor.BLUE;
            case 'a' -> NamedTextColor.GREEN;
            case 'b' -> NamedTextColor.AQUA;
            case 'c' -> NamedTextColor.RED;
            case 'd' -> NamedTextColor.LIGHT_PURPLE;
            case 'e' -> NamedTextColor.YELLOW;
            case 'f' -> NamedTextColor.WHITE;
            default -> NamedTextColor.WHITE;
        };
    }
    
    /**
     * 验证颜色代码是否有效
     * 
     * @param colorCode 要验证的颜色代码
     * @return 如果有效返回true
     */
    public static boolean isValidColorCode(char colorCode) {
        char lowerCode = Character.toLowerCase(colorCode);
        return (lowerCode >= '0' && lowerCode <= '9') || 
               (lowerCode >= 'a' && lowerCode <= 'f') ||
               lowerCode == 'k' || lowerCode == 'l' || lowerCode == 'm' ||
               lowerCode == 'n' || lowerCode == 'o' || lowerCode == 'r';
    }
}
