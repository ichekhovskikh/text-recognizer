package inject;

import com.google.inject.Binder;
import com.google.inject.Module;
import nlp.BasicNlpController;
import nlp.NlpController;

public class NlpControllerModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(NlpController.class).to(BasicNlpController.class);
    }
}
