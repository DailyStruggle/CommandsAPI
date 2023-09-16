package io.github.dailystruggle.commandsapi.common;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * a general and interchangeable command type, requiring tab completion and command execution
 */
public interface CommandsAPICommand {
    String name();
    String permission();
    String description();
    CommandsAPICommand parent();
    void msgBadParameter(UUID callerId, String parameterName, String parameterValue);
    long avgTime();

    /**
     * @param permissionCheckMethod a way to check command sender's permissions
     * @param args                  all additional arguments given with this command
     * @return list of possible values the player could be tabbing for
     */
    List<String> onTabComplete(@NotNull UUID callerId,
                               @NotNull Predicate<String> permissionCheckMethod,
                               @NotNull String[] args,
                               int i,
                               @NotNull Map<String,CommandParameter> tempParameters);

    /**
     * @param permissionCheckMethod a way to check command sender's permissions
     * @param messageMethod         a way to send the command sender a message
     * @param args                  all arguments given with this command
     * @return success or failure of permission check
     */
    CompletableFuture<Boolean> onCommand(@NotNull UUID callerId,
                                         @NotNull Predicate<String> permissionCheckMethod,
                                         @NotNull Consumer<String> messageMethod,
                                         @NotNull String[] args,
                                         int i,
                                         @Nullable Map<String,CommandParameter> tempParameters);

    /**
     * function to be run by each
     * @param callerId ID of caller
     * @param parameterValues parameters given for the current command
     * @param nextCommand next command in the chain, if any
     * @return command success, determining whether this api attempts to run nextCommand
     */
    boolean onCommand(UUID callerId, Map<String,List<String>> parameterValues, CommandsAPICommand nextCommand);

    List<String> help(UUID callerId, Predicate<String> permissionCheckMethod);
}
