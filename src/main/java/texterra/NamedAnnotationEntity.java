package texterra;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NamedAnnotationEntity implements AnnotationEntity {
    @SerializedName("text")
    private String text = null;

    @SerializedName("annotations")
    private NamedAnnotation annotation = null;

    public NamedAnnotationEntity(){}

    public NamedAnnotationEntity(String text, NamedAnnotation annotation) {
        this.text = text;
        this.annotation = annotation;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public NamedAnnotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(NamedAnnotation annotation) {
        this.annotation = annotation;
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

        @SerializedName("type")
        private String type = null;

        @SerializedName("tag")
        private Tag tag = null;

        public NamedType() { }

        public NamedType(String type, Tag tag) {
            this.type = type;
            this.tag = tag;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Tag getTag() {
            return tag;
        }

        public void setTag(Tag tag) {
            this.tag = tag;
        }
    }

    public enum Tag {
        PERSON,
        NORP_NATIONALITY,
        NORP_RELIGION,
        NORP_POLITICAL,
        NORP_OTHER,
        FACILITY,
        ORGANIZATION_CORPORATION,
        ORGANIZATION_EDUCATIONAL,
        ORGANIZATION_POLITICAL,
        ORGANIZATION_OTHER,
        GPE_COUNTRY,
        GPE_CITY,
        GPE_STATE_PROVINCE,
        GPE_OTHER,
        LOCATION_RIVER,
        LOCATION_LAKE_SEA_OCEAN,
        LOCATION_REGION,
        LOCATION_CONTINENT,
        LOCATION_OTHER,
        PRODUCT,
        DATE,
        TIME,
        MONEY,
        EVENT,
        PLANT,
        ANIMAL,
        SUBSTANCE,
        DISEASE,
        WORK_OF_ART,
        LANGUAGE,
        GAME
    }
}
