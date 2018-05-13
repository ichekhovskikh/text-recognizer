package ontology.graph;

import nlp.NamedTag;

import java.awt.*;

public class GraphUtils {
    private GraphUtils(){}

    public static Color getEntityColor(NamedTag tag){
        Color color;
        switch (tag) {
            case GPE_COUNTRY: {
                color = Color.RED;
                break;
            }
            case GPE_CITY: {
                color = Color.GREEN;
                break;
            }
            case GPE_STATE_PROVINCE: {
                color = Color.LIGHT_GRAY;
                break;
            }
            case LOCATION_RIVER: {
                color = Color.CYAN;
                break;
            }
            case LOCATION_LAKE_SEA_OCEAN: {
                color = Color.BLUE;
                break;
            }
            case LOCATION_REGION: {
                color = Color.ORANGE;
                break;
            }
            case LOCATION_CONTINENT: {
                color = Color.PINK;
                break;
            }
            default: {
                color = Color.WHITE;
                break;
            }
        }
        return color;
    }

    public static OntologyEdge localizeEdge(OntologyEdge edge) {
        String localizeText = edge.getText();
        if (edge.getText().equals("coveredBy")) {
            localizeText = "покрыт";
        }
        else if (edge.getText().equals("covers")) {
            localizeText = "охватывает";
        }
        else if (edge.getText().equals("crosses")) {
            localizeText = "пересекает";
        }
        else if (edge.getText().equals("disconnected")) {
            localizeText = "изолированный от";
        }
        else if (edge.getText().equals("disjoint")) {
            localizeText = "разделяет";
        }
        else if (edge.getText().equals("externallyConnected")) {
            localizeText = "внешне связан";
        }
        else if (edge.getText().equals("contains")) {
            localizeText = "содержит";
        }
        else if (edge.getText().equals("inside")) {
            localizeText = "содержится внутри";
        }
        else if (edge.getText().equals("equals")) {
            localizeText = "равен";
        }
        return new OntologyEdge(localizeText);
    }
}
