package ru.kolchinmax.sparkwave.handler;

import at.sti2.spark.core.solution.Match;
import at.sti2.spark.grammar.pattern.Handler;
import at.sti2.spark.handler.SparkwaveHandler;
import at.sti2.spark.handler.SparkwaveHandlerException;
import java.util.HashMap;
import java.util.Map;

public class ConstructHandler implements SparkwaveHandler {
    
    private static final Map<String, SubHandler> SUBHANDLERS = 
            new HashMap<String, SubHandler>();
    private Handler properties;
    private String name;

    @Override
    public void init(Handler invokerProperties) throws SparkwaveHandlerException {
        this.properties = invokerProperties;
        this.name = invokerProperties.getValue("name");
    }

    @Override
    public void invoke(Match match) throws SparkwaveHandlerException {
        SUBHANDLERS.get(this.name).invoke(properties, match);
    }
    
    public static void registerSubHandler(final String name, 
            final SubHandler handler) {
        SUBHANDLERS.put(name, handler);
    }
    
}
