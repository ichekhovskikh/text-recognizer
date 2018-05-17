package nlp.words;

public class SyntaxWord implements InfoWord {
    protected int index;
    protected int headIndex;
    protected String text;
    protected String label;

    public SyntaxWord() {
    }

    public SyntaxWord(int index, int headIndex, String text, String label) {
        this.index = index;
        this.headIndex = headIndex;
        this.label = label;
        this.text = text;
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

    public void setIndex(int index) {
        this.index = index;
    }

    public void setHeadIndex(int headIndex) {
        this.headIndex = headIndex;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return index + " " + text + " " + headIndex + " " + label;
    }
}
