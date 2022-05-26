package com.hanghae.coffee.security;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {

        String getException = (String) request.getAttribute("EXCEPTION");
        int getStatus =  Integer.parseInt((String) request.getAttribute("STATUS"));

        if (StringUtils.isEmpty(getException)) {
            getException = "";
            getStatus = HttpServletResponse.SC_OK;
        }

        setResponse(response, getStatus, getException);

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
