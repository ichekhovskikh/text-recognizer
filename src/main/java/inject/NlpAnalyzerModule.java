package inject;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;
import nlp.analyzers.NlpAnalyzer;
import nlp.analyzers.RelationAnalyzer;
import nlp.analyzers.SyntaxAnalyzer;
import nlp.analyzers.TreeTaggerMorphAnalyzer;

public class NlpAnalyzerModule implements Module {
    private final String morphModelPath;
    private final String syntaxModelPath;
    private final String relationModelPath;

    public NlpAnalyzerModule(String morphModelPath, String syntaxModelPath, String relationModelPath) {
        this.morphModelPath = morphModelPath;
        this.syntaxModelPath = syntaxModelPath;
        this.relationModelPath = relationModelPath;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(String.class).annotatedWith(Names.named("MorphModelPath")).toInstance(morphModelPath);
        binder.bind(String.class).annotatedWith(Names.named("SyntaxModelPath")).toInstance(syntaxModelPath);
        binder.bind(String.class).annotatedWith(Names.named("RelationModelPath")).toInstance(relationModelPath);
        binder.bind(NlpAnalyzer.class).annotatedWith(Names.named("MorphAnalyzer")).to(TreeTaggerMorphAnalyzer.class);
        binder.bind(NlpAnalyzer.class).annotatedWith(Names.named("SyntaxAnalyzer")).to(SyntaxAnalyzer.class);
        binder.bind(NlpAnalyzer.class).annotatedWith(Names.named("RelationAnalyzer")).to(RelationAnalyzer.class);
    }
}
