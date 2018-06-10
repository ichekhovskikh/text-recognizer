package ontology;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

public class DefaultOntologyModelFactory implements OntologyModelFactory {
    public static final String DEFAULT_PATH_ONTOLOGY = "geometries.rdf";

    public String getDefaultOntologyPath() {
        return DEFAULT_PATH_ONTOLOGY;
    }

    public OntModel createOntModel(String pathOntology){
        Model model = FileManager.get().loadModel(pathOntology);
        return ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
    }

    public InfModel createRDFSModel(String pathOntology){
        Model model = FileManager.get().loadModel(pathOntology);
        return ModelFactory.createRDFSModel(model);
    }
}
