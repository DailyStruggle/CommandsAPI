package io.github.dailystruggle.commandsapi.bukkit.LocalParameters;


import io.github.dailystruggle.commandsapi.bukkit.BukkitParameter;
import io.github.dailystruggle.commandsapi.common.CommandParameter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.BiFunction;

public class CoordinateParameter extends BukkitParameter {
    List<String> allValues;
    public CoordinateParameter(BiFunction<CommandSender, String, Boolean> isRelevant) {
        super(isRelevant);
        allValues = Arrays.asList("true","false");
    }

    @Override
    public Collection<String> values() {
        return allValues;
    }
}
