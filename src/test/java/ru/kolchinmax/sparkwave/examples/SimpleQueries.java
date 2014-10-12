package ru.kolchinmax.sparkwave.examples;

import at.sti2.spark.core.solution.Match;
import at.sti2.spark.core.stream.Triple;
import at.sti2.spark.core.triple.RDFTriple;
import at.sti2.spark.core.triple.TripleCondition;
import at.sti2.spark.grammar.SparkParserException;
import at.sti2.spark.grammar.SparkPatternParser;
import at.sti2.spark.grammar.pattern.Handler;
import at.sti2.spark.grammar.pattern.Pattern;
import at.sti2.sparkwave.SparkwaveNetwork;
import at.sti2.sparkwave.input.N3FileInput;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import ru.kolchinmax.sparkwave.handler.ConstructHandler;
import ru.kolchinmax.sparkwave.handler.SubHandler;

public class SimpleQueries {

    @Test(timeout = 15000)
    public void simple() throws IOException, SparkParserException,
            InterruptedException, URISyntaxException {
        Pattern query = readPattern("/queries/simple-pattern.tpg");

        List<RDFTriple> triples = readTriples("/streams/simple-stream.n3");
        System.out.println("Triples read: " + triples.size());

        SparkwaveNetwork network = new SparkwaveNetwork(query);
        network.init();
        
        Listener listener = new Listener();
        ConstructHandler.registerSubHandler("SimpleQueries#simple", listener);

        for (RDFTriple t : triples) {
            network.activateNetwork(new Triple(t, System.currentTimeMillis(), 0l));
        }
    }

    @Test(timeout = 15000)
    public void knosis() throws URISyntaxException, IOException,
            SparkParserException, InterruptedException {
        List<RDFTriple> triples = readTriples("/streams/knosis-stream.n3");
        System.out.println("Triples read: " + triples.size());

        Pattern query = readPattern("/queries/knosis-pattern.tpg");

        SparkwaveNetwork network = new SparkwaveNetwork(query);
        network.init();
        
        Listener listener = new Listener();
        ConstructHandler.registerSubHandler("SimpleQueries#knosis", listener);

        for (RDFTriple triple : triples) {
            network.activateNetwork(new Triple(triple, System.currentTimeMillis(), 0l));
        }
    }

    private class Listener implements SubHandler {

        @Override
        public void invoke(Handler properties, Match match) {
            final List<TripleCondition> conditions
                    = properties.getTriplePatternGraph().getConstruct().getConditions();
            System.out.println(match.outputNTriples(conditions));
        }
    }

    private Pattern readPattern(final String path)
            throws IOException, SparkParserException {
        SparkPatternParser queryParser = new SparkPatternParser();
        File queryFile = FileUtils.toFile(
                this.getClass().getResource(path));
        return queryParser.parse(queryFile).getPattern();
    }

    private List<RDFTriple> readTriples(final String path)
            throws URISyntaxException {
        N3FileInput i = new N3FileInput(
                this.getClass().getResource(path).toURI().toASCIIString());
        i.parseTriples();
        return i.getTriples();
    }
}
