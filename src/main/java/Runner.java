import logging.Logger;
import nlp.NlpController;
import nlp.analyzers.NlpParseException;
import nlp.texterra.TexterraServer;
import ontology.OntologyController;
import org.maltparser.core.exception.MaltChainedException;
import view.TextView;

import java.io.IOException;
import java.net.URISyntaxException;

public class Runner {
    public static void main(String[] args) throws URISyntaxException, MaltChainedException, NlpParseException, IOException {
        TexterraServer.start();

        Logger.setStream(System.out);
        Logger.setStatus(true);

        NlpController nlpController = new NlpController();
        OntologyController ontologyController = new OntologyController();
        TextView view = new TextView(ontologyController, nlpController);
    }
}
