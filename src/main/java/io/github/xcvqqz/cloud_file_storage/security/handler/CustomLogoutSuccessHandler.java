package io.github.xcvqqz.cloud_file_storage.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        if(authentication == null || !authentication.isAuthenticated()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            buildNoContentHeadersResponse(response);
            return;
        }
           response.setStatus(HttpServletResponse.SC_NO_CONTENT);
           buildNoContentHeadersResponse(response);
    }


    private void buildNoContentHeadersResponse(HttpServletResponse response){
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
    }

}