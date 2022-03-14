package io.github.dailystruggle.commandsapi.bukkit.LocalParameters;


import io.github.dailystruggle.commandsapi.bukkit.BukkitParameter;
import io.github.dailystruggle.commandsapi.common.CommandParameter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class OfflinePlayerParameter extends BukkitParameter {
    //because this is a dynamic value, mitigate spam by refusing to update until some time passes
    public OfflinePlayerParameter(BiFunction<CommandSender, String, Boolean> isRelevant) {
        super(isRelevant);
    }

    @Override
    public Collection<String> values() {
        return Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).collect(Collectors.toList());
    }
}
