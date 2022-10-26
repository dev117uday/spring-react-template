package com.example.jwt.utility;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.example.jwt.exception.ExceptionBroker;
import com.example.jwt.model.Users;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JWTUtility implements Serializable {

    private static final long serialVersionUID = 234234523523L;

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${app.jwt.secret_key}")
    private String secretKey;

    // retrieve username from jwt token
    public String getUserIdFromToken(String token) throws ExceptionBroker {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) throws ExceptionBroker {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws ExceptionBroker {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) throws ExceptionBroker {

        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (SignatureException e) {
            throw new ExceptionBroker("invalid jwt", HttpStatus.BAD_REQUEST);
        } catch (ExpiredJwtException e) {
            throw new ExceptionBroker("expried jwt", HttpStatus.BAD_REQUEST);
        } catch (MalformedJwtException e) {
            throw new ExceptionBroker("malformed jwt", HttpStatus.BAD_REQUEST);
        } catch (UnsupportedJwtException e) {
            throw new ExceptionBroker("unsupported jwt", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            throw new ExceptionBroker("bad jwt", HttpStatus.BAD_REQUEST);
        }

    }

    // check if the token has expired
    private Boolean isTokenExpired(String token) throws ExceptionBroker {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // generate token for user
    public String generateToken(Users userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getSub());
    }

    // while creating the token -
    // 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
    // 2. Sign the JWT using the HS512 algorithm and secret key.
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    // validate token
    public Boolean validateToken(String token) throws ExceptionBroker {

        boolean isJWTSigned = Jwts.parser().setSigningKey(secretKey).isSigned(token);

        if (!isTokenExpired(token) && isJWTSigned) {
            return true;
        }

        return false;
    }
}