package org.otus;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.TestNGCitrusSupport;
import com.google.gson.Gson;
import org.otus.pojo.Cource;
import org.otus.pojo.Cources;
import org.otus.pojo.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class TestCources extends TestNGCitrusSupport {

    @Test(description = "Проверяем endpoint '/cource' с условленным параметром 'mock'.")
    @CitrusTest
    public void getTestCourceMock() {

        $(http()
                .client("restClientReqres")
                .send()
                .get("cource/mock"));

        $(http().client("restClientReqres")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body("{\n\"name\": \"Заглушка\",\n\"price\": 555555\n}")
        );
    }

    @Test(description = "Проверяем endpoint '/cource' с отсутсвующим курсом.")
    @CitrusTest
    public void getTestCourceMockError() {

        $(http()
                .client("restClientReqres")
                .send()
                .get("cource/mock15"));

        $(http().client("restClientReqres")
                .receive()
                .response(HttpStatus.BAD_REQUEST)
                .message()
                .type(MessageType.JSON)
                .body(new Gson().toJson(new ErrorResponse()
                        .error("Плохой запрос")
                        .status(400)
                        .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                        .message("указанный курс отсутствует")))
        );
    }

    @Test(description = "Проверяем endpoint '/cource'. Список всех курсов.")
    @CitrusTest
    public void getTestCourceAll() {

        $(http()
                .client("restClientReqres")
                .send()
                .get("cource/all"));

        $(http().client("restClientReqres")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body(new Gson().toJson(getAllCources()))
        );
    }

    @Test(description = "Проверяем endpoint '/cource'. Данные курса QA.")
    @CitrusTest
    public void getTestCourceQA() {

        $(http()
                .client("restClientReqres")
                .send()
                .get("cource/qa"));

        $(http().client("restClientReqres")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body(new Gson().toJson(new Cource()
                        .name("QA")
                        .price(65000)))
        );
    }


    private Cources getAllCources() {
        Cources cources = new Cources()
                .cources(Map.of("QA", 65000, "DevOps", 60000, "Developer", 93000, "SQL", 80000)
                        .entrySet().stream()
                        .map(x -> new Cource().name(x.getKey()).price(x.getValue()))
                        .sorted(Comparator.comparing(Cource::name))
                        .collect(Collectors.toList()));
        System.out.println(cources);
        return cources;
    }
}
