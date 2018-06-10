package ontology.graph;

import nlp.NamedTag;

public interface OntologyVertex {
    void setText(String text);
    String getText();
    NamedTag getTag();
    void setTag(NamedTag tag);
    @Override
    String toString();
    @Override
    boolean equals(Object obj);
}
