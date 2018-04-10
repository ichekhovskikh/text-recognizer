package nlp.texterra;

import com.google.gson.annotations.SerializedName;
import nlp.NamedTag;

import java.util.List;

public class NamedAnnotationEntity implements AnnotationEntity {

    private String text = null;

    private NamedAnnotation annotations = null;

    public NamedAnnotationEntity(){}

    public NamedAnnotationEntity(String text, NamedAnnotation annotations) {
        this.text = text;
        this.annotations = annotations;
    }

    @SerializedName("text")
    public String getText() {
        return text;
    }

    @SerializedName("text")
    public void setText(String text) {
        this.text = text;
    }

    @SerializedName("annotations")
    public NamedAnnotation getAnnotations() {
        return annotations;
    }

    @SerializedName("annotations")
    public void setAnnotations(NamedAnnotation annotations) {
        this.annotations = annotations;
    }

    public class NamedAnnotation {
        @SerializedName("named-entity")
        private List<NamedEntity> namedEntities = null;

        public NamedAnnotation() { }

        public NamedAnnotation(List<NamedEntity> namedEntities) {
            this.namedEntities = namedEntities;
        }

        public List<NamedEntity> getNamedEntities() {
            return namedEntities;
        }

        public void setNamedEntities(List<NamedEntity> namedEntities) {
            this.namedEntities = namedEntities;
        }
    }

    public class NamedEntity {

        @SerializedName("value")
        private NamedType value = null;

        @SerializedName("start")
        private int start;

        @SerializedName("end")
        private int end;

        public NamedEntity() {
        }

        public NamedEntity(NamedType value, int start, int end) {
            this.value = value;
            this.start = start;
            this.end = end;
        }

        public NamedType getValue() {
            return value;
        }

        public void setValue(NamedType value) {
            this.value = value;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }
    }

    public class NamedType {

        private String type = null;

        private NamedTag tag = null;

        public NamedType() { }

        public NamedType(String type, NamedTag tag) {
            this.type = type;
            this.tag = tag;
        }

        @SerializedName("type")
        public String getType() {
            return type;
        }

        @SerializedName("type")
        public void setType(String type) {
            this.type = type;
        }

        @SerializedName("tag")
        public NamedTag getTag() {
            return tag;
        }

        @SerializedName("tag")
        public void setTag(NamedTag tag) {
            this.tag = tag;
        }
    }
}
