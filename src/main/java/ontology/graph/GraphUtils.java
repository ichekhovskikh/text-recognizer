package ontology.graph;

import nlp.NamedTag;
import nlp.NlpUtils;

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

    public static OntologyEdge getLocalizeEdge(OntologyEdge edge) {
        String localizeText = NlpUtils.getLocalizeType(edge.getText());
        return new OntologyEdgeImpl(localizeText);
    }
}
