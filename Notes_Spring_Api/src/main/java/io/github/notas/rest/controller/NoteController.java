package io.github.notas.rest.controller;

import io.github.notas.domain.models.NoteModel;
import io.github.notas.domain.models.UserModel;
import io.github.notas.domain.repository.UserRepository;
import io.github.notas.rest.dto.CompartDTO;
import io.github.notas.security.jwt.JwtService;
import io.github.notas.service.impl.NoteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteServiceImpl noteService;
    private final JwtService jwtService;
    private final UserRepository repositoryUser;


    //Cadastrar uma nota:
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteModel saveNote(@RequestBody NoteModel noteModel, @RequestHeader("x-acess-token") String token) throws IllegalAccessException {
        if(jwtService.tokenValid(token)){
            UserModel userExistent = repositoryUser.findByEmail(jwtService.getLoginUser(token)).
                    orElseThrow(() -> new UsernameNotFoundException("Usuário inexistente"));
            return noteService.save(noteModel, userExistent);
        }
        else{
            throw new IllegalAccessException();
        }
    }

    //Listar todas as notas de um usuário:
    @GetMapping
    public List<NoteModel> find(@RequestHeader("x-acess-token") String token) throws IllegalAccessException {
        if(jwtService.tokenValid(token)){
            UserModel userExistent = repositoryUser.findByEmail(jwtService.getLoginUser(token)).
                    orElseThrow(() -> new UsernameNotFoundException("Usuário inexistente"));
            return noteService.find(userExistent);
        }
        else{
            throw new IllegalAccessException();
        }
    }

    //Deletar uma nota:
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id, @RequestHeader("x-acess-token") String token) throws IllegalAccessException {
        if(jwtService.tokenValid(token)){
            noteService.delete(id);
        }
        else{
            throw new IllegalAccessException();
        }
    }

    //Procurar uma nota por uma especificação:
    @GetMapping("{query}")
    public List<NoteModel> findQuery(@PathVariable String query, @RequestHeader("x-acess-token") String token) throws IllegalAccessException {
        if(jwtService.tokenValid(token)){
            UserModel userExistent = repositoryUser.findByEmail(jwtService.getLoginUser(token)).
                    orElseThrow(() -> new UsernameNotFoundException("Usuário inexistente"));
            return noteService.findQuery(query, userExistent);
        }
        else{
            throw new IllegalAccessException();
        }
    }

    //Alterar uma nota:
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateNote(@PathVariable String id, @RequestBody NoteModel note, @RequestHeader("x-acess-token") String token) throws IllegalAccessException {
        if(jwtService.tokenValid(token)){
            noteService.updateNote(id,note);
        }
        else{
            throw new IllegalAccessException();
        }
    }

    //Compartilhar uma nota:
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void compartilhaNote(@RequestBody CompartDTO dados, @RequestHeader("x-acess-token") String token) throws IllegalAccessException {
        if(jwtService.tokenValid(token)){
            noteService.compartNote(dados);
        }
        else{
            throw new IllegalAccessException();
        }
    }
}