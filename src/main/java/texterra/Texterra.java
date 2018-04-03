package texterra;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.RequestBody;
import texterra.rest.TextRequest;
import texterra.rest.TexterraApi;
import retrofit2.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Texterra {
    private TexterraApi texterraApi = null;

    public Texterra() {
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8082/texterra/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        texterraApi = retrofit.create(TexterraApi.class);
    }

    public List<NamedAnnotationEntity> getNamedAnnotationEntities(String... texts) throws IOException {
        return getNamedAnnotationEntities(Arrays.asList(texts));
    }

    public List<NamedAnnotationEntity> getNamedAnnotationEntities(List<String> texts) throws IOException {
        //String bodyJson = new Gson().toJson(getRequestList(texts));
        //RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), bodyJson);
        Call<List<NamedAnnotationEntity>> call = texterraApi.getFromNLP(getRequestList(texts));
        Response<List<NamedAnnotationEntity>> response = call.execute();
        return response.body();
    }

    private List<TextRequest> getRequestList(List<String> texts) {
        return Lists.transform(texts,
                new Function<String, TextRequest>() {
                    @Nullable
                    @Override
                    public TextRequest apply(@Nullable String text) {
                        return new TextRequest(text);
                    }
                });
    }
}
