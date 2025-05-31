package org.plugin.messagetools.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import org.plugin.messagetools.data.PlayerDataManager;
import org.plugin.messagetools.service.MessageService;
import org.slf4j.Logger;

import java.util.UUID;

/**
 * 玩家事件监听器
 * 监听玩家加入和退出事件
 */
public class PlayerEventListener {

    private final MessageService messageService;
    private final PlayerDataManager playerDataManager;
    private final Logger logger;

    public PlayerEventListener(MessageService messageService, PlayerDataManager playerDataManager, Logger logger) {
        this.messageService = messageService;
        this.playerDataManager = playerDataManager;
        this.logger = logger;
    }

    /**
     * 监听玩家登录事件
     * 这个事件在玩家成功登录到代理服务器时触发
     */
    @Subscribe(order = PostOrder.LATE)
    public void onPostLogin(PostLoginEvent event) {
        Player player = event.getPlayer();

        // 这是玩家加入代理服务器的事件
        handlePlayerJoin(player);
    }

    /**
     * 监听玩家断开连接事件
     * 这个事件在玩家完全离开代理服务器时触发
     */
    @Subscribe(order = PostOrder.LATE)
    public void onPlayerDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        handlePlayerQuit(player);
    }

    /**
     * 处理玩家加入
     */
    private void handlePlayerJoin(Player player) {
        UUID playerUUID = player.getUniqueId();

        try {
            // 检查是否是第一次加入
            boolean isFirstJoin = playerDataManager.isFirstJoin(playerUUID);

            // 标记玩家已加入
            playerDataManager.markPlayerJoined(playerUUID);

            // 处理消息发送
            messageService.handlePlayerJoin(player, isFirstJoin);

        } catch (Exception e) {
            logger.error("处理玩家加入事件时发生错误: {}", player.getUsername(), e);
        }
    }

    /**
     * 处理玩家退出
     */
    private void handlePlayerQuit(Player player) {
        UUID playerUUID = player.getUniqueId();

        try {
            // 检查是否是第一次退出
            boolean isFirstQuit = playerDataManager.isFirstQuit(playerUUID);

            logger.debug("玩家 {} {} 离开服务器",
                player.getUsername(),
                isFirstQuit ? "(第一次)" : "(再次)");

            // 标记玩家已退出
            playerDataManager.markPlayerQuit(playerUUID);

            // 处理消息发送
            messageService.handlePlayerQuit(player, isFirstQuit);

        } catch (Exception e) {
            logger.error("处理玩家退出事件时发生错误: {}", player.getUsername(), e);
        }
    }
}
