package io.github.notas.service.impl;

import io.github.notas.domain.models.UserModel;
import io.github.notas.domain.repository.UserRepository;
import io.github.notas.exception.PasswordInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository repository;

    //Método para salvar o usuário:
    @Transactional
    public UserModel save(UserModel userModel){
        String hashPassword = encoder.encode(userModel.getPassword());
        userModel.setPassword(hashPassword);
        return repository.save(userModel);
    }

    public UserDetails auth(UserModel userModel){
        UserDetails userDetails = loadUserByUsername(userModel.getEmail());
        boolean passwordCorrect = encoder.matches(userModel.getPassword(), userDetails.getPassword());

        if(passwordCorrect){
            return userDetails;
        }

        throw new PasswordInvalidException();

    }

    //Vamos carregar o usuário com sua senha:
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel userModel = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        String[] roles = {"USER"};

        return User
                .builder()
                .username(userModel.getEmail())
                .password(userModel.getPassword())
                .roles(roles)
                .build();
    }

    //Vamos carregar o usuário com as informações para o Header:
    public UserModel loadUser(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

}
