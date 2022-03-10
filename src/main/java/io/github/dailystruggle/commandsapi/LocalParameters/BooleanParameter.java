package io.github.dailystruggle.commandsapi.LocalParameters;

import io.github.dailystruggle.commandsapi.CommandParameter;
import org.bukkit.command.CommandSender;

import java.util.*;

public class BooleanParameter extends CommandParameter {

    public BooleanParameter() {
        allValues = Arrays.asList("true","false");
    }

    @Override
    public Collection<String> getRelevantValues(CommandSender sender) {
        return allValues;
    }
}
