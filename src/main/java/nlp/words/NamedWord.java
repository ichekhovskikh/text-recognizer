package nlp.words;

import nlp.NamedTag;

public class NamedWord extends SyntaxWord {
    protected String type = null;
    protected NamedTag namedTag = null;

    public NamedWord(
            String rawResponse,
            int index,
            int headIndex,
            String label,
            NamedTag namedTag,
            String type) {
        super(rawResponse, index, headIndex, label);
        this.namedTag = namedTag;
        this.type = type;
    }

    public NamedWord(
            SerializedWord serializeWordInfo,
            int index,
            int headIndex,
            String label,
            NamedTag namedTag,
            String type) {
        super(serializeWordInfo, index, headIndex, label);
        this.namedTag = namedTag;
        this.type = type;
    }

    public NamedWord(
            MorphWord morphWord,
            int index,
            int headIndex,
            String label,
            NamedTag namedTag,
            String type) {
        super(
                morphWord.text,
                morphWord.initial,
                morphWord.tag,
                morphWord.feats,
                index,
                headIndex,
                label);
        this.namedTag = namedTag;
        this.type = type;
    }

    public NamedWord(
            String text,
            String initial,
            String tag,
            String feats,
            int index,
            int headIndex,
            String label,
            NamedTag namedTag,
            String type) {
        super(text, initial, tag, feats, index, headIndex, label);
        this.namedTag = namedTag;
        this.type = type;
    }


    public NamedWord(SyntaxWord syntaxWord, NamedTag namedTag, String type) {
        super(
                syntaxWord.text,
                syntaxWord.initial,
                syntaxWord.tag,
                syntaxWord.feats,
                syntaxWord.index,
                syntaxWord.headIndex,
                syntaxWord.label);
        this.namedTag = namedTag;
        this.type = type;
    }
}
