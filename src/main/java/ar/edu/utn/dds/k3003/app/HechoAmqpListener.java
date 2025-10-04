package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.dtos.HechoDTO;
// import ar.edu.utn.dds.k3003.dtos.ColeccionDTO;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.NoSuchElementException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "amqp", name = "enabled", havingValue = "true")
public class HechoAmqpListener {

    private final IFachadaFuente fachadaFuente;
    private final MeterRegistry meterRegistry;
    private final ObjectMapper objectMapper;
    @Value("${amqp.create-enabled:true}")
    private boolean createEnabled;

    public HechoAmqpListener(IFachadaFuente fachadaFuente, MeterRegistry meterRegistry, ObjectMapper objectMapper) {
        this.fachadaFuente = fachadaFuente;
        this.meterRegistry = meterRegistry;
        this.objectMapper = objectMapper;
    }

    // The message converter is configured to use Jackson and snake_case via JacksonConfig
    @RabbitListener(queues = "${amqp.queue:hechos.queue}", containerFactory = "hechosListenerFactory")
    public void onHechoMessage(Message message) {
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);
        System.out.println("Recibiendo mensaje AMQP: " + payload);

        HechoDTO hechoDTO;
        try {
            hechoDTO = objectMapper.readValue(payload, HechoDTO.class);
        } catch (JsonProcessingException e) {
            System.out.println("Mensaje no reconocido, se ignora");
            meterRegistry.counter("hechos_queue_created", "status", "ignored").increment();
            return;
        }

        System.out.println("Recibiendo hecho: " + hechoDTO);
        if (!createEnabled) {
          System.out.println("Creación de hechos por AMQP deshabilitada (amqp.create-enabled=false)");
          meterRegistry.counter("hechos_queue_created", "status", "skipped").increment();
          return;
        }
        // Procesa y registra métricas de éxito/fracaso
        try {
            fachadaFuente.buscarColeccionXNombre(hechoDTO.nombre_coleccion());
            fachadaFuente.agregar(hechoDTO);
            meterRegistry.counter("hechos_queue_created", "status", "success").increment();
        } catch (NoSuchElementException e) {
            System.out.println("Colección no existe: " + hechoDTO.nombre_coleccion());
            meterRegistry.counter("hechos_queue_created", "status", "failed").increment();
        } catch (Exception e) {
            meterRegistry.counter("hechos_queue_created", "status", "failed").increment();
        }
    }
}


