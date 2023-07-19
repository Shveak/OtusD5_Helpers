package org.otus.pojo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(exclude = "timestamp")
@Accessors(fluent = true)
public class ErrorResponse {

    private String error;
    private String message;
    private String timestamp;
    private int status;
}
