package io.github.dailystruggle.commandsapi.bukkit.localCommands;

import io.github.dailystruggle.commandsapi.bukkit.BukkitCommand;

import io.github.dailystruggle.commandsapi.common.CommandParameter;
import io.github.dailystruggle.commandsapi.common.CommandsAPI;
import io.github.dailystruggle.commandsapi.common.CommandsAPICommand;
import io.github.dailystruggle.commandsapi.common.localCommands.TreeCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class BukkitTreeCommand extends BukkitCommand implements TreeCommand {
    protected final Map<String, CommandParameter> parameterLookup = new ConcurrentHashMap<>();

    // key: command name
    // value: command object
    protected final Map<String, CommandsAPICommand> commandLookup = new ConcurrentHashMap<>();

    public BukkitTreeCommand(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        UUID senderId;
        if(sender instanceof Player) {
            senderId = ((Player) sender).getUniqueId();
        }
        else senderId = CommandsAPI.serverId;

        Predicate<String> permissionCheckMethod = sender::hasPermission;
        Consumer<String> messageMethod = sender::sendMessage;
        return onCommand(senderId,permissionCheckMethod,messageMethod,args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        UUID senderId;
        if(sender instanceof Player) {
            senderId = ((Player) sender).getUniqueId();
        }
        else senderId = CommandsAPI.serverId;

        Predicate<String> permissionCheckMethod = sender::hasPermission;
        return onTabComplete(senderId,permissionCheckMethod,args);
    }

    @Override
    public boolean onCommand(UUID callerId,
                             Map<String,List<String>> parameterValues,
                             CommandsAPICommand nextCommand){
        CommandSender commandSender;
        if(callerId.equals(CommandsAPI.serverId)) {
            commandSender = Bukkit.getConsoleSender();
        }
        else {
            commandSender = Bukkit.getPlayer(callerId);
            if(commandSender == null) return false;
        }
        return onCommand(commandSender,parameterValues,nextCommand);
    }

    public abstract boolean onCommand(CommandSender sender,
                              Map<String,List<String>> parameterValues,
                              CommandsAPICommand nextCommand);

    @Override
    public Map<String, CommandParameter> getParameterLookup() {
        return parameterLookup;
    }

    @Override
    public Map<String, CommandsAPICommand> getCommandLookup() {
        return commandLookup;
    }
}
