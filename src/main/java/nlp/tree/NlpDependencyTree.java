package nlp.tree;

import java.util.List;

public interface NlpDependencyTree {
    List<NlpTreeRelation> getRelations();
    void setRelations(List<NlpTreeRelation> relations);
    List<NlpTreeObject> getObjects();
    void setObjects(List<NlpTreeObject> objects);
    void addObject(NlpTreeObject object);
    void addAllObjects(List<NlpTreeObject> objects);
    void addRelation(NlpTreeRelation relation);
    void addAllRelations(List<NlpTreeRelation> relations);
    void addDependency(NlpTreeObject head, NlpTreeRelation relation);
    void addDependency(NlpTreeRelation relation, NlpTreeObject dependent);
    void addDependency(NlpTreeObject head, NlpTreeRelation relation, NlpTreeObject dependent);
    void clear();
}
