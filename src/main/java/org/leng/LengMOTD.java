package org.leng;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.leng.commands.CommandHandler;
import org.leng.listeners.PlayerJoinListener;

import java.io.File;
import java.util.List;
import java.util.Random;

public class LengMOTD extends JavaPlugin {
    private OneLine oneLine;
    private boolean maintenanceMode;
    private String maintenanceMessage;
    private List<String> staffList;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        oneLine = new OneLine(this);

        // Load maintenance mode settings
        FileConfiguration config = getConfig();
        maintenanceMode = config.getBoolean("maintenance_mode", false);
        maintenanceMessage = config.getString("maintenance_message", "§c服务器正在维护中，请稍后再试！");
        staffList = config.getStringList("staff");

        // Register commands
        getCommand("lmotd").setExecutor(new CommandHandler(this));

        // Register listener
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        // Start MOTD update task
        startMotdUpdateTask();

        // Check and create icon folder if it doesn't exist
        File iconFolder = new File(getDataFolder(), "icon");
        if (!iconFolder.exists()) {
            iconFolder.mkdirs();
            getLogger().info("§a[LengMOTD] 已创建图标文件夹: " + iconFolder.getAbsolutePath());
        }

        // Log plugin enable message
        getLogger().info("§a[LengMOTD] 已启用");
    }

    @Override
    public void onDisable() {
        // Log plugin disable message
        getLogger().info("§c[LengMOTD] 已禁用");
    }

    private void startMotdUpdateTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            FileConfiguration config = getConfig();
            String mode = config.getString("mode", "online");

            String firstLine;
            String secondLine;

            if ("online".equalsIgnoreCase(mode)) {
                firstLine = config.getString("online.first", "§6欢迎来玩我的服务器呀");
                secondLine = oneLine.getOneLine(); // 第二行从一言接口获取
                if (secondLine != null) {
                    secondLine = "§3★★★ " + secondLine + " ★★★"; // 添加特殊字符
                }
            } else {
                firstLine = getRandomLine(config.getStringList("custom.first"));
                secondLine = getRandomLine(config.getStringList("custom.second"));
            }

            if (firstLine != null && secondLine != null) {
                // Set server MOTD
                String coloredMotd = firstLine + "\n" + secondLine;
                coloredMotd = coloredMotd.replaceAll("&([0-9a-fk-or])", "§$1");
                Bukkit.getServer().setMotd(coloredMotd);
            }
        }, 0L, 1200L); // 1200 ticks = 60 seconds
    }

    private String getRandomLine(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            return "";
        }
        Random random = new Random();
        return lines.get(random.nextInt(lines.size()));
    }

    public boolean isMaintenanceMode() {
        return maintenanceMode;
    }

    public String getMaintenanceMessage() {
        return maintenanceMessage;
    }

    public void setMaintenanceMode(boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
        getConfig().set("maintenance_mode", maintenanceMode);
        saveConfig();
    }

    public void setMaintenanceMessage(String maintenanceMessage) {
        this.maintenanceMessage = maintenanceMessage;
        getConfig().set("maintenance_message", maintenanceMessage);
        saveConfig();
    }

    public List<String> getStaffList() {
        return staffList;
    }

    public void addStaff(String playerName) {
        staffList.add(playerName);
        getConfig().set("staff", staffList);
        saveConfig();
    }

    public void removeStaff(String playerName) {
        staffList.remove(playerName);
        getConfig().set("staff", staffList);
        saveConfig();
    }

    public boolean isStaff(Player player) {
        return staffList.contains(player.getName());
    }
}