package io.github.dailystruggle.commandsapi.LocalParameters;

import io.github.dailystruggle.commandsapi.CommandParameter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CoordinateParameter extends CommandParameter {
    public CoordinateParameter() {
        allValues = new ArrayList<>();
    }

    @Override
    public Collection<String> getRelevantValues(CommandSender sender) {
        List<String> res = new ArrayList<>(allValues);
        if (sender instanceof Player) {
            res.add("~");
            res.add("-~");
        }
        return res;
    }
}
