package First.Spring.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET_KEY = "8c804c36256e93d041eaa691cfa46e94fa69637edab292f0878f770715619d89687bf5b982e77da2d73246b76ff36bbdb8b26933d5a5993ca144d8532f851a97";

    public String getSubject(String token) {
        return getClaim(token,Claims::getSubject);
    }

    public String generateJwt(UserDetails userDetails){
        return generateJwt(userDetails,new HashMap<>());
    }

    public String generateJwt(
            UserDetails user,
            Map<String ,Object> extraClaims
    ){
        return Jwts.
                builder()
                .addClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 15 * 60 ))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateJwt(String token,UserDetails userDetails){
        String userName = getSubject(token);
        return userName.equals(userDetails.getUsername()) && tokenIsNonExpired(token);
    }

    private boolean tokenIsNonExpired(String token) {
        return extractExpiration(token).after(new Date(System.currentTimeMillis()));
    }

    private Date extractExpiration(String token) {
        return getClaim(token,Claims::getExpiration);
    }

    public <T> T getClaim (String token, Function<Claims, T> claimsResolver){
        Claims allClaims = getAllClaims(token);
        return claimsResolver.apply(allClaims);
    }

    public Claims getAllClaims (String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key  getSignInKey() {
        byte[] key = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }

}
