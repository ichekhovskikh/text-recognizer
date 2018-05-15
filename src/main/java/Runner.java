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
        NlpController nlpController = new NlpController();
        OntologyController controller = new OntologyController();
        TextView view = new TextView(controller, nlpController);
    }
}
