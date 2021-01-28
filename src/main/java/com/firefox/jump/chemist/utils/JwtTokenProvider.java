package com.firefox.jump.chemist.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sun.security.auth.UserPrincipal;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtTokenProvider {
    private static final String SECRET = "67ef9c880f8133966d10d612d32562301f343c3b";

    private static final String ISS = "chemist";

    private static final long EXPIRATION = 3600L;

    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    public static String generateToken(Integer userId) {
        Date now = new Date();
        String token = JWT.create()
                .withIssuer(ISS)
                .withIssuedAt(now)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION * 1000 * 24))
                .withClaim("uid", userId)
                .sign(ALGORITHM);
        return token;
    }

    public static Integer verifyJWT(String token) {
        JWTVerifier verifier = JWT.require(ALGORITHM)
                .withIssuer(ISS)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        Claim userIdClaim = jwt.getClaim("uid");

        return userIdClaim.asInt();
    }

    public UserPrincipal parseToken(String token) {
        JWTVerifier verifier = JWT.require(ALGORITHM)
                .withIssuer(ISS)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        Claim userIdClaim = jwt.getClaim("uid");

        return new UserPrincipal(userIdClaim.asString());
    }

}
