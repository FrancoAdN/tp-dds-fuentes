package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.dtos.HechoDTO;
// import ar.edu.utn.dds.k3003.dtos.ColeccionDTO;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.NoSuchElementException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "amqp", name = "enabled", havingValue = "true")
public class HechoAmqpListener {

    private final IFachadaFuente fachadaFuente;
    private final MeterRegistry meterRegistry;

    public HechoAmqpListener(IFachadaFuente fachadaFuente, MeterRegistry meterRegistry) {
        this.fachadaFuente = fachadaFuente;
        this.meterRegistry = meterRegistry;
    }

    // The message converter is configured to use Jackson and snake_case via JacksonConfig
    @RabbitListener(queues = "${amqp.queue:hechos.queue}", containerFactory = "hechosListenerFactory")
    public void onHechoMessage(HechoDTO hechoDTO) {
        System.out.println("Recibiendo hecho: " + hechoDTO);
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


