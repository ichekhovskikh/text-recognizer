package nlp.analyzers;

import com.google.common.collect.Lists;
import nlp.NlpSentence;
import ru.stachek66.nlp.mystem.holding.Factory;
import ru.stachek66.nlp.mystem.holding.MyStem;
import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import ru.stachek66.nlp.mystem.holding.Request;
import ru.stachek66.nlp.mystem.model.Info;
import scala.Option;
import scala.collection.JavaConversions;

import java.util.List;

public class MystemMorphAnalyzer implements NlpAnalyzer<Info> {
    private MyStem mystemAnalyzer = null;

    public MystemMorphAnalyzer() {
        mystemAnalyzer = new Factory("-igd --format json")
                .newMyStem("3.0", Option.empty()).get();
    }

    public List<Info> parse(NlpSentence sentence) throws NlpParseException {
        try {
            return Lists.newArrayList(
                    JavaConversions.asJavaIterable(mystemAnalyzer
                            .analyze(Request.apply(sentence.getText()))
                            .info()
                            .toIterable()));
        } catch (MyStemApplicationException e) {
            throw new NlpParseException(e);
        }
    }
}
