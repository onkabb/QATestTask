import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class TokenGetter {
    private long time = 0;
    private String token = "";

    // Функция получения и обновления токена
    String getToken() {
        HttpResponse<JsonNode> response;
        if ((System.currentTimeMillis() - time) > 1500) {
            response = Unirest.post("https://regions-test.2gis.com/v1/auth/tokens").asJson();
            time = System.currentTimeMillis();
            token = response.getCookies().getNamed("token").toString();
            return token;
        } else {
            return token;
        }
    }
}
