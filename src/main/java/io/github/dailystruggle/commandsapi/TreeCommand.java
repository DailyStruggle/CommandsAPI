package io.github.dailystruggle.commandsapi;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class TreeCommand extends Command implements TabCompleter, CommandExecutor {
    public static char parameterDelimiter = ':';

    protected final Map<String,CommandParameter> parameterLookup = new ConcurrentHashMap<>();

    // key: parameter name,
    // value: permission requirement
    protected final Map<CommandParameter,String> parameterPermissions = new ConcurrentHashMap<>();

    // key: command name
    // value: command object
    protected final Map<String, TreeCommand> commandLookup = new ConcurrentHashMap<>();

    // key: command
    // value: permission requirement
    protected final Map<TreeCommand,String> commands = new ConcurrentHashMap<>();

    public TreeCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return onCommand(sender,this,this.getName(),args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        int i = 0;
        while (i<args.length && args[i].contains(String.valueOf(parameterDelimiter))) {
            ++i;
        }
        i--;

        //last value condition
        List<String> possibleResults = new ArrayList<>();
        if(i==args.length-1) {
            int delimiterIdx = args[i].indexOf(parameterDelimiter);
            String arg = delimiterIdx > 0 ? args[i].substring(0, delimiterIdx) : args[i];
            if(delimiterIdx<0) {
                for( Map.Entry<CommandParameter, String> entry : parameterPermissions.entrySet()) {
                    if(sender.hasPermission(entry.getValue())) possibleResults.addAll(entry.getKey().getRelevantValues(sender));
                }
                for (Map.Entry<TreeCommand, String> entry : commands.entrySet()) {
                    if (sender.hasPermission(entry.getValue())) possibleResults.add(entry.getKey().getName());
                }
            }
            else if(parameterLookup.containsKey(arg)) {
                List<String> relevantValues = new ArrayList<>(parameterLookup.get(arg).getRelevantValues(sender));
                relevantValues.forEach(s -> s=arg+parameterDelimiter+s);
                possibleResults.addAll(relevantValues);
            }
        }
        else {
            TreeCommand nextCommand = commandLookup.get(args[i]);
            if(nextCommand != null) {
                possibleResults.addAll(nextCommand.tabComplete(sender,alias,Arrays.copyOfRange(args,i,args.length)));
            }
            else {
                for( Map.Entry<CommandParameter, String> entry : parameterPermissions.entrySet()) {
                    if(sender.hasPermission(entry.getValue())) possibleResults.addAll(entry.getKey().getRelevantValues(sender));
                }
                for (Map.Entry<TreeCommand, String> entry : commands.entrySet()) {
                    if (sender.hasPermission(entry.getValue())) possibleResults.add(entry.getKey().getName());
                }
            }
        }

        return possibleResults.stream().filter(s -> s.startsWith(args[args.length-1])).collect(Collectors.toList());
    }
}
