package nlp.tree;

import java.util.List;

public interface NlpTreeObject {
    void addRelation(NlpTreeRelation relation);
    String getText();
    void setText(String text);
    String getTag();
    void setTag(String tag);
    List<NlpTreeRelation> getRelations();
    void setRelations(List<NlpTreeRelation> relations);
    boolean hasRelation();
    @Override
    boolean equals(Object obj);
}
