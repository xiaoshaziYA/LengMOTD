package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.leng.LengMOTD;

import java.util.Collections;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {
    private final LengMOTD plugin;

    public CommandHandler(LengMOTD plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§c/LengMOTD reload - 重载配置文件");
            sender.sendMessage("§c/LengMOTD wh - 切换维护模式");
            sender.sendMessage("§c/LengMOTD add <player> - 添加玩家到 staff 名单");
            sender.sendMessage("§c/LengMOTD remove <player> - 从 staff 名单中移除玩家");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                if (!sender.hasPermission("lmotd.reload")) {
                    sender.sendMessage("§c你没有权限使用这个命令！");
                    return true;
                }
                plugin.reloadConfig();
                sender.sendMessage("§a配置文件已重载！");
                return true;

            case "wh":
                if (!sender.hasPermission("lmotd.wh")) {
                    sender.sendMessage("§c你没有权限使用这个命令！");
                    return true;
                }
                boolean maintenanceMode = plugin.isMaintenanceMode();
                plugin.setMaintenanceMode(!maintenanceMode);
                sender.sendMessage("§a维护模式已" + (!maintenanceMode ? "开启" : "关闭"));
                return true;

            case "add":
                if (!sender.hasPermission("lmotd.staff")) {
                    sender.sendMessage("§c你没有权限使用这个命令！");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage("§c用法: /lmotd add <player>");
                    return true;
                }
                String playerName = args[1]; // 确保在所有路径上都初始化
                plugin.addStaff(playerName);
                sender.sendMessage("§a玩家 " + playerName + " 已添加到 staff 名单！");
                return true;

            case "remove":
                if (!sender.hasPermission("lmotd.staff")) {
                    sender.sendMessage("§c你没有权限使用这个命令！");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage("§c用法: /lmotd remove <player>");
                    return true;
                }
                String playerNameRemove = args[1]; // 确保在所有路径上都初始化
                plugin.removeStaff(playerNameRemove);
                sender.sendMessage("§a玩家 " + playerNameRemove + " 已从 staff 名单中移除！");
                return true;

            default:
                sender.sendMessage("§c未知命令。使用 /LengMOTD 获取帮助。");
                return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("reload", "wh", "add", "remove");
        }
        return Collections.emptyList();
    }
}