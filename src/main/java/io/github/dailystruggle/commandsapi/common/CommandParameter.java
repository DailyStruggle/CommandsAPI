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

    public CommandParameter(BiFunction<UUID, String, Boolean> isRelevant) {
        this.isRelevant = isRelevant;
    }

    public abstract Collection<String> values();

    /**
     * @param senderId id of command sender. The id is less specific than a player class.
     * @return subset of all values, based on the isRelevant function
     */
    public Collection<String> relevantValues(UUID senderId) {
        return values().stream().filter(s -> this.isRelevant.apply(senderId,s)).collect(Collectors.toList());
    }
}
