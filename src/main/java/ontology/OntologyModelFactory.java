package ontology;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

public class OntologyModelFactory {
    public static final String DEFAULT_PATH_ONTOLOGY = "geometries.rdf";
    private static final Model DEFAULT_MODEL;

    static {
        DEFAULT_MODEL = FileManager.get().loadModel(DEFAULT_PATH_ONTOLOGY);
    }

    private OntologyModelFactory(){
    }

    public static OntModel createOntModel(){
        return ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, DEFAULT_MODEL);
    }

    public static OntModel createOntModel(String pathOntology){
        Model model = FileManager.get().loadModel(pathOntology);
        return ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
    }

    public static InfModel createRDFSModel(){
        return ModelFactory.createRDFSModel(DEFAULT_MODEL);
    }

    public static InfModel createRDFSModel(String pathOntology){
        Model model = FileManager.get().loadModel(pathOntology);
        return ModelFactory.createRDFSModel(model);
    }
}
