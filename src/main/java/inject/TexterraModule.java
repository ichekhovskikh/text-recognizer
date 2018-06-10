package inject;

import com.google.inject.Binder;
import com.google.inject.Module;
import nlp.texterra.Texterra;
import nlp.texterra.TexterraImpl;

public class TexterraModule implements Module {
    private final String host;
    private final int port;

    public TexterraModule(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(Texterra.class).toInstance(new TexterraImpl(host, port));
    }
}
