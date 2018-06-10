package inject;

import com.google.inject.Binder;
import com.google.inject.Module;
import nlp.tree.NlpDependencyTree;
import nlp.tree.NlpDependencyTreeImpl;

public class NlpDependencyTreeModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(NlpDependencyTree.class).to(NlpDependencyTreeImpl.class);
    }
}
