import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class testFavorites {

    public static class RequestData { // Данные для общих тестов
        String title = "Мое любимое место";
        double lat = 55.036500;
        double lon = 55.036500;
        String color = "RED";
    }

    String getToken() { // Функция получения токена
        HttpResponse<JsonNode> response = Unirest.post("https://regions-test.2gis.com/v1/auth/tokens").asJson();
        return response.getCookies().getNamed("token").toString();

    }

    @BeforeAll
    static void beforeAll() { // Установка URL по умолчанию
        Unirest.config().defaultBaseUrl("https://regions-test.2gis.com/v1/favorites");
    }

    @Test
    void basicTest(){ // Тест на совпадение возвращаемых результатов
        RequestData data = new RequestData();
        Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", getToken())
                .field("title",  data.title)
                .field("lat", String.valueOf(data.lat))
                .field("lon", String.valueOf(data.lon))
                .field("color", data.color)
                .asJson()
                .ifSuccess(json -> {
                    assertEquals(data.title, json.getBody().getObject().get("title"));
                    assertEquals(data.lat, Double.parseDouble(json.getBody().getObject().get("lat").toString()));
                    assertEquals(data.lon, Double.parseDouble(json.getBody().getObject().get("lon").toString()));
                    assertEquals(data.color, json.getBody().getObject().get("color"));
                });
    }

    @Test
    void testId() {  // Тестирование ID на автоинкремент
        RequestData data = new RequestData();
        ArrayList<HttpResponse<JsonNode>> list = new ArrayList<HttpResponse<JsonNode>>();
        for (int i = 0; i < 5; i++) {
            list.add(Unirest.post("https://regions-test.2gis.com/v1/favorites")
                    .header("Cookie", getToken())
                    .field("title",  data.title)
                    .field("lat", String.valueOf(data.lat))
                    .field("lon", String.valueOf(data.lon))
                    .field("color", data.color)
                    .asJson());
            if (i > 0)
                assertTrue(Long.parseLong(list.get(i - 1).getBody().getObject().get("id").toString()) < Long.parseLong(list.get(i).getBody().getObject().get("id").toString()));
        }
    }
}