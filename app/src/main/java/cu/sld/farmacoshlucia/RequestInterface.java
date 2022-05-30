package cu.sld.farmacoshlucia;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("vista/api.php")
    Call<JSONResponse> getJSON();
}
