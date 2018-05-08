package ontology;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.InfModel;

public class Test {

    public static void main (String[] args) {
        OntModel ontModel = OntologyModelFactory.createOntModel();
        InfModel infmodel = OntologyModelFactory.createRDFSModel();

        //Graph graph = infmodel.getGraph();
        //graph.add(new Triple());
        ontModel.write(System.out);
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
