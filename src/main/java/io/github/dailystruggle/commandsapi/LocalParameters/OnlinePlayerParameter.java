package io.github.dailystruggle.commandsapi.LocalParameters;

import io.github.dailystruggle.commandsapi.CommandParameter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class OnlinePlayerParameter extends CommandParameter {
    //because this is a dynamic value, mitigate spam by refusing to update until some time passes
    private long lastRefreshTimeMillis = 0;

    public OnlinePlayerParameter() {
        allValues = new ArrayList<>();
    }

    @Override
    public Collection<String> getRelevantValues(CommandSender sender) {
        long currentTimeMillis = System.currentTimeMillis();
        long diff = currentTimeMillis-lastRefreshTimeMillis;
        if(diff<0) diff = Long.MAX_VALUE; //in case counter resets
        if(diff < 2000) return allValues;

        lastRefreshTimeMillis = currentTimeMillis;
        allValues = Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).collect(Collectors.toList());
        return allValues;
    }
}
