package io.github.dailystruggle.commandsapi.bukkit.localCommands;

import io.github.dailystruggle.commandsapi.bukkit.BukkitCommand;

import io.github.dailystruggle.commandsapi.common.CommandParameter;
import io.github.dailystruggle.commandsapi.common.CommandsAPI;
import io.github.dailystruggle.commandsapi.common.CommandsAPICommand;
import io.github.dailystruggle.commandsapi.common.localCommands.TreeCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BukkitTreeCommand extends BukkitCommand implements TreeCommand {
    protected long avgTime = 0;

    protected final Map<String, CommandParameter> parameterLookup = new ConcurrentHashMap<>();

    private final CommandsAPICommand parent;

    // key: command name
    // value: command object
    protected final Map<String, CommandsAPICommand> commandLookup = new ConcurrentHashMap<>();

    public BukkitTreeCommand(Plugin plugin, @Nullable CommandsAPICommand parent) {
        super(plugin);
        PluginCommand command = Bukkit.getPluginCommand(name());
        if(command!=null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
        this.parent = parent;
    }

    @Override
    public CommandsAPICommand parent() {
        return parent;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        UUID senderId;
        if(sender instanceof Player) {
            senderId = ((Player) sender).getUniqueId();
        }
        else if(sender.getName().equals(Bukkit.getConsoleSender().getName())) senderId = CommandsAPI.serverId;
        else {
            sender.sendMessage("alternate command senders not currently supported");
            return false;
        }

        return onCommand(senderId,sender::hasPermission,sender::sendMessage,args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        UUID senderId;
        if(sender instanceof Player) {
            senderId = ((Player) sender).getUniqueId();
        }
        else if(sender.getName().equals(Bukkit.getConsoleSender().getName())) senderId = CommandsAPI.serverId;
        else return null;

        return onTabComplete(senderId,sender::hasPermission,args);
    }

    @Override
    public boolean onCommand(UUID callerId,
                             Map<String,List<String>> parameterValues,
                             CommandsAPICommand nextCommand){
        long start = System.nanoTime();
        CommandSender commandSender;
        if(callerId.equals(CommandsAPI.serverId)) {
            commandSender = Bukkit.getConsoleSender();
        }
        else {
            commandSender = Bukkit.getPlayer(callerId);
            if(commandSender == null) return false;
        }
        boolean res = onCommand(commandSender,parameterValues,nextCommand);
        long stop = System.nanoTime();
        if(stop<start) start = -(Long.MAX_VALUE-start);
        long diff = stop - start;
        if(avgTime == 0) avgTime = diff;
        else avgTime = ((avgTime*7)/8) + (diff/8);
        return res;
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

    @Override
    public long avgTime() {
        return avgTime;
    }
}
