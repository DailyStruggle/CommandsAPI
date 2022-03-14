package io.github.dailystruggle.commandsapi.common;

import java.util.UUID;

public class CommandsAPI {
    public static char parameterDelimiter = ':';
    public static char multiParameterDelimiter = ',';
    public static final UUID serverId = new UUID(0,0);

    //todo: use these somehow?
    public static final Factory commandFactory = new Factory();
    public static final Factory parameterFactory = new Factory();

    public enum SERVER_TYPE{

    }

    public CommandsAPI() {
    }
}
