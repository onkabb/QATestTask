import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NegativeTests {

    RequestData data = new RequestData();
    TokenGetter tokenGetter = new TokenGetter();

    // Тестирование недопустимых граничных значений длины названия
    @Test
    void negativeTestTitleLength() {
        HttpResponse<JsonNode> res = Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", tokenGetter.getToken())
                .field("title", "")
                .field("lat", String.valueOf(data.lat))
                .field("lon", String.valueOf(data.lon))
                .field("color", data.color)
                .asJson();
        assertEquals(400, res.getStatus());

        String str = new String(new char[1000]).replace("\0", "A");

        res = Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", tokenGetter.getToken())
                .field("title", str)
                .field("lat", String.valueOf(data.lat))
                .field("lon", String.valueOf(data.lon))
                .field("color", data.color)
                .asJson();
        assertEquals(400, res.getStatus()); // При длине 1000 статус 200. В ТЗ было 999.
    }

    //Тестирование недопустимых граничных значений широты
    @ParameterizedTest
    @ValueSource(doubles = {-91, 91, 180, -180})
    void negativeTestLat(double lat) {
        HttpResponse<JsonNode> res = Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", tokenGetter.getToken())
                .field("title", data.title)
                .field("lat", String.valueOf(lat))
                .field("lon", String.valueOf(data.lon))
                .field("color", data.color)
                .asJson();
        assertEquals(400, res.getStatus());
    }

    //Тестирование недопустимых значений широты
    @ParameterizedTest
    @ValueSource(strings = {"", "text"})
    void negativeTestLat(String lat) {
        HttpResponse<JsonNode> res = Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", tokenGetter.getToken())
                .field("title", data.title)
                .field("lat", String.valueOf(lat))
                .field("lon", String.valueOf(data.lon))
                .field("color", data.color)
                .asJson();
        assertEquals(400, res.getStatus());
    }

    //Тестирование недопустимых граничных значений долготы
    @ParameterizedTest
    @ValueSource(doubles = {-181, 181, 1000, -360})
    void negativeTestLon(double lon) {
        HttpResponse<JsonNode> res = Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", tokenGetter.getToken())
                .field("title", data.title)
                .field("lat", String.valueOf(data.lat))
                .field("lon", String.valueOf(lon))
                .field("color", data.color)
                .asJson();
        assertEquals(400, res.getStatus());
    }

    //Тестирование недопустимых значений долготы
    @ParameterizedTest
    @ValueSource(strings = {"", "text"})
    void negativeTestLon(String lon) {
        HttpResponse<JsonNode> res = Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", tokenGetter.getToken())
                .field("title", data.title)
                .field("lat", String.valueOf(data.lat))
                .field("lon", String.valueOf(lon))
                .field("color", data.color)
                .asJson();
        assertEquals(400, res.getStatus());
    }

    // Тестирование допустимых цветов в нижнем регистре.
    // Согласно описанию метода он должнен принимать только BLUE, GREEN, RED, YELLOW.
    // Про независимость от регистра не сказано
    @ParameterizedTest
    @ValueSource(strings = {"blue", "green", "red", "yellow"})
    void negativeTestColor(String color) {
        HttpResponse<JsonNode> res = Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", tokenGetter.getToken())
                .field("title", data.title)
                .field("lat", String.valueOf(data.lat))
                .field("lon", String.valueOf(data.lon))
                .field("color", color)
                .asJson();
        assertEquals(400, res.getStatus()); // Статус 200
    }

    // Тестирование других цветов
    @ParameterizedTest
    @ValueSource(strings = {"#FF0000", "#0000FF", "#008000", "#FFFF00", "AntiqueWhite", "#faebd7",
            "beige", "black", "brown", "CadetBlue", "chocolate", "coral", "CornflowerBlue", "DarkViolet",
            "GhostWhite", "gray", "GreenYellow", "light", "pale", "pink", "plum", "purple", "salmon", "SkyBlue", "violet", "white"})
    void negativeTestColorN(String color) {
        HttpResponse<JsonNode> res = Unirest.post("https://regions-test.2gis.com/v1/favorites")
                .header("Cookie", tokenGetter.getToken())
                .field("title", data.title)
                .field("lat", String.valueOf(data.lat))
                .field("lon", String.valueOf(data.lon))
                .field("color", color)
                .asJson();
        assertEquals(400, res.getStatus()); // Статус 200 при brown
    }

}



