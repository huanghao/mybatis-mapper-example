package org.example;

import io.mybatis.provider.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity.Table("person")
public class Person {
    public Person() {}
    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
    @Entity.Column(id = true)
    private Long id;

    @Entity.Column()
    private String name;

    @Entity.Column()
    private Integer age;
}
