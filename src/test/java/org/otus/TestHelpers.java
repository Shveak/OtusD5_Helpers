package org.otus;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.TestNGCitrusSupport;
import com.google.gson.Gson;
import org.otus.pojo.ErrorResponse;
import org.otus.pojo.Rating;
import org.otus.pojo.User;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class TestHelpers extends TestNGCitrusSupport {

    private TestContext context;

    @Test(description = "Проверяем endpoint '/user' с условленным параметром 'mock'")
    @CitrusTest
    public void getTestUserMock() {

        $(http()
                .client("restClientReqres")
                .send()
                .get("user/mock"));

        $(http().client("restClientReqres")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body("{\n" +
                        "    \"name\": \"Заглушка\",\n" +
                        "    \"score\": 555\n" +
                        "}")
        );
    }

    @Test(description = "Проверяем endpoint '/user' с отсутсвующим пользователем")
    @CitrusTest
    public void getTestUserMockError() {

        $(http()
                .client("restClientReqres")
                .send()
                .get("user/mock15"));

        $(http().client("restClientReqres")
                .receive()
                .response(HttpStatus.BAD_REQUEST)
                .message()
                .type(MessageType.JSON)
                .body(new Gson().toJson(new ErrorResponse()
                        .error("Плохой запрос")
                        .status(400)
                        .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                        .message("указанный пользователь отсутствует")))
        );
    }

    @Test(description = "Проверяем endpoint '/user'. Список всех пользователей. ")
    @CitrusTest
    public void getTestUserAll() {

        $(http()
                .client("restClientReqres")
                .send()
                .get("user/all"));

        $(http().client("restClientReqres")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body(new Gson().toJson(getAllUser()))
        );
    }

    @Test(description = "Проверяем endpoint '/user'. Рейтинг пользователя. ")
    @CitrusTest
    public void getTestUserRating() {

        $(http()
                .client("restClientReqres")
                .send()
                .get("user/User-2"));

        $(http().client("restClientReqres")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body(new Gson().toJson(new Rating()
                        .name("User-2")
                        .score(52)))
        );
    }


    private List<User> getAllUser() {
        return LongStream
                .range(0, 3)
                .mapToObj(x -> new User()
                        .name("User-" + (x + 1))
                        .cource("QA").email("user" + (x + 1) + "@mail.ru")
                        .age((int) (30 + (x + 1) * 3)))
                .collect(Collectors.toList());
    }
}
