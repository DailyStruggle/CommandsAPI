package io.github.dailystruggle.commandsapi.common.localCommands;

import io.github.dailystruggle.commandsapi.common.CommandExecutor;
import io.github.dailystruggle.commandsapi.common.CommandsAPI;
import io.github.dailystruggle.commandsapi.common.CommandsAPICommand;
import io.github.dailystruggle.commandsapi.common.CommandParameter;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.*;
import java.util.stream.Collectors;


/**
 * a command layer that can call a number of sub-commands, hence creating a tree structure.
 * This enables a typical command chain, where one command has a number of sub-commands
 */
public interface TreeCommand extends CommandsAPICommand {
    default void addParameter(String name, CommandParameter parameter) {
        getParameterLookup().put(name.toLowerCase(),parameter);
    }

    default void addSubCommand(CommandsAPICommand command) {
        getCommandLookup().put(command.name().toUpperCase(),command);
    }

    Map<String, CommandParameter> getParameterLookup();
    Map<String, CommandsAPICommand> getCommandLookup();

    default List<String> onTabComplete(UUID callerId, Predicate<String> permissionCheckMethod, String[] args, int i) {
        Set<String> parameterValues = new HashSet<>();
        while (i<args.length && args[i].contains(String.valueOf(CommandsAPI.parameterDelimiter))) {
            if(i<args.length-1) {
                String[] arr = args[i].split(String.valueOf(CommandsAPI.parameterDelimiter));
                parameterValues.add(arr[0]);
            }
            ++i;
        }

        List<String> possibleResults = new ArrayList<>();
        if(i==args.length) {//last value condition
            if(i>0) { //last value in a chain
                i--;
                int delimiterIdx = args[i].indexOf(CommandsAPI.parameterDelimiter);
                String arg = delimiterIdx > 0 ? args[i].substring(0, delimiterIdx) : args[i];

                if (delimiterIdx < 0) {
                    for (Map.Entry<String, CommandParameter> entry : getParameterLookup().entrySet()) {
                        if(parameterValues.contains(entry.getKey())) continue;
                        possibleResults.add(entry.getKey() + CommandsAPI.parameterDelimiter);
                    }
                    for (CommandsAPICommand command : getCommandLookup().values()) {
                        if (permissionCheckMethod.test(command.permission())) possibleResults.add(command.name());
                    }
                } else if (getParameterLookup().containsKey(arg)) {
                    if(parameterValues.contains(arg)) return new ArrayList<>();
                    String val = args[i].substring(delimiterIdx+1);
                    Set<String> vals = Arrays.stream(val.split(String.valueOf(CommandsAPI.multiParameterDelimiter))).collect(Collectors.toSet());
                    List<String> relevantValues = new ArrayList<>(getParameterLookup().get(arg).relevantValues(callerId));
                    String front = (args[i].contains(String.valueOf(CommandsAPI.multiParameterDelimiter)))
                            ?  args[i].substring(0,args[i].lastIndexOf(CommandsAPI.multiParameterDelimiter))+CommandsAPI.multiParameterDelimiter
                            : arg+CommandsAPI.parameterDelimiter;
                    relevantValues = relevantValues.stream().filter(s -> !vals.contains(s)).map(s -> front + s).collect(Collectors.toList());
                    possibleResults.addAll(relevantValues);
                }
            }
            else { //only value
                for (Map.Entry<String, CommandParameter> entry : getParameterLookup().entrySet()) {
                    possibleResults.add(entry.getKey() + CommandsAPI.parameterDelimiter);
                }
                for (CommandsAPICommand command : getCommandLookup().values()) {
                    if (permissionCheckMethod.test(command.permission())) possibleResults.add(command.name());
                }
            }
        }
        else {
            CommandsAPICommand nextCommand = getCommandLookup().get(args[i].toUpperCase());
            if(nextCommand != null) {
                return nextCommand.onTabComplete(callerId,permissionCheckMethod, args,i);
            }
            else {
                for (Map.Entry<String, CommandParameter> entry : getParameterLookup().entrySet()) {
                    if(parameterValues.contains(entry.getKey())) continue;
                    possibleResults.add(entry.getKey() + CommandsAPI.parameterDelimiter);
                }
                for (CommandsAPICommand command : getCommandLookup().values()) {
                    if (permissionCheckMethod.test(command.permission())) possibleResults.add(command.name());
                }
            }
        }

        possibleResults.add("help");
        if(args.length > 0) return possibleResults.stream().filter(s -> s.startsWith(args[args.length-1])).collect(Collectors.toList());
        else return possibleResults;
    }

