package nlp.texterra;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import nlp.texterra.rest.RequestText;
import nlp.texterra.rest.TexterraApi;
import retrofit2.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Texterra {
    private TexterraApi texterraApi = null;
    private Gson parser = null;

    public Texterra() {
        parser = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8082/texterra/")
                .build();
        texterraApi = retrofit.create(TexterraApi.class);
    }

    public List<NamedAnnotationEntity> getNamedAnnotationEntities(String... texts) throws IOException {
        return getNamedAnnotationEntities(Arrays.asList(texts));
    }

    public List<NamedAnnotationEntity> getNamedAnnotationEntities(List<String> texts) throws IOException {
        String bodyJson = parser.toJson(getRequestList(texts));
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), bodyJson);
        Call<ResponseBody> call = texterraApi.getFromNLP("named-entity", body);
        retrofit2.Response<ResponseBody> response = call.execute();
        return parser.fromJson(CharStreams.toString(response.body().charStream()), new TypeToken<List<NamedAnnotationEntity>>(){}.getType());
    }

    private List<RequestText> getRequestList(List<String> texts) {
        return Lists.transform(texts,
                new Function<String, RequestText>() {
                    @Nullable
                    @Override
                    public RequestText apply(@Nullable String text) {
                        return new RequestText(text);
                    }
                });
    }
}
