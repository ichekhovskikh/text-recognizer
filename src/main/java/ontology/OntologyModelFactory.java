package ontology;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.InfModel;

public interface OntologyModelFactory {
    String getDefaultOntologyPath();
    OntModel createOntModel(String pathOntology);
    InfModel createRDFSModel(String pathOntology);
}
