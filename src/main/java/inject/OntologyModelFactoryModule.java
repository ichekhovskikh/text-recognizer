package inject;

import com.google.inject.Binder;
import com.google.inject.Module;
import ontology.DefaultOntologyModelFactory;
import ontology.OntologyModelFactory;

public class OntologyModelFactoryModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(OntologyModelFactory.class).to(DefaultOntologyModelFactory.class);
    }
}
