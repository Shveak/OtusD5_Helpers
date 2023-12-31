package org.otus.pojo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(fluent = true)
public class User {

    private String name;
    private String cource;
    private String email;
    private Integer age;

}
