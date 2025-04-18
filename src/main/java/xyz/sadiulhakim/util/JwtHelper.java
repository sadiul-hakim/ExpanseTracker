package xyz.sadiulhakim.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public class JwtHelper {
    private static final String SECRET = "VxRfBGJFviiO62cg/M0YY5WypcyvtUUjfkI5aDJgwt4dLz6BQKuaKChKyn+Ulhz+";

    public static String generateToken(UserDetails userDetails, Map<String, Object> extraClaims, long expirationDate) {
        JwtBuilder builder = Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationDate))
                .signWith(getSecretKey());

        return builder.compact();
    }

    public static boolean isValidToken(String token, UserDetails details) throws MalformedJwtException {

        boolean isValid = extractUsername(token).equalsIgnoreCase(details.getUsername()) && !isExpired(token);
        if (!isValid) {
            throw new MalformedJwtException("Invalid Token");
        }
        return true;
    }

    private static boolean isExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    private static Date extractExpiration(String token) throws MalformedJwtException {

        return parseSingleClaim(token, Claims::getExpiration);
    }

    public static String extractUsername(String token) throws ExpiredJwtException, UnsupportedJwtException,
            MalformedJwtException, SignatureException, IllegalArgumentException {

        return parseSingleClaim(token, Claims::getSubject);
    }

    public static Object extractClaim(String token, String claim) throws MalformedJwtException {

        return parseSingleClaim(token, claims -> claims.get(claim, Object.class));
    }

    private static <T> T parseSingleClaim(String token, Function<Claims, T> resolver) throws ExpiredJwtException,
            UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {

        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) throws ExpiredJwtException, UnsupportedJwtException,
            MalformedJwtException, SignatureException, IllegalArgumentException {

        JwtParser parser = Jwts.parser()
                .verifyWith(getSecretKey()).build();
        return parser.parseSignedClaims(token).getPayload();
    }

    private static SecretKey getSecretKey() {

        byte[] bytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(bytes);
    }
}
