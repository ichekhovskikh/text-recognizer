package nlp.words;

import com.google.gson.annotations.SerializedName;

import java.beans.Transient;

public class SerializeWordInfo implements WordInfo {
    private String text;
    private Analyse[] analysis;

    public SerializeWordInfo() {
    }

    public SerializeWordInfo(String text, Analyse[] analysis) {
        this.text = text;
        this.analysis = analysis;
    }

    public SerializeWordInfo(String text, String gr, String lex) {
        this.text = text;
        this.analysis = new Analyse[] { new Analyse(gr, lex) };
    }

    @SerializedName("text")
    public String getText() {
        return text;
    }


    @SerializedName("text")
    public void setText(String text) {
        this.text = text;
    }

    @SerializedName("analysis")
    public Analyse[] getAnalysis() {
        return analysis;
    }

    @SerializedName("analysis")
    public void setAnalysis(Analyse[] analysis) {
        this.analysis = analysis;
    }

    @Transient
    public Analyse getAnalyse() {
        return (analysis.length < 1) ? null : analysis[0];
    }

    public static class Analyse {

        private String gr;
        private String lex;

        public Analyse(){ }

        public Analyse(String lex, String gr){
            this.lex = lex;
            this.gr = gr;
        }

        @SerializedName("gr")
        public void setGr(String gr){
            this.gr = gr;
        }

        @SerializedName("gr")
        public String getGr(){
            return gr;
        }

        @SerializedName("lex")
        public String getLex() {
            return lex;
        }

        @SerializedName("lex")
        public void setLex(String lex) {
            this.lex = lex;
        }
    }
}
