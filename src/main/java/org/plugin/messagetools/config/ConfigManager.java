package org.plugin.messagetools.config;

import org.yaml.snakeyaml.Yaml;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * 配置文件管理器
 * 负责加载、保存和管理插件配置
 */
public class ConfigManager {

    private final Path dataDirectory;
    private final Logger logger;
    private Map<String, Object> config;
    private final Yaml yaml;

    public ConfigManager(Path dataDirectory, Logger logger) {
        this.dataDirectory = dataDirectory;
        this.logger = logger;
        this.yaml = new Yaml();
    }

    /**
     * 加载配置文件
     */
    public void loadConfig() {
        try {
            // 确保数据目录存在
            if (!Files.exists(dataDirectory)) {
                Files.createDirectories(dataDirectory);
            }

            Path configFile = dataDirectory.resolve("config.yml");

            // 如果配置文件不存在，从资源中复制默认配置
            if (!Files.exists(configFile)) {
                copyDefaultConfig(configFile);
            }

            // 加载配置文件
            try (InputStream inputStream = Files.newInputStream(configFile)) {
                config = yaml.load(inputStream);
                logger.info("配置文件加载成功");
            }

        } catch (IOException e) {
            logger.error("加载配置文件时发生错误", e);
            // 使用默认配置
            loadDefaultConfig();
        }
    }

    /**
     * 从资源文件复制默认配置
     */
    private void copyDefaultConfig(Path configFile) throws IOException {
        try (InputStream defaultConfig = getClass().getClassLoader().getResourceAsStream("config.yml")) {
            if (defaultConfig != null) {
                Files.copy(defaultConfig, configFile);
                logger.info("已创建默认配置文件");
            } else {
                logger.warn("无法找到默认配置文件，将使用内置默认配置");
                loadDefaultConfig();
            }
        }
    }

    /**
     * 加载内置默认配置
     */
    private void loadDefaultConfig() {
        try (InputStream defaultConfig = getClass().getClassLoader().getResourceAsStream("config.yml")) {
            if (defaultConfig != null) {
                config = yaml.load(defaultConfig);
            }
        } catch (IOException e) {
            logger.error("加载默认配置时发生错误", e);
        }
    }

    /**
     * 保存配置文件
     */
    public void saveConfig() {
        try {
            Path configFile = dataDirectory.resolve("config.yml");
            try (FileWriter writer = new FileWriter(configFile.toFile())) {
                yaml.dump(config, writer);
                logger.info("配置文件保存成功");
            }
        } catch (IOException e) {
            logger.error("保存配置文件时发生错误", e);
        }
    }

    /**
     * 获取配置值
     */
    @SuppressWarnings("unchecked")
    public <T> T getConfig(String path, T defaultValue) {
        if (config == null) {
            return defaultValue;
        }

        String[] keys = path.split("\\.");
        Object current = config;

        for (String key : keys) {
            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(key);
                if (current == null) {
                    return defaultValue;
                }
            } else {
                return defaultValue;
            }
        }

        try {
            return (T) current;
        } catch (ClassCastException e) {
            logger.warn("配置项 {} 的类型不匹配，使用默认值", path);
            return defaultValue;
        }
    }

    /**
     * 获取字符串配置
     */
    public String getString(String path, String defaultValue) {
        return getConfig(path, defaultValue);
    }

    /**
     * 获取布尔配置
     */
    public boolean getBoolean(String path, boolean defaultValue) {
        return getConfig(path, defaultValue);
    }

    /**
     * 获取整数配置
     */
    public int getInt(String path, int defaultValue) {
        Object value = getConfig(path, null);
        if (value == null) {
            return defaultValue;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            logger.warn("配置项 {} 无法转换为整数，使用默认值: {}", path, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 获取长整数配置
     */
    public long getLong(String path, long defaultValue) {
        Object value = getConfig(path, null);
        if (value == null) {
            return defaultValue;
        }

        if (value instanceof Number) {
            return ((Number) value).longValue();
        }

        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            logger.warn("配置项 {} 无法转换为长整数，使用默认值: {}", path, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 获取字符串列表配置
     */
    public List<String> getStringList(String path) {
        return getConfig(path, List.of());
    }

    /**
     * 重新加载配置
     */
    public void reloadConfig() {
        loadConfig();
    }
}
