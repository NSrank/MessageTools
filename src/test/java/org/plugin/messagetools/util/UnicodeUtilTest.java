package org.plugin.messagetools.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * UnicodeUtil测试类
 */
public class UnicodeUtilTest {
    
    @Test
    public void testProcessNamedUnicode() {
        // 测试货币符号
        assertEquals("价格: ￥100", UnicodeUtil.processUnicode("价格: {unicode:yen}100"));
        assertEquals("价格: ￥100", UnicodeUtil.processUnicode("价格: {unicode:yuan}100"));
        assertEquals("价格: €50", UnicodeUtil.processUnicode("价格: {unicode:euro}50"));
        assertEquals("价格: $25", UnicodeUtil.processUnicode("价格: {unicode:dollar}25"));
        assertEquals("价格: ₦200", UnicodeUtil.processUnicode("价格: {unicode:naira}200"));
        
        // 测试箭头符号
        assertEquals("方向 →", UnicodeUtil.processUnicode("方向 {unicode:arrow_right}"));
        assertEquals("方向 ←", UnicodeUtil.processUnicode("方向 {unicode:arrow_left}"));
        assertEquals("方向 ↑", UnicodeUtil.processUnicode("方向 {unicode:arrow_up}"));
        assertEquals("方向 ↓", UnicodeUtil.processUnicode("方向 {unicode:arrow_down}"));
        
        // 测试特殊符号
        assertEquals("评分 ★★★★★", UnicodeUtil.processUnicode("评分 {unicode:star}{unicode:star}{unicode:star}{unicode:star}{unicode:star}"));
        assertEquals("喜欢 ♥", UnicodeUtil.processUnicode("喜欢 {unicode:heart}"));
        assertEquals("音乐 ♪", UnicodeUtil.processUnicode("音乐 {unicode:music}"));
    }
    
    @Test
    public void testProcessHexUnicode() {
        // 测试十六进制Unicode
        assertEquals("￥", UnicodeUtil.processUnicode("{u:FFE5}"));  // 日元符号
        assertEquals("€", UnicodeUtil.processUnicode("{u:20AC}"));   // 欧元符号
        assertEquals("★", UnicodeUtil.processUnicode("{u:2605}"));   // 实心星星
        assertEquals("♥", UnicodeUtil.processUnicode("{u:2665}"));   // 红心
        
        // 测试组合使用
        assertEquals("价格: ￥100 ★", UnicodeUtil.processUnicode("价格: {u:FFE5}100 {u:2605}"));
    }
    
    @Test
    public void testMixedUnicode() {
        // 测试混合使用命名和十六进制Unicode
        String input = "商店: {unicode:yen}100 {u:2605} 评分: {unicode:star}{unicode:star}{unicode:star}";
        String expected = "商店: ￥100 ★ 评分: ★★★";
        assertEquals(expected, UnicodeUtil.processUnicode(input));
    }
    
    @Test
    public void testInvalidUnicode() {
        // 测试无效的Unicode名称
        assertEquals("价格: {unicode:invalid}100", UnicodeUtil.processUnicode("价格: {unicode:invalid}100"));
        
        // 测试无效的十六进制代码
        assertEquals("符号: {u:INVALID}", UnicodeUtil.processUnicode("符号: {u:INVALID}"));
        assertEquals("符号: {u:GGGG}", UnicodeUtil.processUnicode("符号: {u:GGGG}"));
    }
    
    @Test
    public void testEmptyAndNullInput() {
        // 测试空字符串和null
        assertEquals("", UnicodeUtil.processUnicode(""));
        assertEquals(null, UnicodeUtil.processUnicode(null));
    }
    
    @Test
    public void testNoUnicodePlaceholders() {
        // 测试没有Unicode占位符的文本
        String input = "这是普通文本，没有Unicode占位符";
        assertEquals(input, UnicodeUtil.processUnicode(input));
    }
    
    @Test
    public void testCaseSensitivity() {
        // 测试大小写不敏感
        assertEquals("￥", UnicodeUtil.processUnicode("{unicode:YEN}"));
        assertEquals("￥", UnicodeUtil.processUnicode("{unicode:Yen}"));
        assertEquals("￥", UnicodeUtil.processUnicode("{unicode:yen}"));
        
        // 测试十六进制大小写
        assertEquals("￥", UnicodeUtil.processUnicode("{u:ffe5}"));
        assertEquals("￥", UnicodeUtil.processUnicode("{u:FFE5}"));
        assertEquals("￥", UnicodeUtil.processUnicode("{u:FfE5}"));
    }
    
