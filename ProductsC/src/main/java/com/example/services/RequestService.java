package com.example.services;

import com.example.model.ProductAPOD;
import com.example.model.ProductDetailsDTO;
import com.example.model.UserDetailsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class RequestService {
    String baseURL="http://localhost:8081/";
    private HttpURLConnection openConn(String Url) throws IOException {

        URL url = new URL(Url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");

        return connection;
    }

    public ProductDetailsDTO retrieveProductFromApi(String sku) throws IOException {
        String baseUrl = baseURL+"products/"+sku;
        ProductDetailsDTO product=null;
        try {
            InputStream responseStream = openConn(baseUrl).getInputStream();

            ObjectMapper mapper = new ObjectMapper();

            ProductAPOD apod = mapper.readValue(responseStream, ProductAPOD.class);
            product= new ProductDetailsDTO(apod.sku, apod.name, apod.description);
        } catch (IOException e) {
            System.out.println(e);
        }
        return product;
    }
    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }

    public UserDetailsDTO makeRequestToAutentication(String jwt){
        String urlRequest = "http://localhost:8087/auth/search/" + jwt;
        UserDetailsDTO user = null;
        try {
            InputStream responseStream = openConn(urlRequest).getInputStream();

            ObjectMapper mapper = new ObjectMapper();

            user = mapper.readValue(responseStream, UserDetailsDTO.class);
        } catch (IOException e) {
            System.out.println(e);
        }

        return user;
    }
}
