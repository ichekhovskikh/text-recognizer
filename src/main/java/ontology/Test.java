package ontology;

import nlp.NamedTag;
import nlp.NlpUtils;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.InfModel;

import java.io.FileNotFoundException;

public class Test {

    public static void main (String[] args) throws FileNotFoundException {
        OntologyController ontologyController = new OntologyController();
        /*ontologyController.addIndividualProperty("Russia", "contains", "Samara");
        System.out.println("=====================================================");
        System.out.println(ontologyController.getIndividual("Russia"));
        ontologyController.commit();*/
        //Graph graph = infmodel.getGraph();
        //graph.add(new Triple());
        //ontModel.write(System.out);
       /* ValidityReport validity = infmodel.validate();
        if (validity.isValid()) {
            System.out.println("OK");
        } else {
            System.out.println("Conflicts");
            for (Iterator i = validity.getReports(); i.hasNext(); )
            {
                System.out.println(" - " + i.next());
            }
        }*/

    }
}
