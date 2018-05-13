package ontology.graph;

import nlp.NamedTag;
import nlp.NlpUtils;

public class OntologyVertex {
    private String text;
    private NamedTag tag;

    public OntologyVertex(String text, NamedTag tag) {
        this.text = text;
        this.tag = tag;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public NamedTag getTag() {
        return tag;
    }

    public void setTag(NamedTag tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return text + " [" + NlpUtils.getlocalizeName(tag) + "]";
    }
}
