package org.otus.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class Rating {
    private String name;
    private Integer score;
}
