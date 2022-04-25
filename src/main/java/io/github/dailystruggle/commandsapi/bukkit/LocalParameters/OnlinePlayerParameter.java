package io.github.dailystruggle.commandsapi.bukkit.LocalParameters;


import io.github.dailystruggle.commandsapi.bukkit.BukkitParameter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class OnlinePlayerParameter extends BukkitParameter {
    public OnlinePlayerParameter(String permission, String description, BiFunction<CommandSender, String, Boolean> isRelevant) {
        super(permission, description, isRelevant);
    }

    @Override
    public Set<String> values() {
        return Bukkit.getOnlinePlayers().stream().map(OfflinePlayer::getName).collect(Collectors.toSet());
    }
}
