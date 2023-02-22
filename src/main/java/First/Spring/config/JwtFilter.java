package First.Spring.config;

import First.Spring.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().startsWith("/api/v1/auth")){
            filterChain.doFilter(request,response);
            return;
        }
        String authHeader = request.getHeader("Authorization");
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        String jwt = authHeader.substring(7);
        try {
            String email = jwtService.getSubject(jwt);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (jwtService.validateJwt(jwt,userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(request);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException e) {
            log.error("Expired Token Exception {}",e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setHeader("error",e.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);
            Map<String,Object> error = new HashMap<>();
            error.put("error_message",e.getMessage().substring(0,11));
            new ObjectMapper().writeValue(response.getOutputStream(),error);
        } catch (SignatureException e) {
            log.error("JWT Signature Exception {}",e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setHeader("error",e.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);
            Map<String,Object> error = new HashMap<>();
            error.put("error_message",e.getMessage());
            new ObjectMapper().writeValue(response.getOutputStream(),error);
        }  catch(Exception e){
            log.error("Some other exception in JWT parsing: {}",e.getMessage());
        }
        filterChain.doFilter(request,response);
    }
}
