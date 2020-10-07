package io.github.notas.domain.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Document(collection = "nota")
@Getter
@Setter
public class NoteModel {

    @Id
    private String id;
    private String title;
    private String body;
    private String created_at;
    private String updated_at;
    private String author;
    private HashMap<String, Integer> listCoAuthor = new HashMap();
    private String coAuthor;
    private String compart;

    public NoteModel(){
        this.title = title;
        this.body = body;
        this.created_at = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyy"));
        this.updated_at = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyy"));
        this.author = author;
        this.coAuthor = null;
        this.compart = "Não";
    }
}
