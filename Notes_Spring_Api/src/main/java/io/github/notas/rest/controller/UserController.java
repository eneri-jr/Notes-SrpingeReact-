package io.github.notas.rest.controller;

import io.github.notas.domain.models.UserModel;
import io.github.notas.domain.repository.UserRepository;
import io.github.notas.exception.PasswordInvalidException;
import io.github.notas.rest.dto.CredentialsDTO;
import io.github.notas.rest.dto.TokenDTO;
import io.github.notas.security.jwt.JwtService;
import io.github.notas.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserServiceImpl userService;
    private final JwtService jwtService;
    private final UserRepository repositoryUser;

    @Autowired
    private PasswordEncoder encoder;

    //Cadastrar um usuário:
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserModel saveUser(@RequestBody @Valid UserModel userModel) {
        return userService.save(userModel);
    }

    //Criar Login do usuário:
    @PostMapping("/login")
    public TokenDTO auth(@RequestBody CredentialsDTO credentials){
        try{
            UserModel userModel = UserModel.builder()
                    .email(credentials.getEmail())
                    .password(credentials.getPassword())
                    .build();
            UserDetails userAuth = userService.auth(userModel);
            String token = jwtService.generateToken(userModel);
            UserModel user = userService.loadUser(userModel.getEmail());
            return new TokenDTO(user, token);
        }catch(UsernameNotFoundException | PasswordInvalidException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }

    //Alterar nome e email:
    @PutMapping
    public UserModel update(@RequestBody UserModel user, @RequestHeader("x-acess-token") String token){
        boolean tokenValid = jwtService.tokenValid(token);
        String email = jwtService.getLoginUser(token);
        UserModel userExistent = repositoryUser.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("Usuário inexistente"));
        if(tokenValid){
            userExistent.setName(user.getName());
            userExistent.setEmail(user.getEmail());
            repositoryUser.save(userExistent);
            return userExistent;
        }
        else{
            return userExistent;
        }
    }

    //Alterar senha:
    @PutMapping("/password")
    public UserModel updatePass(@RequestBody UserModel user, @RequestHeader("x-acess-token") String token){
        boolean tokenValid = jwtService.tokenValid(token);
        String email = jwtService.getLoginUser(token);
        UserModel userExistent = repositoryUser.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("Usuário inexistente"));

        if(tokenValid) {
            String hashPassword = encoder.encode(user.getPassword());
            userExistent.setPassword(hashPassword);
            repositoryUser.save(userExistent);
            return userExistent;
        }else{
            return userExistent;
        }
    }

    //Deletar user:

    @DeleteMapping
    public void delete(@RequestHeader("x-acess-token") String token){
        boolean tokenValid = jwtService.tokenValid(token);
        String email = jwtService.getLoginUser(token);
        UserModel userExistent = repositoryUser.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("Usuário inexistente"));

        if(tokenValid) {
            repositoryUser.delete(userExistent);
        }
    }


}
