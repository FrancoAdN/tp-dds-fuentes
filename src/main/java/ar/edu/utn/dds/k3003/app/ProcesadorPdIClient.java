package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.dtos.PdIDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProcesadorPdIClient {

	@POST("api/pdis")
	Call<PdIDTO> procesar(@Body PdIDTO pdIDTO);
}


