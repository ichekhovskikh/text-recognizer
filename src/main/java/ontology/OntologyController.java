package ontology;

import nlp.NlpUtils;
import ontology.graph.OntologyEdge;
import ontology.graph.OntologyGraph;
import ontology.graph.OntologyVertex;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class OntologyController {
    private OntModel model;

    private final String SOURCE;
    private final String INDIVIDUAL_PATH;
    private final String OBJECT_PROPERTY_PATH;

    public OntologyController() {
        this("http://www.opengis.net/ont/",
                OntologyModelFactory.createOntModel());
    }

    public OntologyController(String source, OntModel model) {
        this.model = model;
        this.SOURCE = source;
        INDIVIDUAL_PATH = source + "sf#";
        OBJECT_PROPERTY_PATH = source + "geosparql#";
    }

    public String getSource() {
        return SOURCE;
    }

    public String getIndividualPath() {
        return INDIVIDUAL_PATH;
    }

    public String getObjectPropertyPath() {
        return OBJECT_PROPERTY_PATH;
    }

    public Individual getIndividual(String individualName) {
        individualName = individualName.toUpperCase();
        return model.getIndividual(INDIVIDUAL_PATH + individualName);
    }

    public ObjectProperty getObjectProperty(String objectPropertyName) {
        return model.getObjectProperty(OBJECT_PROPERTY_PATH + objectPropertyName);
    }

    public Individual addIndividual(String individualName, String className) {
        individualName = individualName.toUpperCase();
        OntClass classOfIndividual = model.getOntClass(INDIVIDUAL_PATH + className);
        return classOfIndividual.createIndividual(INDIVIDUAL_PATH + individualName);
    }

    public void addIndividualProperty(Individual subject, ObjectProperty property, Individual object) {
        model.add(subject, property, object);
    }

    public void addIndividualProperty(String subjectName, String objectName, String propertyName) {
        Individual subject = getIndividual(subjectName);
        ObjectProperty property = getObjectProperty(propertyName);
        Individual object = getIndividual(objectName);
        model.add(subject, property, object);
    }

    public void commit() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("src\\main\\resources\\" + OntologyModelFactory.PATH_ONTOLOGY);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        model.write(writer);
    }

    public void saveOntology(String path) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(path);
        model.write(writer);
    }

    public OntologyGraph getGraph() {
        OntologyGraph graph = new OntologyGraph();
        ExtendedIterator<Individual> subjectsIterator = model.listIndividuals();
        while (subjectsIterator.hasNext()) {
            Individual subject = subjectsIterator.next();
            OntologyVertex subjectVertex = new OntologyVertex(subject.getLocalName(),
                    NlpUtils.getNamedTag(subject.getOntClass().getLocalName()));

            ExtendedIterator<ObjectProperty> propertiesIterator = model.listObjectProperties();
            while (propertiesIterator.hasNext()) {
                ObjectProperty property = propertiesIterator.next();
                OntologyEdge edge = new OntologyEdge(property.getLocalName());

                NodeIterator objectsIterator = model.listObjectsOfProperty(subject, property);
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
