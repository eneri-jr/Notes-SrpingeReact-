package io.github.notas.rest.dto;

import io.github.notas.domain.models.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private UserModel user;
    private String token;
}
