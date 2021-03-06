package io.github.dailystruggle.commandsapi.bukkit.LocalParameters;


import io.github.dailystruggle.commandsapi.bukkit.BukkitParameter;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.function.BiFunction;

public class CoordinateParameter extends BukkitParameter {
    Set<String> allValues;
    public CoordinateParameter(String permission, String description, BiFunction<CommandSender, String, Boolean> isRelevant) {
        super(permission, description,isRelevant);
        allValues = new HashSet<>(Arrays.asList("true","false"));
    }

    @Override
    public Set<String> values() {
        return allValues;
    }
}
