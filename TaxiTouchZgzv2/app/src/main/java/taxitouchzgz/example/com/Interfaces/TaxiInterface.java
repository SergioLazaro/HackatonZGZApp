package taxitouchzgz.example.com.Interfaces;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.mime.TypedInput;

/**
 * Created by sergiolazaromagdalena on 12/7/15.
 */
public interface TaxiInterface {
    @Headers("Content-Type: text/plain")
    @POST("/data/query/225/sql?origin=original")
    void listTaxiStop(@Body TypedInput string, Callback<Response> callback);;
}
