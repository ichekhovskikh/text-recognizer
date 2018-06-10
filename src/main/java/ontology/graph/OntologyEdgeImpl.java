package ontology.graph;

public class OntologyEdgeImpl implements OntologyEdge {
    private String text;

    public OntologyEdgeImpl(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
