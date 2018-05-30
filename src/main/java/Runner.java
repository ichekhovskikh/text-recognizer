import logging.Logger;
import nlp.NlpController;
import nlp.analyzers.NlpParseException;
import nlp.texterra.TexterraServer;
import ontology.OntologyController;
import org.maltparser.core.exception.MaltChainedException;
import view.TextView;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class Runner {
    public static void main(String[] args)  {
        TexterraServer.start();

        Logger.setStream(System.out);
        Logger.setStatus(true);
        try {
            NlpController nlpController = new NlpController();
            OntologyController ontologyController = new OntologyController();
            TextView view = new TextView(ontologyController, nlpController);
        } catch (org.apache.jena.shared.NotFoundException | NullPointerException | URISyntaxException | MaltChainedException | NlpParseException | IOException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Ошибка запуска [" + e.getMessage() + "]!",
                    "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }
}
