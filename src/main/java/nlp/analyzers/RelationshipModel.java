package nlp.analyzers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationshipModel {
    private Map<String, List<String>> relations = null;

    public RelationshipModel(){
        relations = new HashMap<>();
    }

    public RelationshipModel(String path) throws IOException, URISyntaxException {
        Path relPath = new File(getClass()
                .getClassLoader()
                .getResource(path)
                .toURI()).toPath();

        List<String> lines = Files.readAllLines(relPath, Charset.forName("windows-1251"));
        StringBuilder jsonModel = new StringBuilder();
        for (String line : lines)
            jsonModel.append(line).append(" ");
        relations = new Gson().fromJson(jsonModel.toString(), RelationshipModel.class).relations;
    }

    @SerializedName("relations")
    public Map<String, List<String>> getRelations() {
        return relations;
    }

    @SerializedName("relations")
    public void setRelations(Map<String, List<String>> relations) {
        this.relations = relations;
    }

    @Transient
    public void addRelation(String key, String value) {
        if (relations.containsKey(key))
            relations.get(key).add(value);
        else relations.put(key, Lists.newArrayList(value));
    }

    @Transient
    public List<String> getValues(String key) {
        return relations.get(key);
    }

    @Transient
    public List<String> getKeys() {
        return Lists.newArrayList(relations.keySet());
    }
}