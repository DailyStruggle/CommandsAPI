package io.github.dailystruggle.commandsapi.common;

import java.util.Collection;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public abstract class CommandParameter {
    /**
     * function to validate enum values, using the player id
     */
    protected BiFunction<UUID,String,Boolean> isRelevant;
    private final String permission;
    private final String description;

    public CommandParameter(String permission, String description, BiFunction<UUID, String, Boolean> isRelevant) {
        this.isRelevant = isRelevant;
        this.permission = permission;
        this.description = description;
    }

    public abstract Collection<String> values();

    public String permission() {
        return permission;
    }

    public String description() {
        return description;
    }

    /**
     * @param senderId id of command sender. The id is less specific than a player class.
     * @return subset of all values, based on the isRelevant function
     */
    public Collection<String> relevantValues(UUID senderId) {
        return values().stream().filter(s -> this.isRelevant.apply(senderId,s)).collect(Collectors.toList());
    }
}
