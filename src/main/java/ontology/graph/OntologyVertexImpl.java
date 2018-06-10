package ontology.graph;

import nlp.NamedTag;
import nlp.NlpUtils;

public class OntologyVertexImpl implements OntologyVertex {
    private String text;
    private NamedTag tag;

    public OntologyVertexImpl(String text, NamedTag tag) {
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
        return text + " [" + NlpUtils.getLocalizeName(tag) + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof OntologyVertex))
            return false;
        OntologyVertex vertex = (OntologyVertex) obj;
        return vertex.getTag() == tag && vertex.getText().toUpperCase().equals(text.toUpperCase());
    }
}
