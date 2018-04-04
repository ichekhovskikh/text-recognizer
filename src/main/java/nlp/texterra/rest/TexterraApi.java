package nlp.texterra.rest;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface TexterraApi {
    @POST("nlp")
    Call<ResponseBody> getFromNLP(@Query("targetType") String targetType, @Body RequestBody body);
}
