package org.plugin.messagetools.service;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
// import net.william278.papiproxybridge.api.PlaceholderAPI;
import org.plugin.messagetools.config.ConfigManager;
import org.plugin.messagetools.util.ColorUtil;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 消息处理服务
 * 负责处理和发送各种类型的消息
 */
public class MessageService {

    private final ProxyServer server;
    private final ConfigManager configManager;
    private final Logger logger;
    private final boolean papiEnabled;
    private final Object plugin;

    public MessageService(ProxyServer server, ConfigManager configManager, Logger logger, Object plugin) {
        this.server = server;
        this.configManager = configManager;
        this.logger = logger;
        this.plugin = plugin;
        this.papiEnabled = checkPAPIAvailability();

        if (papiEnabled) {
            logger.info("PAPIProxyBridge 已检测到，PlaceholderAPI 支持已启用");
        } else {
            logger.info("PAPIProxyBridge 未检测到，将使用内置变量替换");
        }
    }

    /**
     * 检查PAPIProxyBridge是否可用
     */
    private boolean checkPAPIAvailability() {
        try {
            Class.forName("net.william278.papiproxybridge.api.PlaceholderAPI");
            return configManager.getBoolean("placeholderapi.enabled", true);
        } catch (ClassNotFoundException e) {
            logger.debug("PAPIProxyBridge 未找到，将使用内置变量替换");
            return false;
        }
    }

    /**
     * 处理玩家加入事件
     */
    public void handlePlayerJoin(Player player, boolean isFirstJoin) {
        boolean debugEnabled = configManager.getBoolean("debug.enabled", false);
        boolean verboseEvents = configManager.getBoolean("debug.verbose_events", false);

        if (debugEnabled) {
            logger.info("处理玩家加入事件: {} (第一次: {})", player.getUsername(), isFirstJoin);
        }

        // 根据tick配置安排消息发送
        if (isFirstJoin) {
            // 首次加入：发送首次加入消息和私人消息
            scheduleMessageByTick(player, "first_join", () -> sendFirstJoinMessages(player));
            scheduleMessageByTick(player, "message", () -> sendPrivateMessages(player));
        } else {
            // 普通加入：发送普通加入消息和私人消息
            scheduleMessageByTick(player, "join", () -> sendJoinMessages(player));
            scheduleMessageByTick(player, "message", () -> sendPrivateMessages(player));
        }

        // 控制台输出（立即执行）
        logToConsole(player, isFirstJoin ? "first_join" : "join");
    }

    /**
     * 根据tick配置安排消息发送
     */
    private void scheduleMessageByTick(Player player, String messageType, Runnable messageTask) {
        boolean enabled = configManager.getBoolean("messages." + messageType + ".enabled", true);
        if (!enabled) {
            return;
        }

        int ticks = configManager.getInt("messages." + messageType + ".tick", 0);
        long delayMs = ticks * 50L; // 1 tick = 50ms

        boolean debugEnabled = configManager.getBoolean("debug.enabled", false);
        boolean verboseEvents = configManager.getBoolean("debug.verbose_events", false);

        if (debugEnabled && verboseEvents) {
            logger.info("安排 {} 消息发送: {}tick ({}ms) 后执行", messageType, ticks, delayMs);
        }

        if (ticks <= 0) {
            // 立即执行
            messageTask.run();
        } else {
            // 延迟执行
            server.getScheduler().buildTask(plugin, messageTask)
                .delay(delayMs, TimeUnit.MILLISECONDS)
                .schedule();
        }
    }

    /**
     * 处理玩家退出事件
     */
    public void handlePlayerQuit(Player player, boolean isFirstQuit) {
        boolean debugEnabled = configManager.getBoolean("debug.enabled", false);

        if (debugEnabled) {
            logger.info("处理玩家退出事件: {} (第一次: {})", player.getUsername(), isFirstQuit);
        }

        // 根据tick配置安排消息发送
        if (isFirstQuit) {
            scheduleMessageByTick(player, "first_quit", () -> sendFirstQuitMessages(player));
        } else {
            scheduleMessageByTick(player, "quit", () -> sendQuitMessages(player));
        }

        // 控制台输出（立即执行）
        logToConsole(player, isFirstQuit ? "first_quit" : "quit");
    }

