package nlp.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NlpDependencyTreeImpl implements NlpDependencyTree {
    private List<NlpTreeRelation> relations;
    private List<NlpTreeObject> objects;

    public NlpDependencyTreeImpl() {
        relations = new ArrayList<>();
        objects = new ArrayList<>();
    }

    public NlpDependencyTreeImpl(List<NlpTreeRelation> relations, List<NlpTreeObject> objects) {
        this.relations = relations;
        this.objects = objects;
    }

    public List<NlpTreeRelation> getRelations() {
        return relations;
    }

    public void setRelations(List<NlpTreeRelation> relations) {
        this.relations = relations;
    }

    public List<NlpTreeObject> getObjects() {
        return objects;
    }

    public void setObjects(List<NlpTreeObject> objects) {
        this.objects = objects;
    }

    public void addObject(NlpTreeObject object) {
        if (objects.stream().anyMatch(elem -> elem.equals(object)))
            return;
        objects.add(object);
    }

    public void addAllObjects(List<NlpTreeObject> objects) {
        for (NlpTreeObject object : objects)
            addObject(object);
    }

    public void addRelation(NlpTreeRelation relation) {
        if (relations.stream().anyMatch(elem -> elem.equals(relation)))
            return;
        relations.add(relation);
    }

    public void addAllRelations(List<NlpTreeRelation> relations) {
        for (NlpTreeRelation relation : relations)
            addRelation(relation);
    }

    public void addDependency(NlpTreeObject head, NlpTreeRelation relation) {
        NlpTreeRelation finalRelation = relation;
        Optional optional = relations
                .stream()
                .filter(elem -> elem.equals(finalRelation))
                .findFirst();
        if (optional.isPresent()) {
            relation = (NlpTreeRelation) optional.get();
        } else relations.add(relation);

        NlpTreeObject finalHead = head;
        optional = objects
                .stream()
                .filter(elem -> elem.equals(finalHead))
                .findFirst();
        if (optional.isPresent()) {
            head = (NlpTreeObject) optional.get();
        } else objects.add(head);

        relation.addHead(head);
    }

    public void addDependency(NlpTreeRelation relation, NlpTreeObject dependent) {
        NlpTreeRelation finalRelation = relation;
        Optional optional = relations
                .stream()
                .filter(elem -> elem.equals(finalRelation))
                .findFirst();
        if (optional.isPresent()) {
            relation = (NlpTreeRelation) optional.get();
        } else relations.add(relation);

        NlpTreeObject finalDependent = dependent;
        optional = objects
                .stream()
                .filter(elem -> elem.equals(finalDependent))
                .findFirst();
        if (optional.isPresent()) {
            dependent = (NlpTreeObject) optional.get();
        } else objects.add(dependent);

        relation.addDependent(dependent);
    }

    public void addDependency(NlpTreeObject head, NlpTreeRelation relation, NlpTreeObject dependent) {
        NlpTreeRelation finalRelation = relation;
        Optional optional = relations
                .stream()
                .filter(elem -> elem.equals(finalRelation))
                .findFirst();
        if (optional.isPresent()) {
            relation = (NlpTreeRelation) optional.get();
        } else relations.add(relation);

        NlpTreeObject finalHead = head;
        optional = objects
                .stream()
                .filter(elem -> elem.equals(finalHead))
                .findFirst();
        if (optional.isPresent()) {
            head = (NlpTreeObject) optional.get();
        } else objects.add(head);

        NlpTreeObject finalDependent = dependent;
        optional = objects
                .stream()
                .filter(elem -> elem.equals(finalDependent))
                .findFirst();
        if (optional.isPresent()) {
            dependent = (NlpTreeObject) optional.get();
        } else objects.add(dependent);

        relation.addHead(head);
        relation.addDependent(dependent);
    }

    public void clear() {
        relations.clear();
        objects.clear();
    }
}
