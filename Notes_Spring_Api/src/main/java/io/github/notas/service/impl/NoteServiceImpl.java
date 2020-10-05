package io.github.notas.service.impl;

import io.github.notas.domain.models.NoteModel;
import io.github.notas.domain.models.UserModel;
import io.github.notas.domain.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoteServiceImpl {

    @Autowired
    private NoteRepository repositoryNote;

    //Método para salvar a nota:
    @Transactional
    public NoteModel save(NoteModel noteModel, UserModel user){
        noteModel.setAuthor(user.getId());
        return repositoryNote.save(noteModel);
    }

    //Método para listar todas as notas de um usuário:
    @Transactional
    public List<NoteModel> find(UserModel user){
        String id = user.getId();
        List<NoteModel> list = repositoryNote.findByAuthor(id);
        list.addAll(repositoryNote.findByCoAuthor(id));
        return list;
    }

    //Método para deletar uma nota:
    @Transactional
    public void delete(String id){
        repositoryNote.findById(id)
                .map(note -> {
                    repositoryNote.delete(note);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota não encontrado"));
    }

    //Método para localizar uma nota por alguma especificação:
    public List<NoteModel> findQuery(String query, UserModel user) {
        String id = user.getId();
        List<NoteModel> notes = repositoryNote.findByBodyRegex(query);
        List<NoteModel> result = new ArrayList<NoteModel>();
        for (NoteModel n : notes) {
            if (n.getAuthor().equals(id)) {
                result.add(n);
            }
        }
        return result;
    }

    //Método para altera uma nota:
    public void updateNote(String id, NoteModel note){
        repositoryNote.findById(id)
                .map(notaExistente -> {
                    note.setId(notaExistente.getId());
                    note.setUpdated_at(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyy")));
                    note.setCreated_at(notaExistente.getCreated_at());
                    note.setAuthor(notaExistente.getAuthor());
                    note.setCoAuthor(notaExistente.getCoAuthor());
                    note.setCompart(notaExistente.getCompart());
                    repositoryNote.save(note);
                    return note;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota não encontrada"));
    }
}
