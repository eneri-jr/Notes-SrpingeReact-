package io.github.notas.rest.dto;

import io.github.notas.domain.models.NoteModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompartDTO {
    private NoteModel note;
    private String email;
}