package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.dtos.PdIDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProcesadorPdIClient {

	@POST("api/pdis")
	Call<PdIDTO> procesar(@Body PdIDTO pdIDTO);

	@GET("api/pdis")
	Call<java.util.List<PdIDTO>> listarPorHecho(@Query("hecho") String hechoId);
}


