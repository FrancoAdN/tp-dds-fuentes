package ar.edu.utn.dds.k3003.config;

import ar.edu.utn.dds.k3003.app.ProcesadorPdIClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RetrofitConfig {

	@Bean
	public Retrofit retrofit(ObjectMapper objectMapper) {
		OkHttpClient httpClient = new OkHttpClient.Builder().build();
		return new Retrofit.Builder()
			.baseUrl("https://two025-tp-anual-sebabalas.onrender.com/")
			.client(httpClient)
			.addConverterFactory(JacksonConverterFactory.create(objectMapper))
			.build();
	}

	@Bean
	public ProcesadorPdIClient procesadorPdIClient(Retrofit retrofit) {
		return retrofit.create(ProcesadorPdIClient.class);
	}
}


