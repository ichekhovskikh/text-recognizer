import com.google.inject.Guice;
import com.google.inject.Injector;
import inject.*;
import logging.Logger;
import nlp.NlpController;
import nlp.texterra.TexterraServer;
import ontology.DefaultOntologyModelFactory;
import ontology.OntologyController;
import view.TextView;

import javax.swing.*;

public class Runner {
    public static void main(String[] args)  {
        TexterraServer.start();

        Logger.setStream(System.out);
        Logger.setStatus(true);

        Injector injector = Guice.createInjector(
                new TexterraModule("http://localhost", 8082),
                new NlpAnalyzerModule("russian-utf8.par:utf8", "russian.mco", "russian.rel"),
                new NlpDependencyTreeModule(),
                new NlpControllerModule(),
                new OntologyModelFactoryModule(),
                new OntologyControllerModule("http://www.opengis.net/ont/",
                        DefaultOntologyModelFactory.DEFAULT_PATH_ONTOLOGY));
        OntologyController ontologyController = injector.getInstance(OntologyController.class);
        NlpController nlpController = injector.getInstance(NlpController.class);
        try {
            TextView view = new TextView(ontologyController, nlpController);
        } catch (org.apache.jena.shared.NotFoundException | NullPointerException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Ошибка запуска [" + e.getMessage() + "]!",
                    "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }
}
