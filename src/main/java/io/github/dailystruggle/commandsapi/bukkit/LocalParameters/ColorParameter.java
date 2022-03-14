package io.github.dailystruggle.commandsapi.bukkit.LocalParameters;


import io.github.dailystruggle.commandsapi.bukkit.BukkitParameter;
import io.github.dailystruggle.commandsapi.common.CommandParameter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ColorParameter extends BukkitParameter {
    List<String> allValues;
    public ColorParameter(BiFunction<CommandSender, String, Boolean> isRelevant) {
        super(isRelevant);
        allValues = Arrays.asList("FFFFFF","000000");
    }

    @Override
    public Collection<String> values() {
        return allValues;
    }
}
