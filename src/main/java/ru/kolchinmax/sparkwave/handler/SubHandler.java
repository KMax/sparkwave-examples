package ru.kolchinmax.sparkwave.handler;

import at.sti2.spark.core.solution.Match;
import at.sti2.spark.grammar.pattern.Handler;

public interface SubHandler {

    public void invoke(Handler properties, Match match);
    
}
