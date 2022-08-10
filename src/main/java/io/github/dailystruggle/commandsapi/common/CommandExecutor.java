package io.github.dailystruggle.commandsapi.common;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record CommandExecutor(CommandsAPICommand command,
                              UUID callerId,
                              Map<String, List<String>> parameterValues,
                              CommandsAPICommand nextCommand,
                              CompletableFuture<Boolean> result) implements Runnable {
    public void run() {
        result.complete(command.onCommand(callerId,parameterValues,nextCommand));
    }
}
