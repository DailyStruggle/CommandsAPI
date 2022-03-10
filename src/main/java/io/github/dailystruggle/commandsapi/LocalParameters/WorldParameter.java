package io.github.dailystruggle.commandsapi.LocalParameters;

import io.github.dailystruggle.commandsapi.CommandParameter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class WorldParameter extends CommandParameter {
    //because this is a dynamic value, mitigate spam by refusing to update until some time passes
    private long lastRefreshTimeMillis = 0;

    @Override
    public Collection<String> getRelevantValues(CommandSender sender) {
        long currentTimeMillis = System.currentTimeMillis();
        long diff = currentTimeMillis-lastRefreshTimeMillis;
        if(diff<0) diff = Long.MAX_VALUE; //in case counter resets
        if(diff < 10000) return allValues;

        lastRefreshTimeMillis = currentTimeMillis;
        allValues = Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
        return allValues;
    }
}
