package nlp.texterra;

import java.io.IOException;
import java.util.List;

public interface Texterra {
    List<NamedAnnotationEntity> getNamedAnnotationEntities(String... texts) throws IOException;
    List<NamedAnnotationEntity> getNamedAnnotationEntities(List<String> texts) throws IOException;
}
