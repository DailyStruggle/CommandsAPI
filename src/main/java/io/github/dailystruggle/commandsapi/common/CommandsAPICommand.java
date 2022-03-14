package io.github.dailystruggle.commandsapi.common;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * a general and interchangeable command type, requiring tab completion and command execution
 */
public interface CommandsAPICommand {
    String name();
    String permission();

    /**
     * @param permissionCheckMethod a way to check command sender's permissions
     * @param args                  all additional arguments given with this command
     * @return list of possible values the player could be tabbing for
     */
    default List<String> onTabComplete(UUID callerId, Predicate<String> permissionCheckMethod, String[] args) {
        return null;
    }

    /**
     * @param permissionCheckMethod a way to check command sender's permissions
     * @param messageMethod a way to send the command sender a message
     * @param args all additional arguments given with this command
     * @return success or failure of permission check
     */
    boolean onCommand(UUID callerId, Predicate<String> permissionCheckMethod, Consumer<String> messageMethod, String[] args);

    /**
     * function to be run by each
     * @param callerId ID of caller
     * @param parameterValues parameters given for the current command
     * @param nextCommand next command in the chain, if any
     * @return command success
     */
    boolean onCommand(UUID callerId, Map<String,List<String>> parameterValues, CommandsAPICommand nextCommand);
}
