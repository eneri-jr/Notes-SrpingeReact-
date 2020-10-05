package io.github.notas.domain.repository;

import io.github.notas.domain.models.NoteModel;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends MongoRepository<NoteModel, String> {

    List<NoteModel> findByAuthor(String id);
    List<NoteModel> findByCoAuthor(String id);
    List<NoteModel> findByBodyRegex(String query);
}
