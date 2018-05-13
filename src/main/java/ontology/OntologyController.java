package ontology;

import nlp.NlpUtils;
import ontology.graph.OntologyEdge;
import ontology.graph.OntologyGraph;
import ontology.graph.OntologyVertex;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class OntologyController {
    private OntModel ontModel;
    private InfModel infModel;

    private final String SOURCE;
    private final String INDIVIDUAL_PATH;
    private final String OBJECT_PROPERTY_PATH;

    public OntologyController() {
        this("http://www.opengis.net/ont/",
                OntologyModelFactory.createOntModel(),
                OntologyModelFactory.createRDFSModel());
    }

    public OntologyController(String source, OntModel ontModel, InfModel infModel){
        this.ontModel = ontModel;
        this.infModel = infModel;
        this.SOURCE = source;
        INDIVIDUAL_PATH = source + "sf#";
        OBJECT_PROPERTY_PATH = source + "geosparql#";
    }

    public Individual getIndividual(String individualName){
        return ontModel.getIndividual(INDIVIDUAL_PATH + individualName);
    }

    public ObjectProperty getObjectProperty(String objectPropertyName){
        return ontModel.getObjectProperty(OBJECT_PROPERTY_PATH + objectPropertyName);
    }

    public Individual addIndividual(String individualName, String className) {
        OntClass classOfIndividual = ontModel.getOntClass(INDIVIDUAL_PATH + className);
        return classOfIndividual.createIndividual(INDIVIDUAL_PATH + individualName);
    }

    public void addIndividualProperty(Individual subject, Individual object, ObjectProperty property){
        ontModel.add(subject, property, object);
    }

    public void addIndividualProperty(String subjectName, String objectName, String propertyName){
        Individual subject = getIndividual(subjectName);
        ObjectProperty property = getObjectProperty(objectName);
        Individual object = getIndividual(propertyName);
        ontModel.add(subject, property, object);
    }

    public void commit() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("src\\main\\resources\\" + OntologyModelFactory.PATH_ONTOLOGY);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ontModel.write(writer);
    }

    public void saveOntology(String path) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(path);
        ontModel.write(writer);
    }

    public OntologyGraph getGraph() {
        OntologyGraph graph = new OntologyGraph();
        ExtendedIterator<Individual> subjectsIterator = ontModel.listIndividuals();
        while (subjectsIterator.hasNext()) {
            Individual subject = subjectsIterator.next();
            OntologyVertex subjectVertex = new OntologyVertex(subject.getLocalName(),
                    NlpUtils.getNamedTag(subject.getOntClass().getLocalName()));

            ExtendedIterator<ObjectProperty> propertiesIterator = ontModel.listObjectProperties();
            while (propertiesIterator.hasNext()) {
                ObjectProperty property = propertiesIterator.next();
                OntologyEdge edge = new OntologyEdge(property.getLocalName());

                NodeIterator objectsIterator = ontModel.listObjectsOfProperty(subject, property);
                while (objectsIterator.hasNext()) {
                    Individual object = objectsIterator.next().as(Individual.class);
                    OntologyVertex objectVertex = new OntologyVertex(object.getLocalName(),
                            NlpUtils.getNamedTag(object.getOntClass().getLocalName()));
                    graph.addEdge(subjectVertex, edge, objectVertex);
                }
            }
        }
        return graph;
    }
}
