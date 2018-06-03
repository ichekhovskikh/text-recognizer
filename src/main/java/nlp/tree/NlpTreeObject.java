package nlp.tree;

import java.util.ArrayList;
import java.util.List;

public class NlpTreeObject {
    private String text;
    private String tag;
    private List<NlpTreeRelation> relations;

    public NlpTreeObject() {
        relations = new ArrayList<>();
    }

    public NlpTreeObject(String text, String tag) {
        this.text = text;
        this.tag = tag;
        relations = new ArrayList<>();
    }

    public NlpTreeObject(String text, String tag, List<NlpTreeRelation> relations) {
        this.text = text;
        this.tag = tag;
        this.relations = relations;
    }

    public void addRelation(NlpTreeRelation relation) {
        if (relations.stream().anyMatch(elem -> elem.equals(relation)))
            return;
        relations.add(relation);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<NlpTreeRelation> getRelations() {
        return relations;
    }

    public void setRelations(List<NlpTreeRelation> relations) {
        this.relations = relations;
    }

    public boolean hasRelation() {
        return !relations.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NlpTreeObject))
            return false;
        else if (this == obj) return true;
        NlpTreeObject nlpTreeObject = (NlpTreeObject)obj;
        return this.text.equals(nlpTreeObject.text) && this.tag.equals(nlpTreeObject.tag);
    }
}
