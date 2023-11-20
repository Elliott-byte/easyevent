package com.example.easyevent.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Base64;
import java.util.Date;

public class TokenUtil {
    static final String  ISSUSER = "Elliott";
    static final String USEREMAIL = "USEREMAIL";
    static final long MILLI_SECONDS_IN_HOUR = 1 * 60 * 60 * 1000;
    static Algorithm algorithm = Algorithm.HMAC256("Elliott demo");
    public static String signToken(String userEmail, int expirationInHour){
        String token = JWT.create()
                .withIssuer(ISSUSER)
                .withClaim(USEREMAIL, userEmail)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationInHour * MILLI_SECONDS_IN_HOUR))
                .sign(algorithm);
        return token;
    }

    public static String verifyToken(String token){
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUSER)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        String userEmail = jwt.getClaim(USEREMAIL).asString();
        return userEmail;
    }
}
