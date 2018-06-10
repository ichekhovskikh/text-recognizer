package nlp.tree;

import java.util.List;

public interface NlpTreeRelation {
    String getTag();
    void setTag(String tag);
    List<NlpTreeObject> getHeads();
    void setHeads(List<NlpTreeObject> heads);
    List<NlpTreeObject> getDependents();
    void setDependents(List<NlpTreeObject> dependents);
    void addHead(NlpTreeObject head);
    void addDependent(NlpTreeObject dependent);
    boolean hasObjects();
    @Override
    boolean equals(Object obj);
}
