package nlp.analyzers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.beans.Transient;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RelationshipModel {
    private Multimap<String, String> relations = null;

    public RelationshipModel(){
        relations = HashMultimap.create();
    }

    public RelationshipModel(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        StringBuilder jsonModel = new StringBuilder();
        for (String line : lines)
            jsonModel.append(line).append(" ");
        relations = new Gson().fromJson(jsonModel.toString(), RelationshipModel.class).relations;
    }

    @SerializedName("relations")
    public Multimap<String, String> getRelations() {
        return relations;
    }

    @SerializedName("relations")
    public void setRelations(Multimap<String, String> relations) {
        this.relations = relations;
    }

    @Transient
    public List<String> getValues(String key) {
        return Lists.newArrayList(relations.get(key));
    }

    @Transient
    public List<String> getKeys() {
        return Lists.newArrayList(relations.keySet());
    }
}