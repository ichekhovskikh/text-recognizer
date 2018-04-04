package nlp.words;

public class SyntaxWord extends MorphWord {
    protected int index;
    protected int headIndex;
    protected String label;
    protected Category category;

    public SyntaxWord(String rawResponse, int index, int headIndex, String label){
        super(rawResponse);
        this.index = index;
        this.headIndex = headIndex;
        this.label = label;
        setCategory(tag);
    }

    public SyntaxWord(SerializedWord serializeWordInfo, int index, int headIndex, String label) {
        super(serializeWordInfo);
        this.index = index;
        this.headIndex = headIndex;
        this.label = label;
        setCategory(tag);
    }

    public SyntaxWord(
            MorphWord morphWord,
            int index,
            int headIndex,
            String label) {
        this(
                morphWord.text,
                morphWord.initial,
                morphWord.tag,
                morphWord.feats,
                index,
                headIndex,
                label);
    }

    public SyntaxWord(
            String text,
            String initial,
            String tag,
            String feats,
            int index,
            int headIndex,
            String label) {
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
