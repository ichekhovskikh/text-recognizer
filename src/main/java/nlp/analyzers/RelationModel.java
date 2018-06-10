package nlp.analyzers;

import java.util.List;
import java.util.Map;

public interface RelationModel {
    Map<String, List<String>> getRelations();
    void setRelations(Map<String, List<String>> relations);
    void addRelation(String key, String value);
    void putRelations(String key, List<String> values);
    List<String> getValues(String key);
    List<String> getKeys();
}
