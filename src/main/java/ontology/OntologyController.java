package ontology;

import ontology.graph.OntologyGraph;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;

import java.io.FileNotFoundException;

public interface OntologyController {
    String getFilePath();
    void setFilePath(String filePath);
    String getSource();
    String getIndividualPath();
    String getObjectPropertyPath();
    Individual getIndividual(String individualName);
    ObjectProperty getObjectProperty(String objectPropertyName);
    OntClass createOntClass(String className);
    ObjectProperty createObjectProperty(String propertyName);
    Individual addIndividual(String individualName, String className);
    void addIndividualProperty(Individual subject, ObjectProperty property, Individual object);
    void addIndividualProperty(String subjectName, String objectName, String propertyName);
    void commit();
    void saveOntology(String path) throws FileNotFoundException;
    OntologyGraph getGraph();
}
