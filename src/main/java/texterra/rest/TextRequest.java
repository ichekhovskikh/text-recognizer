package texterra.rest;

import com.google.gson.annotations.SerializedName;

public class TextRequest {
    @SerializedName("text")
    public String text;

    public TextRequest() {
        text = "";
    }

    public TextRequest(String text) {
        this.text = text;
    }
}
