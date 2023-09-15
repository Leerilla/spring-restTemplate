package com.example.imgtest;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/")
public class ApiController {


    @PostMapping("/file")
    public ResponseEntity<?> test(MultipartFile file) throws IOException {

        String host = "http://localhost:9000/upload";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ByteArrayResource contentsAsResource = new ByteArrayResource(file.getBytes()){
            @Override
            public String getFilename(){
                return file.getOriginalFilename();
            }
        };

        body.add("img", contentsAsResource);

        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("multipart", "form-data", Charset.forName("UTF-8"));
        headers.setContentType(mediaType);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity res = restTemplate.postForEntity(host, request,String.class);
        System.out.println(res);
        return ResponseEntity.ok("성공");
    }


    @PostMapping("/files")
    public ResponseEntity<?> arrays(MultipartFile[] files) throws IOException {

        String host = "http://localhost:9000/uploads";
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        Arrays.stream(files).forEach(file -> {
            try{
                ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
                };
                body.add("imgs", resource);
            }catch (Exception e){
                e.printStackTrace();
            }
        });

//        body.add("imgs",contentsAsResource);

        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("multipart", "form-data", Charset.forName("UTF-8"));
        headers.setContentType(mediaType);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity res = restTemplate.exchange(host, HttpMethod.POST, request,String.class);
        return res;
    }

}
