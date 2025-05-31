package org.plugin.messagetools.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ColorUtil 工具类测试
 */
public class ColorUtilTest {
    
    @Test
    public void testParseColorCodes() {
        // 测试基本颜色代码解析
        String input = "&aHello &bWorld";
        Component result = ColorUtil.parseColorCodes(input);
        assertNotNull(result);
        
        // 测试空字符串
        Component empty = ColorUtil.parseColorCodes("");
        assertEquals(Component.empty(), empty);
        
        // 测试null输入
        Component nullResult = ColorUtil.parseColorCodes(null);
        assertEquals(Component.empty(), nullResult);
    }
    
    @Test
    public void testStripColorCodes() {
        // 测试移除颜色代码
        String input = "&aHello &bWorld &c!";
        String expected = "Hello World !";
        String result = ColorUtil.stripColorCodes(input);
        assertEquals(expected, result);
        
        // 测试没有颜色代码的文本
        String plain = "Hello World";
        assertEquals(plain, ColorUtil.stripColorCodes(plain));
        
        // 测试空字符串
        assertEquals("", ColorUtil.stripColorCodes(""));
        
        // 测试null
        assertNull(ColorUtil.stripColorCodes(null));
    }
    
    @Test
    public void testHasColorCodes() {
        // 测试包含颜色代码的文本
        assertTrue(ColorUtil.hasColorCodes("&aHello"));
        assertTrue(ColorUtil.hasColorCodes("Hello &bWorld"));
        assertTrue(ColorUtil.hasColorCodes("&l&nBold and underlined"));
        
        // 测试不包含颜色代码的文本
        assertFalse(ColorUtil.hasColorCodes("Hello World"));
        assertFalse(ColorUtil.hasColorCodes(""));
        assertFalse(ColorUtil.hasColorCodes(null));
    }
    
    @Test
    public void testGetColorFromCode() {
        // 测试有效的颜色代码
        assertEquals(NamedTextColor.BLACK, ColorUtil.getColorFromCode('0'));
        assertEquals(NamedTextColor.DARK_BLUE, ColorUtil.getColorFromCode('1'));
        assertEquals(NamedTextColor.GREEN, ColorUtil.getColorFromCode('a'));
        assertEquals(NamedTextColor.GREEN, ColorUtil.getColorFromCode('A')); // 测试大写
        assertEquals(NamedTextColor.WHITE, ColorUtil.getColorFromCode('f'));
        
        // 测试无效的颜色代码
        assertEquals(NamedTextColor.WHITE, ColorUtil.getColorFromCode('z'));
        assertEquals(NamedTextColor.WHITE, ColorUtil.getColorFromCode('!'));
    }
    
    @Test
    public void testIsValidColorCode() {
        // 测试有效的颜色代码
        assertTrue(ColorUtil.isValidColorCode('0'));
        assertTrue(ColorUtil.isValidColorCode('9'));
        assertTrue(ColorUtil.isValidColorCode('a'));
        assertTrue(ColorUtil.isValidColorCode('f'));
        assertTrue(ColorUtil.isValidColorCode('A')); // 大写
        assertTrue(ColorUtil.isValidColorCode('F')); // 大写
        
        // 测试有效的格式代码
        assertTrue(ColorUtil.isValidColorCode('k'));
        assertTrue(ColorUtil.isValidColorCode('l'));
        assertTrue(ColorUtil.isValidColorCode('m'));
        assertTrue(ColorUtil.isValidColorCode('n'));
        assertTrue(ColorUtil.isValidColorCode('o'));
        assertTrue(ColorUtil.isValidColorCode('r'));
        
        // 测试无效的代码
        assertFalse(ColorUtil.isValidColorCode('g'));
        assertFalse(ColorUtil.isValidColorCode('z'));
        assertFalse(ColorUtil.isValidColorCode('!'));
        assertFalse(ColorUtil.isValidColorCode(' '));
    }
    
    @Test
    public void testCreateColoredText() {
        Component result = ColorUtil.createColoredText("Hello", NamedTextColor.RED);
        assertNotNull(result);
        assertEquals("Hello", result.children().isEmpty() ? 
            ((net.kyori.adventure.text.TextComponent) result).content() : "");
    }
    
    @Test
    public void testComponentToString() {
        Component component = Component.text("Hello").color(NamedTextColor.RED);
        String result = ColorUtil.componentToString(component);
        assertNotNull(result);
        assertTrue(result.contains("Hello"));
        
        // 测试null组件
        assertEquals("", ColorUtil.componentToString(null));
    }
}
