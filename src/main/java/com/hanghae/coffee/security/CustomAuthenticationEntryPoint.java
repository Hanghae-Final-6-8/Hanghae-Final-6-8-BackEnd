package com.hanghae.coffee.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {
        String msg;
        int status = 0;
        if (request.getAttribute("EXCEPTION") != null) {
            if ("NOT LOGIN STATUS".equals(request.getAttribute("EXCEPTION"))) {
                status = 440;
            } else if ("NOT VALIDATE TOKEN".equals(request.getAttribute("EXCEPTION"))) {
                status = 441;
            }

            msg = (String) request.getAttribute("EXCEPTION");
        } else {
            status = HttpServletResponse.SC_UNAUTHORIZED;
            msg = "Unauthorized";
        }

        setResponse(response, status, msg);

    }

    //한글 출력을 위해 getWriter() 사용
    private void setResponse(HttpServletResponse response, int status, String msg)
        throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);

        JSONObject responseJson = new JSONObject();
        responseJson.put("msg", msg);
        responseJson.put("status", status);

        response.getWriter().print(responseJson);
    }

}
