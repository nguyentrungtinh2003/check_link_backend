package com.TrungTinhBackend.check_link_backend.service.google;

import com.TrungTinhBackend.check_link_backend.entity.User;
import com.TrungTinhBackend.check_link_backend.service.jwt.JwtUtils;
import com.TrungTinhBackend.check_link_backend.service.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        User user = new User();
        user.setUsername(oAuth2User.getName());
        user.setEmail(oAuth2User.getEmail());
        user.setImg(oAuth2User.getPicture());

        userService.processOAuthPostLogin(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        Authentication authentication1 = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication1);

        String jwtToken = jwtUtils.generateToken(userDetails);

        response.sendRedirect("https://url-checker-dev.vercel.app/oauth2/redirect?token=" + jwtToken);
    }
}