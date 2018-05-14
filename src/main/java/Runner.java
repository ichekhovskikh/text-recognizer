import nlp.NlpSentence;
import nlp.NlpWords;
import nlp.analyzers.NlpParseException;
import ontology.OntologyController;
import org.maltparser.core.exception.MaltChainedException;
import view.TextView;
import view.WordsView;

import java.io.IOException;
import java.net.URISyntaxException;

public class Runner {
    public static void main(String[] args) throws URISyntaxException, MaltChainedException, NlpParseException, IOException {
        NlpWords nlpWords = new NlpWords();
        OntologyController controller = new OntologyController();
        TextView view = new TextView(controller, nlpWords);
    }
}
