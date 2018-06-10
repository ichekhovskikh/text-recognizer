package ontology;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import nlp.NlpUtils;
import ontology.graph.*;
import org.apache.jena.ontology.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class BasicOntologyController implements OntologyController {
    private final OntologyModelFactory ontologyFactory;
    private String filePath;
    private OntModel model;

    private final String SOURCE;
    private final String INDIVIDUAL_PATH;
    private final String OBJECT_PROPERTY_PATH;

    @Inject
    public BasicOntologyController(@Named("OntologySource")String source,
                                   @Named("OntologyPath")String path,
                                   OntologyModelFactory ontologyFactory) {
        this.filePath = path;
        this.ontologyFactory = ontologyFactory;
        this.model = this.ontologyFactory.createOntModel(filePath);
        this.SOURCE = source;
        this.INDIVIDUAL_PATH = source + "sf#";
        this.OBJECT_PROPERTY_PATH = source + "geosparql#";
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
        model = this.ontologyFactory.createOntModel(this.filePath);
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

    public OntClass createOntClass(String className) {
        return model.createClass(INDIVIDUAL_PATH + className);
    }

    public ObjectProperty createObjectProperty(String propertyName) {
        return model.createObjectProperty(OBJECT_PROPERTY_PATH + propertyName);
    }

    public Individual addIndividual(String individualName, String className) {
        individualName = individualName.toUpperCase();
        OntClass classOfIndividual = model.getOntClass(INDIVIDUAL_PATH + className);
        if (classOfIndividual == null)
            classOfIndividual = createOntClass(className);
        return classOfIndividual.createIndividual(INDIVIDUAL_PATH + individualName);
    }

    public void addIndividualProperty(Individual subject, ObjectProperty property, Individual object) {
        model.add(subject, property, object);
    }

    public void addIndividualProperty(String subjectName, String objectName, String propertyName) {
        Individual subject = getIndividual(subjectName);
        ObjectProperty property = getObjectProperty(propertyName);
        if (property == null)
            property = createObjectProperty(propertyName);
        Individual object = getIndividual(objectName);
        model.add(subject, property, object);
    }

    public void commit() {
        PrintWriter writer = null;
        try {
            String path = filePath.equals(ontologyFactory.getDefaultOntologyPath()) ?
                    "src\\main\\resources\\" + filePath : filePath;
            writer = new PrintWriter(path);
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
        OntologyGraph graph = new OntologyGraphImpl();
        model.listIndividuals().forEachRemaining(subject -> {
            OntologyVertex subjectVertex = new OntologyVertexImpl(subject.getLocalName(),
                    NlpUtils.getNamedTag(subject.getOntClass().getLocalName()));
            model.listObjectProperties().forEachRemaining(property -> {
                OntologyEdge edge = new OntologyEdgeImpl(property.getLocalName());
                model.listObjectsOfProperty(subject, property).forEachRemaining(object -> {
                    Individual individual = object.as(Individual.class);
                    OntologyVertex objectVertex = new OntologyVertexImpl(individual.getLocalName(),
                            NlpUtils.getNamedTag(individual.getOntClass().getLocalName()));
                    graph.addEdge(subjectVertex, edge, objectVertex);
                });
            });
        });
        return graph;
    }
}
