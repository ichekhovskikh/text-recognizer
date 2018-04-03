package texterra.rest;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;
import texterra.AnnotationEntity;
import texterra.NamedAnnotationEntity;

import java.util.List;

public interface TexterraApi {
    //@FormUrlEncoded
 //   @Multipart
    @POST("nlp?targetType=named-entity")
    Call<List<NamedAnnotationEntity>> getFromNLP(@Body List<TextRequest> texts);
}
