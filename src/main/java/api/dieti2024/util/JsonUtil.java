package api.dieti2024.util;
import api.dieti2024.exceptions.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

public class JsonUtil {
    // Costruttore privato per impedire l'istanziazione
    private JsonUtil() {
        throw new IllegalStateException("Utility class");
    }
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new ApiException("Errore nella conversione dell'oggetto in JSON", HttpStatus.BAD_REQUEST);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new ApiException("Errore nella conversione del JSON in oggetto", HttpStatus.BAD_REQUEST);
        }
    }

}
