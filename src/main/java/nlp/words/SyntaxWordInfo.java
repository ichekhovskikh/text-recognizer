package nlp.words;

public class SyntaxWordInfo extends MorphWordInfo {
    private int index;
    private int headIndex;
    private String label;
    private Category category;

    public SyntaxWordInfo(String rawResponse, int index, int headIndex, String label){
        super(rawResponse);
        this.index = index;
        this.headIndex = headIndex;
        this.label = label;
        setCategory(tag);
    }

    public SyntaxWordInfo(SerializeWordInfo serializeWordInfo, int index, int headIndex, String label) {
        super(serializeWordInfo);
        this.index = index;
        this.headIndex = headIndex;
        this.label = label;
        setCategory(tag);
    }

    public SyntaxWordInfo(MorphWordInfo morphWordInfo, int index, int headIndex, String label) {
        this(morphWordInfo.text, morphWordInfo.initial, morphWordInfo.tag, morphWordInfo.feats, index, headIndex, label);
    }

    public SyntaxWordInfo(String text, String initial, String tag, String feats, int index, int headIndex, String label) {
        super(text, initial, tag, feats);
        this.index = index;
        this.headIndex = headIndex;
        this.label = label;
        setCategory(this.tag);
    }

    public int getIndex() {
        return index;
    }

    public int getHeadIndex() {
        return headIndex;
    }

    public String getLabel() {
        return label;
    }

    public Category getCategory() {
        return category;
    }

    private void setCategory(String tag){
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