    default boolean onCommand(UUID callerId, Predicate<String> permissionCheckMethod, Consumer<String> messageMethod, String[] args, int i) {
        if(!permissionCheckMethod.test(permission())) return false;
        Map<String,List<String>> parameterValues = new HashMap<>();
        for (; i < args.length; i++) {
            String arg = args[i];

            //catch delimiter with no value
            if (arg.endsWith(String.valueOf(CommandsAPI.parameterDelimiter))) {
                messageMethod.accept("no value given for " + arg);
                continue;
            }

            String[] argSplit = arg.split(String.valueOf(CommandsAPI.parameterDelimiter));
            if (argSplit.length < 2) {//if it's a sub-command, process the current command and run the subcommand
                if(arg.equalsIgnoreCase("help") && !getCommandLookup().containsKey("HELP")) {
                    help(callerId,permissionCheckMethod).forEach(messageMethod);
                    return true;
                }

                CommandsAPICommand subCommand = getCommandLookup().get(arg.toUpperCase());

                //catch bad command
                if (subCommand == null) {
                    return true;
                }

                //on top of trying subcommand, run current command in case of independent functionality

                CompletableFuture<Boolean> cont = new CompletableFuture<>();
                new CommandExecutor(this,callerId,parameterValues,subCommand, cont);

                //catch no perms for subcommand
                if (!permissionCheckMethod.test(subCommand.permission())) {
                    return false;
                }

                //run subcommand after this command
                int finalI = i;
                cont.whenCompleteAsync((aBoolean, throwable) -> {
                    if(aBoolean)
                        subCommand.onCommand(callerId,permissionCheckMethod, messageMethod, args, finalI);
                });
                return true;
            }

            //otherwise, test and add the current arg to the list of parameters
            String paramName = argSplit[0].toLowerCase();
            CommandParameter currentParameter = getParameterLookup().get(paramName);
            if (currentParameter == null) {
                messageMethod.accept("bad parameter:" + argSplit[0]);
                continue;
            }

            String val = argSplit[1];

            //filter according to actual possible inputs
            CommandParameter parameter = getParameterLookup().get(arg);

            //split args
            //filter according to parameter limiter to guard possible answers
            //collect into list for command experience
            List<String> vals =
                    Arrays.stream(val.split(String.valueOf(CommandsAPI.multiParameterDelimiter)))
                            .filter(s -> parameter.isRelevant.apply(callerId,s))
                            .collect(Collectors.toList());

            //only add if there are valid values
            if(vals.size() > 0)
                parameterValues.putIfAbsent(paramName, vals);
        }
        return onCommand(callerId, parameterValues, null);
    }

    default List<String> help(UUID callerId, Predicate<String> permissionCheckMethod) {
        List<String> parents = new ArrayList<>();
        CommandsAPICommand parent = parent();
        while (parent!=null) {
            parents.add(parent.name());
            parent = parent.parent();
        }

        StringBuilder completeCommand = new StringBuilder();
        for(int i = parents.size()-1; i > 0; i--) {
            completeCommand.append(parents.get(i)).append(" ");
        }

        List<String> possibleResults = new ArrayList<>(2+getParameterLookup().size() + getCommandLookup().size());
        possibleResults.add("Commands: ");
        for (CommandsAPICommand command : getCommandLookup().values()) {
            if (!permissionCheckMethod.test(command.permission())) continue;
            possibleResults.add("  - /" + completeCommand + command.name() +
                    "\n    " + command.description());
        }
        possibleResults.add("Parameters: ");
        for (Map.Entry<String, CommandParameter> entry : getParameterLookup().entrySet()) {
            if(!permissionCheckMethod.test(entry.getValue().permission())) continue;
            possibleResults.add("  - " + entry.getKey() + CommandsAPI.parameterDelimiter +
                    "\n    " + entry.getValue().description());
        }
        return possibleResults;
    }
}
