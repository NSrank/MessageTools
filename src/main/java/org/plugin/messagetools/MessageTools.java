package org.plugin.messagetools;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.plugin.messagetools.config.ConfigManager;
import org.plugin.messagetools.data.PlayerDataManager;
import org.plugin.messagetools.listener.PlayerEventListener;
import org.plugin.messagetools.service.MessageService;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/**
 * MessageTools 主类
 * 一个支持自定义加入/退出消息的Velocity插件，集成PlaceholderAPI支持
 */
@Plugin(
        id = "messagetools",
        name = "MessageTools",
        version = "1.6-SNAPSHOT",
        description = "A Velocity plugin for customizable join/quit messages with PlaceholderAPI support",
        authors = {"NSrank & Augment"}
)
public class MessageTools {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;

    private ConfigManager configManager;
    private PlayerDataManager playerDataManager;
    private MessageService messageService;
    private PlayerEventListener playerEventListener;

    @Inject
    public MessageTools(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("MessageTools 插件正在启动...");

        try {
            // 初始化配置管理器
            configManager = new ConfigManager(dataDirectory, logger);
            configManager.loadConfig();

            // 初始化玩家数据管理器
            String playerDataFile = configManager.getString("storage.player_data_file", "playerdata.yml");
            playerDataManager = new PlayerDataManager(dataDirectory, logger, playerDataFile);
            playerDataManager.loadPlayerData();

            // 初始化消息服务
            messageService = new MessageService(server, configManager, logger, this);

            // 初始化事件监听器
            playerEventListener = new PlayerEventListener(messageService, playerDataManager, logger);

            // 注册事件监听器
            server.getEventManager().register(this, playerEventListener);
            logger.info("事件监听器已注册");

            // 启动自动保存任务
            startAutoSaveTask();

            logger.info("===================================");
            logger.info("MessageTools v1.6 已加载");
            logger.info("作者：NSrank & Augment");
            logger.info("===================================");

            // 只在调试模式下显示详细配置信息
            boolean debugEnabled = configManager.getBoolean("debug.enabled", false);
            boolean verboseConfig = configManager.getBoolean("debug.verbose_config", false);

            if (debugEnabled && verboseConfig) {
                logger.info("当前配置 - 加入消息启用: {}", configManager.getBoolean("messages.join.enabled", true));
                logger.info("当前配置 - 私人消息启用: {}", configManager.getBoolean("messages.message.enabled", true));
                logger.info("当前配置 - 控制台输出启用: {}", configManager.getBoolean("console.enabled", true));
                logger.info("当前配置 - 调试模式启用: {}", debugEnabled);
            }

        } catch (Exception e) {
            logger.error("MessageTools 插件启动失败", e);
        }
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        logger.info("MessageTools 插件正在关闭...");

        try {
            // 保存玩家数据
            if (playerDataManager != null) {
                playerDataManager.savePlayerData();
            }

            // 保存配置文件
            if (configManager != null) {
                configManager.saveConfig();
            }

            logger.info("MessageTools 插件关闭完成");

        } catch (Exception e) {
            logger.error("MessageTools 插件关闭时发生错误", e);
        }
    }

    /**
     * 启动自动保存任务
     */
    private void startAutoSaveTask() {
        boolean autoSave = configManager.getBoolean("storage.auto_save", true);
        if (!autoSave) {
            return;
        }

        int interval = configManager.getInt("storage.auto_save_interval", 5);

        server.getScheduler().buildTask(this, () -> {
            try {
                playerDataManager.savePlayerData();
                logger.debug("自动保存玩家数据完成");
            } catch (Exception e) {
                logger.error("自动保存玩家数据时发生错误", e);
            }
        }).repeat(interval, TimeUnit.MINUTES).schedule();

        logger.info("自动保存任务已启动，间隔: {} 分钟", interval);
    }

    /**
     * 获取配置管理器
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * 获取玩家数据管理器
     */
    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    /**
     * 获取消息服务
     */
    public MessageService getMessageService() {
        return messageService;
    }
}
