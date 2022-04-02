package io.github.dailystruggle.commandsapi.bukkit.LocalParameters;

import io.github.dailystruggle.commandsapi.bukkit.BukkitParameter;
import org.bukkit.command.CommandSender;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class EnumParameter<T extends Enum<T>> extends BukkitParameter {
    List<String> allValues;
    public EnumParameter(String permission, String description, BiFunction<CommandSender, String, Boolean> isRelevant) {
        super(permission, description,isRelevant);
        Class<? extends Enum<T>> enumClass = (Class<? extends Enum<T>>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        allValues = Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).collect(Collectors.toList());
    }

    @Override
    public Collection<String> values() {
        return allValues;
    }
}
