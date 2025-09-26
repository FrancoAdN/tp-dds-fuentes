package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.dtos.ColeccionDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "amqp", name = "enabled", havingValue = "true")
public class HechoAmqpListener {

    private final IFachadaFuente fachadaFuente;

    public HechoAmqpListener(IFachadaFuente fachadaFuente) {
        this.fachadaFuente = fachadaFuente;
    }

    // The message converter is configured to use Jackson and snake_case via JacksonConfig
    @RabbitListener(queues = "${amqp.queue:hechos.queue}", containerFactory = "hechosListenerFactory")
    public void onHechoMessage(HechoDTO hechoDTO) {
        System.out.println("Recibiendo hecho: " + hechoDTO);
        // Asegura que la colección exista; si no, la crea automáticamente
        try {
            fachadaFuente.buscarColeccionXNombre(hechoDTO.nombre_coleccion());
        } catch (java.util.NoSuchElementException e) {
            System.out.println("Colección no existe, creando: " + hechoDTO.nombre_coleccion());
            fachadaFuente.agregar(new ColeccionDTO(hechoDTO.nombre_coleccion(), "creada desde AMQP", null));
        }
        // Persiste el hecho usando la Fachada
        fachadaFuente.agregar(hechoDTO);
    }
}


