package io.github.notas.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Document(collection = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserModel {

    @Id
    private String id;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    private String password;

    private String created_at;
    private String updated_at;

    public UserModel(){
        this.name = name;
        this.email = email;
        this.password = password;
        this.created_at = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyy"));
        this.updated_at = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyy"));
    }
}