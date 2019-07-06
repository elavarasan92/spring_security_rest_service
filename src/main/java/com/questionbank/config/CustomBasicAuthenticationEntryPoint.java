package com.questionbank.config;
import java.io.IOException;
import java.io.PrintWriter;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
 
public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
 
    @Override
    public void commence(final HttpServletRequest request, 
            final HttpServletResponse response, 
            final AuthenticationException authException) throws IOException, ServletException {
         
        response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
         System.out.println("Unauthorised user ");
       // PrintWriter writer = response.getWriter();
        //writer.println("HTTP Status 401 : " + authException.getMessage());
        response.sendError(406, "Invalid credentials");
    }
     
    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("REALM");
        super.afterPropertiesSet();
    }
}