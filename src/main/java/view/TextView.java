package view;

import nlp.NlpWords;
import ontology.OntologyController;

public class TextView {
    private OntologyController controller;
    private NlpWords nlpWords;

    public TextView(OntologyController controller, NlpWords nlpWords) {
        this.controller = controller;
        this.nlpWords = nlpWords;
        initComponents();
    }

    private void initComponents() {

    }
}