    @Test
    public void testHasUnicodePlaceholders() {
        // 测试检测Unicode占位符
        assertTrue(UnicodeUtil.hasUnicodePlaceholders("{unicode:yen}"));
        assertTrue(UnicodeUtil.hasUnicodePlaceholders("{u:FFE5}"));
        assertTrue(UnicodeUtil.hasUnicodePlaceholders("文本 {unicode:star} 更多文本"));
        assertTrue(UnicodeUtil.hasUnicodePlaceholders("文本 {u:2605} 更多文本"));
        
        assertFalse(UnicodeUtil.hasUnicodePlaceholders("普通文本"));
        assertFalse(UnicodeUtil.hasUnicodePlaceholders(""));
        assertFalse(UnicodeUtil.hasUnicodePlaceholders(null));
    }
    
    @Test
    public void testUnicodeMapping() {
        // 测试Unicode映射功能
        assertTrue(UnicodeUtil.hasUnicodeMapping("yen"));
        assertTrue(UnicodeUtil.hasUnicodeMapping("euro"));
        assertTrue(UnicodeUtil.hasUnicodeMapping("star"));
        
        assertFalse(UnicodeUtil.hasUnicodeMapping("nonexistent"));
        
        assertEquals("￥", UnicodeUtil.getUnicodeCharacter("yen"));
        assertEquals("€", UnicodeUtil.getUnicodeCharacter("euro"));
        assertEquals("★", UnicodeUtil.getUnicodeCharacter("star"));
        assertNull(UnicodeUtil.getUnicodeCharacter("nonexistent"));
    }
    
    @Test
    public void testCustomUnicodeMapping() {
        // 测试添加自定义Unicode映射
        UnicodeUtil.addUnicodeMapping("custom", "🎮");
        assertTrue(UnicodeUtil.hasUnicodeMapping("custom"));
        assertEquals("🎮", UnicodeUtil.getUnicodeCharacter("custom"));
        assertEquals("游戏 🎮", UnicodeUtil.processUnicode("游戏 {unicode:custom}"));
        
        // 测试移除映射
        UnicodeUtil.removeUnicodeMapping("custom");
        assertFalse(UnicodeUtil.hasUnicodeMapping("custom"));
        assertNull(UnicodeUtil.getUnicodeCharacter("custom"));
    }
    
    @Test
    public void testUnicodeToHex() {
        // 测试Unicode字符转十六进制
        assertEquals("FFE5", UnicodeUtil.unicodeToHex("￥"));
        assertEquals("20AC", UnicodeUtil.unicodeToHex("€"));
        assertEquals("2605", UnicodeUtil.unicodeToHex("★"));
        assertEquals("2665", UnicodeUtil.unicodeToHex("♥"));
        
        assertEquals("", UnicodeUtil.unicodeToHex(""));
        assertEquals("", UnicodeUtil.unicodeToHex(null));
    }
    
    @Test
    public void testComplexScenario() {
        // 测试复杂场景
        String input = "欢迎来到服务器！{unicode:star} 你的余额: {unicode:yen}1000 {unicode:heart} " +
                      "方向指示: {unicode:arrow_right} 传送门 {unicode:arrow_left} 商店 " +
                      "特殊符号: {u:2665} {u:2605} 评分: {unicode:star}{unicode:star}{unicode:star}";
        
        String expected = "欢迎来到服务器！★ 你的余额: ￥1000 ♥ " +
                         "方向指示: → 传送门 ← 商店 " +
                         "特殊符号: ♥ ★ 评分: ★★★";
        
        assertEquals(expected, UnicodeUtil.processUnicode(input));
    }
    
    @Test
    public void testGameRelatedUnicode() {
        // 测试游戏相关的Unicode符号
        assertEquals("武器: ⚔", UnicodeUtil.processUnicode("武器: {unicode:sword}"));
        assertEquals("防具: 🛡", UnicodeUtil.processUnicode("防具: {unicode:shield}"));
        assertEquals("工具: ⛏", UnicodeUtil.processUnicode("工具: {unicode:pickaxe}"));
        assertEquals("远程: 🏹", UnicodeUtil.processUnicode("远程: {unicode:bow}"));
    }
    
    @Test
    public void testWeatherUnicode() {
        // 测试天气相关的Unicode符号
        assertEquals("天气: ☀", UnicodeUtil.processUnicode("天气: {unicode:sun}"));
        assertEquals("天气: ☁", UnicodeUtil.processUnicode("天气: {unicode:cloud}"));
        assertEquals("天气: ☔", UnicodeUtil.processUnicode("天气: {unicode:rain}"));
        assertEquals("天气: ❄", UnicodeUtil.processUnicode("天气: {unicode:snow}"));
        assertEquals("天气: ⚡", UnicodeUtil.processUnicode("天气: {unicode:lightning}"));
    }
}
