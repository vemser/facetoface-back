package br.com.vemser.facetoface.security;

import br.com.vemser.facetoface.entity.PerfilEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String KEY_CARGOS = "CARGOS";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    @Value("${jwt.expirationSenha}")
    private String expirationSenha;

    public String getToken(UsuarioEntity usuarioEntity, String expiration) {
        if (expiration != null) {
            this.expiration = expiration;
        }
        LocalDateTime localDateTimeAtual = LocalDateTime.now();
        Date dataAtual = Date.from(localDateTimeAtual.atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime dateExpiracaoLocalDate = localDateTimeAtual.plusMonths(Long.parseLong(this.expiration));
        Date expiracao = Date.from(dateExpiracaoLocalDate.atZone(ZoneId.systemDefault()).toInstant());


        List<String> cargosDoUsuario = usuarioEntity.getPerfis().stream()
                .map(PerfilEntity::getAuthority)
                .toList();

        return Jwts.builder().
                setIssuer("vemser-api")
                .claim(Claims.ID, usuarioEntity.getEmail())
                .claim(KEY_CARGOS, cargosDoUsuario)
                .setIssuedAt(dataAtual)
                .setExpiration(expiracao)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getTokenSenha(UsuarioEntity usuarioEntity) {

        LocalDateTime localDateTimeAtual = LocalDateTime.now();
        Date dataAtual = Date.from(localDateTimeAtual.atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime dateExpiracaoLocalDate = localDateTimeAtual.plusMinutes(Long.parseLong(this.expirationSenha));
        Date expiracao = Date.from(dateExpiracaoLocalDate.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder().
                setIssuer("vemser-api")
                .claim(Claims.ID, usuarioEntity.getEmail())
                .setIssuedAt(dataAtual)
                .setExpiration(expiracao)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public UsernamePasswordAuthenticationToken isValid(String token) {
        if (token == null) {
            return null;
        }
        token = token.replace("Bearer ", "");
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        String user = claims.get(Claims.ID, String.class);

//        String cargo = claims.get(KEY_CARGOS, String.class);


        SimpleGrantedAuthority cargoSimple = new SimpleGrantedAuthority(KEY_CARGOS);
        return new UsernamePasswordAuthenticationToken(user, null, Collections.singleton(cargoSimple));
    }

    public String getEmailByToken(String token) throws InvalidTokenException {
        token = token.replace("Bearer ", "");
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get(Claims.ID, String.class);
        } catch (ExpiredJwtException exception) {
            throw new InvalidTokenException("Token Expirado");
        }
    }
}
