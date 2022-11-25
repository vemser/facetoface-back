//package br.com.vemser.facetoface.security;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@RequiredArgsConstructor
//public class TokenAuthenticationFilter extends OncePerRequestFilter {
//    private final TokenService tokenService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        String headerAut = request.getHeader("Authorization");
//
//        UsernamePasswordAuthenticationToken isValid = tokenService.isValid(headerAut);
//
//        SecurityContextHolder.getContext().setAuthentication(isValid);
//
//        filterChain.doFilter(request, response);
//    }
//
//}
