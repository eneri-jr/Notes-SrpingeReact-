package io.github.notas.security.jwt;

import io.github.notas.domain.models.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.expiracao}")
    private String expiration;

    @Value("${security.jwt.chave-assinatura}")
    private String subscriptionKey;

    //Gerar data de expiração para o Token:
    public Date generateDate(){
        long expString = Long.valueOf(expiration);
        LocalDateTime dateHourExpiration = LocalDateTime.now().plusHours(expString);
        Instant instant = dateHourExpiration.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    //Geração de Token depois de validar usuário:
    public String generateToken(UserModel userModel){
        Date date = generateDate();
        return Jwts
                .builder()
                .setSubject(userModel.getEmail())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, subscriptionKey)
                .compact();
    }

    //Obter dados do usuário que estão no token:
    private Claims getClaims(String token) throws ExpiredJwtException {
        return Jwts
                .parser()
                .setSigningKey(subscriptionKey)
                .parseClaimsJws(token)
                .getBody();
    }

    //Método para validar o Token:
    public boolean tokenValid(String token){
        try{
            Claims claims = getClaims(token);
            Date dateExpiration = claims.getExpiration();
            LocalDateTime localDateTime =
                    dateExpiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return !LocalDateTime.now().isAfter(localDateTime);
        }catch(Exception e){
            return false;
        }
    }

    //Método para saber que usuário esta logado:
    public String getLoginUser(String token) throws ExpiredJwtException{
        return (String) getClaims(token).getSubject();
    }

//    public static void main(String[] args) {
//        ConfigurableApplicationContext contexto = SpringApplication.run(NotesApplication.class);
//        JwtService service = contexto.getBean(JwtService.class);
//        UserModel userModel = UserModel.builder().email("jr@gmail.com").build();
//        String token = service.generateToken(userModel);
//        System.out.println(token);
//
//        boolean isTokenValido = service.tokenValid(token);
//        System.out.println(isTokenValido);
//
//        System.out.println(service.getLoginUser(token));
//
//    }
}
