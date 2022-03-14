package io.github.dailystruggle.commandsapi.bukkit.LocalParameters;


import io.github.dailystruggle.commandsapi.bukkit.BukkitParameter;
import io.github.dailystruggle.commandsapi.common.CommandParameter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class WorldParameter extends BukkitParameter {
    public WorldParameter(BiFunction<CommandSender, String, Boolean> isRelevant) {
        super(isRelevant);
    }

    @Override
    public Collection<String> values() {
        return Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
    }
}
