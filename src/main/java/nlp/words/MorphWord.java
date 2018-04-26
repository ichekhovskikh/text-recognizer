package nlp.words;

public class MorphWord implements InfoWord {
    private int index;
    private String text;
    private String initial;
    private String tag;
    private String feats;
    private Category category;

    public MorphWord() {
    }

    public MorphWord(String text, String initial, String tag, String feats) {
        this.text = text;
        this.initial = initial;
        this.tag = tag;
        this.feats = feats;
        setCategory(this.tag);
    }

    public MorphWord(int index, String text, String initial, String tag, String feats) {
        this.index = index;
        this.text = text;
        this.initial = initial;
        this.tag = tag;
        this.feats = feats;
        setCategory(this.tag);
    }

    public String getText() {
        return text;
    }

    public String getInitial() {
        return initial;
    }

    public String getTag() {
        return tag;
    }

    public String getFeats() {
        return feats;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setFeats(String feats) {
        this.feats = feats;
    }

    public Category getCategory() {
        return category;
    }

    private void setCategory(String tag) {
        for (Category category : Category.values()) {
            if (tag.equals(String.valueOf(category.name().charAt(0)))) {
                this.category = category;
                return;
            }
        }
    }

    public enum Category {
        N_NOUN,
        V_VERB,
        A_ADJECTIVE,
        P_PRONOUN,
        R_ADVERB,
        S_ADPOSITION,
        C_CONJUNCTION,
        M_NUMERAL,
        Q_PARTICLE,
        I_INTERJECTION,
        Y_ABBREVIATION,
        X_RESIDUAL
    }
}
