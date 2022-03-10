package io.github.dailystruggle.commandsapi;

import org.bukkit.command.CommandSender;

import java.util.Collection;

public abstract class CommandParameter {
    public Collection<String> allValues = null;

    public abstract Collection<String> getRelevantValues(CommandSender sender);
}
