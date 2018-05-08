package ontology;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

public class OntologyModelFactory {
    public static final String PATH_ONTOLOGY = "simple_features_geometries.rdf";
    private static final Model MODEL;

    static {
        MODEL = FileManager.get().loadModel(PATH_ONTOLOGY);
    }

    private OntologyModelFactory(){
    }

    public static OntModel createOntModel(){
        return ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM, MODEL);
    }

    public static InfModel createRDFSModel(){
        return ModelFactory.createRDFSModel(MODEL);
    }
}
