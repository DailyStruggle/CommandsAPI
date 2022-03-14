package io.github.dailystruggle.commandsapi.bukkit.LocalParameters;

import io.github.dailystruggle.commandsapi.bukkit.BukkitParameter;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class IntegerParameter extends BukkitParameter {
    List<String> allValues;
    public IntegerParameter(BiFunction<CommandSender, String, Boolean> isRelevant, Vector<Integer> range) {
        super(isRelevant);
        allValues = new ArrayList<>();
        int maxSteps = 20;
        double span = range.lastElement() - range.firstElement();
        double step = span/maxSteps;
        for(double i = range.firstElement(); i < range.lastElement(); i = (step>1) ? i+1 : i+step) {
            allValues.add(String.valueOf((int)i));
        }
    }

    public IntegerParameter(BiFunction<CommandSender, String, Boolean> isRelevant, Object... options) {
        super(isRelevant);
        allValues = Arrays.stream(options).map(String::valueOf).collect(Collectors.toList());
    }

    @Override
    public Collection<String> values() {
        return allValues;
    }
}
