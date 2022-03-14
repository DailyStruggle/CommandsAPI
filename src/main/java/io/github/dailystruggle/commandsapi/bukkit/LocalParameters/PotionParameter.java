package io.github.dailystruggle.commandsapi.bukkit.LocalParameters;


import io.github.dailystruggle.commandsapi.bukkit.BukkitParameter;
import org.bukkit.command.CommandSender;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class PotionParameter extends BukkitParameter {
    List<String> allValues;
    public PotionParameter(BiFunction<CommandSender, String, Boolean> isRelevant) {
        super(isRelevant);
        allValues = Arrays.stream(PotionEffectType.values()).map(PotionEffectType::getName).collect(Collectors.toList());
    }

    @Override
    public Collection<String> values() {
        return allValues;
    }
}
