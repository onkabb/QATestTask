import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PositiveTests {

    // Установка URL по умолчанию
    @BeforeAll
    static void beforeAll() {
        Unirest.config().defaultBaseUrl("https://regions-test.2gis.com/v1/favorites");
    }

    RequestData data = new RequestData();
    TokenGetter tokenGetter = new TokenGetter();

    // Тест на совпадение возвращаемых результатов
    @Test
    void basicTest() {
        HttpResponse<JsonNode> res = Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", tokenGetter.getToken())
                .field("title", data.title)
                .field("lat", String.valueOf(data.lat))
                .field("lon", String.valueOf(data.lon))
                .field("color", data.color)
                .asJson();
        assertEquals(200, res.getStatus());
        assertEquals(data.title, res.getBody().getObject().get("title"));
        assertEquals(data.lat, Double.parseDouble(res.getBody().getObject().get("lat").toString()));
        assertEquals(data.lon, Double.parseDouble(res.getBody().getObject().get("lon").toString()));
        assertEquals(data.color, res.getBody().getObject().get("color"));
    }

    // Тестирование ID на автоинкремент
    @Test
    void testId() {
        ArrayList<HttpResponse<JsonNode>> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(Unirest.post("https://regions-test.2gis.com/v1/favorites")
                    .header("Cookie", tokenGetter.getToken())
                    .field("title", data.title)
                    .field("lat", String.valueOf(data.lat))
                    .field("lon", String.valueOf(data.lon))
                    .field("color", data.color)
                    .asJson());
            if (i > 0) {
                assertTrue(Long.parseLong(list.get(i - 1).getBody().getObject().get("id").toString()) < Long.parseLong(list.get(i).getBody().getObject().get("id").toString()));
            }
        }
    }

    // Проверка латинских и кириллических символов, цифр и знаков
    @ParameterizedTest
    @ValueSource(strings = {"Place", "Место", "123", "!@#$%^&*()_+-=|{}[];':/?"})
    void positiveTestTitle(String string) {
        HttpResponse<JsonNode> res = Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", tokenGetter.getToken())
                .field("title", string)
                .field("lat", String.valueOf(data.lat))
                .field("lon", String.valueOf(data.lon))
                .field("color", data.color)
                .asJson();
        assertEquals(200, res.getStatus());
        assertEquals(string, res.getBody().getObject().get("title"));
    }

    // Тест в допустимой длине строки
    @Test
    void positiveTestTitleLength() {
        String str = new String(new char[999]).replace("\0", "A");

        HttpResponse<JsonNode> res = Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", tokenGetter.getToken())
                .field("title", str)
                .field("lat", String.valueOf(data.lat))
                .field("lon", String.valueOf(data.lon))
                .field("color", data.color)
                .asJson();
        assertEquals(200, res.getStatus());
        assertEquals(str, res.getBody().getObject().get("title"));

        str = new String(new char[1]).replace("\0", "A");

        res = Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", tokenGetter.getToken())
                .field("title", str)
                .field("lat", String.valueOf(data.lat))
                .field("lon", String.valueOf(data.lon))
                .field("color", data.color)
                .asJson();
        assertEquals(200, res.getStatus());
        assertEquals(str, res.getBody().getObject().get("title"));
    }

    // Тестирование допустимых граничных значений широты
    @ParameterizedTest
    @ValueSource(doubles = {-90, 90, -89, 89, 0})
    void positiveTestLat(double lat) {
        HttpResponse<JsonNode> res = Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", tokenGetter.getToken())
                .field("title", data.title)
                .field("lat", String.valueOf(lat))
                .field("lon", String.valueOf(data.lon))
                .field("color", data.color)
                .asJson();
        assertEquals(200, res.getStatus());
        assertEquals(lat, Double.parseDouble(res.getBody().getObject().get("lat").toString()));
    }

    // Тестирование допустимых граничных значений долготы
    @ParameterizedTest
    @ValueSource(doubles = {-180, 180, -179, 179, 0})
    void positiveTestLon(double lon) {
        HttpResponse<JsonNode> res = Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", tokenGetter.getToken())
                .field("title", data.title)
                .field("lat", String.valueOf(data.lat))
                .field("lon", String.valueOf(lon))
                .field("color", data.color)
                .asJson();
        assertEquals(200, res.getStatus());
        assertEquals(lon, Double.parseDouble(res.getBody().getObject().get("lon").toString()));
    }

    // Тестирование допустимых цветов
    @ParameterizedTest
    @ValueSource(strings = {"BLUE", "GREEN", "RED", "YELLOW"})
    void positiveTestColor(String color) {
        HttpResponse<JsonNode> res = Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", tokenGetter.getToken())
                .field("title", data.title)
                .field("lat", String.valueOf(data.lat))
                .field("lon", String.valueOf(data.lon))
                .field("color", color)
                .asJson();
        assertEquals(200, res.getStatus());
        assertEquals(color, res.getBody().getObject().get("color").toString());
    }

    // Тестирование значение по умолчанию
    @Test
    void positiveTestColor() {
        HttpResponse<JsonNode> res = Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", tokenGetter.getToken())
                .field("title", data.title)
                .field("lat", String.valueOf(data.lat))
                .field("lon", String.valueOf(data.lon))
                .asJson();
        assertEquals(200, res.getStatus());
        assertNull(res.getBody().getObject().get("color"));
    }

    // Тестирование даты создания на факт существования
    @Test
    void positiveTestDate() {
        HttpResponse<JsonNode> res = Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", tokenGetter.getToken())
                .field("title", data.title)
                .field("lat", String.valueOf(data.lat))
                .field("lon", String.valueOf(data.lon))
                .asJson();
        assertEquals(200, res.getStatus());
        assertNotNull(res.getBody().getObject().get("created_at"));
    }
}

