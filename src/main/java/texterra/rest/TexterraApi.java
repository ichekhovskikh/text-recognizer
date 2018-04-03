package texterra.rest;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import texterra.AnnotationEntity;
import texterra.NamedAnnotationEntity;

import java.util.List;

public interface TexterraApi {
    @POST("nlp")
    Call<ResponseBody> getFromNLP(@Query("targetType") String targetType, @Body RequestBody body);
}
