package nlp.tree;

import java.util.ArrayList;
import java.util.List;

public class NlpTreeRelation {
    private String tag;
    private List<NlpTreeObject> heads;
    private List<NlpTreeObject> dependents;

    public NlpTreeRelation() {
        heads = new ArrayList<>();
        dependents = new ArrayList<>();
    }

    public NlpTreeRelation(String tag) {
        this.tag = tag;
        heads = new ArrayList<>();
        dependents = new ArrayList<>();
    }

    public NlpTreeRelation(String tag, List<NlpTreeObject> heads, List<NlpTreeObject> dependents) {
        this.tag = tag;
        this.heads = heads;
        this.dependents = dependents;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<NlpTreeObject> getHeads() {
        return heads;
    }

    public void setHeads(List<NlpTreeObject> heads) {
        this.heads = heads;
    }

    public List<NlpTreeObject> getDependents() {
        return dependents;
    }

    public void setDependents(List<NlpTreeObject> dependents) {
        this.dependents = dependents;
    }

    public void addHead(NlpTreeObject head) {
        if (heads.stream().anyMatch(elem -> elem.equals(head)))
            return;
        heads.add(head);
        head.addRelation(this);
    }

    public void addDependent(NlpTreeObject dependent) {
        if (dependents.stream().anyMatch(elem -> elem.equals(dependent)))
            return;
        dependents.add(dependent);
        dependent.addRelation(this);
    }

    public boolean hasObjects() {
        return !heads.isEmpty() && !dependents.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NlpTreeRelation))
            return false;
        else if (this == obj) return true;
        NlpTreeRelation nlpTreeRelation = (NlpTreeRelation)obj;
        return this.tag.equals(nlpTreeRelation.tag);
    }
}
