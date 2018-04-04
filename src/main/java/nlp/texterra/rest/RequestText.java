package nlp.texterra.rest;

import com.google.gson.annotations.SerializedName;

public class RequestText {

    @SerializedName("text")
    public String text;

    public RequestText() {
        text = "";
    }

    public RequestText(String text) {
        this.text = text;
    }
}
