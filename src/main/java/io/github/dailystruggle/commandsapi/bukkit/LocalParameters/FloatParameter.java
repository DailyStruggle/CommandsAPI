package io.github.dailystruggle.commandsapi.bukkit.LocalParameters;

import io.github.dailystruggle.commandsapi.bukkit.BukkitParameter;
import org.bukkit.command.CommandSender;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class FloatParameter extends BukkitParameter {
    List<String> allValues;
    public FloatParameter(BiFunction<CommandSender, String, Boolean> isRelevant, Vector<Double> range) {
        super(isRelevant);
        allValues = new ArrayList<>();
        int maxSteps = 20;
        double span = range.lastElement() - range.firstElement();
        double step = span/maxSteps;
        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        for(double i = range.firstElement(); i < range.lastElement(); i = (step>1) ? i+1 : i+step) {
            allValues.add(String.valueOf(decimalFormat.format(i)));
        }
    }

    public FloatParameter(BiFunction<CommandSender, String, Boolean> isRelevant, Object... options) {
        super(isRelevant);
        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        allValues = Arrays.stream(options).map(decimalFormat::format).collect(Collectors.toList());
    }

    @Override
    public Collection<String> values() {
        return allValues;
    }
}
