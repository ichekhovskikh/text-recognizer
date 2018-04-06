import nlp.TextService;
import org.annolab.tt4j.TreeTaggerException;
import org.maltparser.core.exception.MaltChainedException;
import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import nlp.texterra.NamedAnnotationEntity;
import nlp.texterra.Texterra;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws MaltChainedException, IOException, URISyntaxException, MyStemApplicationException, TreeTaggerException {
        String text = "Самарская область является частью России.";

        TextService textService = new TextService(text);
        textService.syntaxParsingSentence(0);

        Texterra texterra = new Texterra();
        List<NamedAnnotationEntity> entity = texterra.getNamedAnnotationEntities("Пошли гулять по Москве.");
        int k = text.length();
        /*String s = "1\tГо\tго\tM\tM\tMo---d\t0\tROOT\t_\t_\n2\tбухать\tбухать\tV\tV\tVmn----a-e\t1\tпредик\t_\t_\n";
        String[] ss = s.split("\n");
        List<SyntaxWord> words = new ArrayList<>();
        for(String re : ss) {
            if (re.equals(""))
                continue;
            String[] attrs = re.split("\t");
            SyntaxWord info = new SyntaxWord(attrs[1], attrs[2], attrs[3], attrs[5], Integer.parseInt(attrs[0]), Integer.parseInt(attrs[6]), attrs[7]);
            words.add(info);
        }*/
        //int k = text.length();
        /*List<Info> res = textService.parse("привет мама");
        System.out.println(new Gson().toJson(new nlp.words.MorphWord(res.get(0).rawResponse())));*/
        // List<String> sentenceList = textService.getAllSentences();
        //textService.syntaxParsingSentence(0);
        //textService.syntaxParsingSentence(sentenceList.get(0));
        /*for (String sentence : sentenceList) {
            System.out.println();
            String[] tokens = textService.getTokens(sentence);
            for (int i = 0; i < tokens.length; i++)
                System.out.print(tokens[i] + " ");

        }*/
        // Point TT4J to the TreeTagger installation directory. The executable is expected
        // in the "bin" subdirectory - in this example at "/opt/treetagger/bin/tree-tagger"
        /*System.setProperty("treetagger.home", "/treetagger");
        TreeTaggerWrapper tt = new TreeTaggerWrapper<String>();
        try {
            tt.setModel("russian-utf8.par:utf8");
            tt.setHandler(new TokenHandler<String>() {
                public void token(String token, String pos, String lemma) {
                    System.out.println(token + "\t" + pos + "\t" + lemma);
                }
            });
            tt.process(asList(new String[]{"занимающия", "Она", "c", "другом", "занимается", "ерундой", "и", "больше", "ничем"}));
        } finally {
            tt.destroy();
        }*/
    }
}