    /**
     * 发送普通加入消息
     */
    private void sendJoinMessages(Player player) {
        boolean enabled = configManager.getBoolean("messages.join.enabled", true);
        boolean debugEnabled = configManager.getBoolean("debug.enabled", false);
        boolean verboseEvents = configManager.getBoolean("debug.verbose_events", false);

        if (debugEnabled && verboseEvents) {
            logger.info("普通加入消息启用状态: {}", enabled);
        }

        if (!enabled) {
            if (debugEnabled && verboseEvents) {
                logger.info("普通加入消息已禁用，跳过发送");
            }
            return;
        }

        List<String> messages = configManager.getStringList("messages.join.messages");

        if (debugEnabled && verboseEvents) {
            logger.info("获取到 {} 条普通加入消息", messages.size());
            for (String msg : messages) {
                logger.info("消息内容: {}", msg);
            }
        }

        sendMessagesToAll(player, messages);
    }

    /**
     * 发送第一次加入消息
     */
    private void sendFirstJoinMessages(Player player) {
        if (!configManager.getBoolean("messages.first_join.enabled", true)) {
            return;
        }

        List<String> messages = configManager.getStringList("messages.first_join.messages");
        sendMessagesToAll(player, messages);
    }

    /**
     * 发送普通退出消息
     */
    private void sendQuitMessages(Player player) {
        if (!configManager.getBoolean("messages.quit.enabled", true)) {
            return;
        }

        List<String> messages = configManager.getStringList("messages.quit.messages");
        sendMessagesToAll(player, messages);
    }

    /**
     * 发送第一次退出消息
     */
    private void sendFirstQuitMessages(Player player) {
        if (!configManager.getBoolean("messages.first_quit.enabled", true)) {
            return;
        }

        List<String> messages = configManager.getStringList("messages.first_quit.messages");
        sendMessagesToAll(player, messages);
    }

    /**
     * 发送私人消息给玩家
     */
    private void sendPrivateMessages(Player player) {
        if (!configManager.getBoolean("messages.message.enabled", true)) {
            return;
        }

        List<String> messages = configManager.getStringList("messages.message.messages");
        sendMessagesToPlayer(player, messages);
    }

    /**
     * 向所有玩家发送消息（同一类别的消息统一延迟后一同发出）
     */
    private void sendMessagesToAll(Player player, List<String> messages) {
        if (messages.isEmpty()) {
            return;
        }

        boolean debugEnabled = configManager.getBoolean("debug.enabled", false);
        boolean verboseEvents = configManager.getBoolean("debug.verbose_events", false);

        // 处理所有消息的变量替换
        CompletableFuture<List<String>> processedMessagesFuture = processAllMessages(player, messages);

        processedMessagesFuture.thenAccept(processedMessages -> {
            if (debugEnabled && verboseEvents) {
                logger.info("准备同时发送 {} 条全服消息", processedMessages.size());
            }

            // 同时发送所有消息，不再有内部延迟
            for (String processedMessage : processedMessages) {
                Component component = ColorUtil.parseColorCodes(processedMessage);
                server.getAllPlayers().forEach(p -> p.sendMessage(component));

                if (debugEnabled && verboseEvents) {
                    logger.info("已发送全服消息: {}", ColorUtil.stripColorCodes(processedMessage));
                }
            }
        });
    }

