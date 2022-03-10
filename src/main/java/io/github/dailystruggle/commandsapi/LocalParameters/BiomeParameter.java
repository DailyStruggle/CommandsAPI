package io.github.dailystruggle.commandsapi.LocalParameters;

import io.github.dailystruggle.commandsapi.CommandParameter;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class BiomeParameter extends CommandParameter {

    public BiomeParameter() {
        allValues = Arrays.stream(Biome.values()).map(Enum::name).collect(Collectors.toList());
    }

    @Override
    public Collection<String> getRelevantValues(CommandSender sender) {
        return allValues;
    }
}
