package org.plugin.messagetools.data;

import org.yaml.snakeyaml.Yaml;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 玩家数据管理器
 * 负责管理玩家的首次加入状态等数据
 */
public class PlayerDataManager {
    
    private final Path dataDirectory;
    private final Logger logger;
    private final Map<UUID, PlayerData> playerDataCache;
    private final Yaml yaml;
    private final String fileName;
    
    public PlayerDataManager(Path dataDirectory, Logger logger, String fileName) {
        this.dataDirectory = dataDirectory;
        this.logger = logger;
        this.fileName = fileName;
        this.playerDataCache = new ConcurrentHashMap<>();
        this.yaml = new Yaml();
    }
    
    /**
     * 加载玩家数据
     */
    public void loadPlayerData() {
        try {
            Path dataFile = dataDirectory.resolve(fileName);
            
            if (!Files.exists(dataFile)) {
                logger.info("玩家数据文件不存在，将创建新文件");
                return;
            }
            
            try (InputStream inputStream = Files.newInputStream(dataFile)) {
                Map<String, Object> data = yaml.load(inputStream);
                
                if (data != null) {
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        try {
                            UUID uuid = UUID.fromString(entry.getKey());
                            @SuppressWarnings("unchecked")
                            Map<String, Object> playerInfo = (Map<String, Object>) entry.getValue();
                            
                            PlayerData playerData = new PlayerData();
                            playerData.setFirstJoin((Boolean) playerInfo.getOrDefault("firstJoin", true));
                            playerData.setFirstQuit((Boolean) playerInfo.getOrDefault("firstQuit", true));
                            playerData.setLastJoinTime((Long) playerInfo.getOrDefault("lastJoinTime", System.currentTimeMillis()));
                            playerData.setLastQuitTime((Long) playerInfo.getOrDefault("lastQuitTime", 0L));
                            
                            playerDataCache.put(uuid, playerData);
                        } catch (Exception e) {
                            logger.warn("加载玩家数据时发生错误: {}", entry.getKey(), e);
                        }
                    }
                }
                
                logger.info("已加载 {} 个玩家的数据", playerDataCache.size());
            }
            
        } catch (IOException e) {
            logger.error("加载玩家数据文件时发生错误", e);
        }
    }
    
    /**
     * 保存玩家数据
     */
    public void savePlayerData() {
        try {
            Path dataFile = dataDirectory.resolve(fileName);
            
            // 确保目录存在
            if (!Files.exists(dataDirectory)) {
                Files.createDirectories(dataDirectory);
            }
            
            Map<String, Object> data = new HashMap<>();
            
            for (Map.Entry<UUID, PlayerData> entry : playerDataCache.entrySet()) {
                Map<String, Object> playerInfo = new HashMap<>();
                PlayerData playerData = entry.getValue();
                
                playerInfo.put("firstJoin", playerData.isFirstJoin());
                playerInfo.put("firstQuit", playerData.isFirstQuit());
                playerInfo.put("lastJoinTime", playerData.getLastJoinTime());
                playerInfo.put("lastQuitTime", playerData.getLastQuitTime());
                
                data.put(entry.getKey().toString(), playerInfo);
            }
            
            try (FileWriter writer = new FileWriter(dataFile.toFile())) {
                yaml.dump(data, writer);
                logger.debug("玩家数据保存成功");
            }
            
        } catch (IOException e) {
            logger.error("保存玩家数据文件时发生错误", e);
        }
    }
    
    /**
     * 获取玩家数据
     */
    public PlayerData getPlayerData(UUID uuid) {
        return playerDataCache.computeIfAbsent(uuid, k -> new PlayerData());
    }
    
    /**
     * 检查玩家是否第一次加入
     */
    public boolean isFirstJoin(UUID uuid) {
        return getPlayerData(uuid).isFirstJoin();
    }
    
    /**
     * 检查玩家是否第一次退出
     */
    public boolean isFirstQuit(UUID uuid) {
        return getPlayerData(uuid).isFirstQuit();
    }
    
    /**
     * 标记玩家已加入
     */
    public void markPlayerJoined(UUID uuid) {
        PlayerData data = getPlayerData(uuid);
        data.setFirstJoin(false);
        data.setLastJoinTime(System.currentTimeMillis());
    }
    
    /**
     * 标记玩家已退出
     */
    public void markPlayerQuit(UUID uuid) {
        PlayerData data = getPlayerData(uuid);
        data.setFirstQuit(false);
        data.setLastQuitTime(System.currentTimeMillis());
    }
    
    /**
     * 玩家数据类
     */
    public static class PlayerData {
        private boolean firstJoin = true;
        private boolean firstQuit = true;
        private long lastJoinTime = System.currentTimeMillis();
        private long lastQuitTime = 0L;
        
        public boolean isFirstJoin() {
            return firstJoin;
        }
        
        public void setFirstJoin(boolean firstJoin) {
            this.firstJoin = firstJoin;
        }
        
        public boolean isFirstQuit() {
            return firstQuit;
        }
        
        public void setFirstQuit(boolean firstQuit) {
            this.firstQuit = firstQuit;
        }
        
        public long getLastJoinTime() {
            return lastJoinTime;
        }
        
        public void setLastJoinTime(long lastJoinTime) {
            this.lastJoinTime = lastJoinTime;
        }
        
        public long getLastQuitTime() {
            return lastQuitTime;
        }
        
        public void setLastQuitTime(long lastQuitTime) {
            this.lastQuitTime = lastQuitTime;
        }
    }
}