    /**
     * 向特定玩家发送消息（同一类别的消息统一延迟后一同发出）
     */
    private void sendMessagesToPlayer(Player player, List<String> messages) {
        if (messages.isEmpty()) {
            return;
        }

        boolean debugEnabled = configManager.getBoolean("debug.enabled", false);
        boolean verboseEvents = configManager.getBoolean("debug.verbose_events", false);

        // 处理所有消息的变量替换
        CompletableFuture<List<String>> processedMessagesFuture = processAllMessages(player, messages);

        processedMessagesFuture.thenAccept(processedMessages -> {
            if (debugEnabled && verboseEvents) {
                logger.info("准备同时发送 {} 条私人消息给 {}",
                    processedMessages.size(), player.getUsername());
            }

            // 同时发送所有消息，不再有内部延迟
            for (String processedMessage : processedMessages) {
                Component component = ColorUtil.parseColorCodes(processedMessage);
                player.sendMessage(component);

                if (debugEnabled && verboseEvents) {
                    logger.info("已发送私人消息给 {}: {}",
                        player.getUsername(), ColorUtil.stripColorCodes(processedMessage));
                }
            }
        });
    }

    /**
     * 批量处理消息中的变量
     */
    private CompletableFuture<List<String>> processAllMessages(Player player, List<String> messages) {
        List<CompletableFuture<String>> futures = messages.stream()
            .map(message -> processMessage(player, message))
            .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .toList());
    }



    /**
     * 处理消息中的变量
     */
    private CompletableFuture<String> processMessage(Player player, String message) {
        // 首先替换内置变量
        String processed = replaceBuiltinPlaceholders(player, message);

        // 如果启用了PAPI，则处理PAPI变量
        if (papiEnabled) {
            return processPAPIPlaceholders(player, processed);
        } else {
            return CompletableFuture.completedFuture(processed);
        }
    }

    /**
     * 替换内置变量
     */
    private String replaceBuiltinPlaceholders(Player player, String message) {
        return message
            .replace("%player_name%", player.getUsername())
            .replace("%player_uuid%", player.getUniqueId().toString())
            .replace("%player_ip%", player.getRemoteAddress().getAddress().getHostAddress())
            .replace("%server_online%", String.valueOf(server.getPlayerCount()))
            .replace("%server_max%", String.valueOf(server.getConfiguration().getShowMaxPlayers()))
            .replace("%server_time_HH:mm:ss%", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
            .replace("%server_time_yyyy-MM-dd%", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .replace("%server_time_yyyy-MM-dd HH:mm:ss%", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    /**
     * 处理PAPI变量
     */
    private CompletableFuture<String> processPAPIPlaceholders(Player player, String message) {
        try {
            // 使用反射调用PAPIProxyBridge API
            Class<?> papiClass = Class.forName("net.william278.papiproxybridge.api.PlaceholderAPI");

            // 获取createInstance方法
            java.lang.reflect.Method createInstanceMethod = papiClass.getMethod("createInstance");
            Object apiInstance = createInstanceMethod.invoke(null);

            // 获取formatPlaceholders方法
            java.lang.reflect.Method formatMethod = papiClass.getMethod("formatPlaceholders", String.class, java.util.UUID.class);

            long timeout = configManager.getLong("placeholderapi.timeout", 1000L);

            @SuppressWarnings("unchecked")
            CompletableFuture<String> result = (CompletableFuture<String>) formatMethod.invoke(apiInstance, message, player.getUniqueId());

            return result.orTimeout(timeout, TimeUnit.MILLISECONDS)
                .exceptionally(throwable -> {
                    logger.warn("处理PlaceholderAPI变量时发生错误: {}", throwable.getMessage());
                    return message; // 返回原始消息
                });
        } catch (Exception e) {
            logger.warn("调用PlaceholderAPI时发生错误", e);
            return CompletableFuture.completedFuture(message);
        }
    }

    /**
     * 输出到控制台
     */
    private void logToConsole(Player player, String eventType) {
        if (!configManager.getBoolean("console.enabled", true)) {
            return;
        }

        String format = configManager.getString("console.format." + eventType,
            "[MessageTools] 玩家事件: %player_name%");

        String logMessage = replaceBuiltinPlaceholders(player, format);
        logger.info(logMessage);
    }
}
