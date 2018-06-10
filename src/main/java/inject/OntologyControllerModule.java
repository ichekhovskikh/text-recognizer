package inject;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;
import ontology.BasicOntologyController;
import ontology.OntologyController;

public class OntologyControllerModule implements Module {
    private final String source;
    private final String path;

    public OntologyControllerModule(String source, String path) {
        this.source = source;
        this.path = path;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(String.class).annotatedWith(Names.named("OntologySource")).toInstance(source);
        binder.bind(String.class).annotatedWith(Names.named("OntologyPath")).toInstance(path);
        binder.bind(OntologyController.class).to(BasicOntologyController.class);
    }
}
