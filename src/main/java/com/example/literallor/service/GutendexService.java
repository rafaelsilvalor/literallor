package com.example.literallor.service;

import com.example.literallor.model.GutendexResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GutendexService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "https://gutendex.com/books?search={title}";

    public GutendexResponseDTO searchBook(String title) {
        try {
            return restTemplate.getForObject(API_URL, GutendexResponseDTO.class, title);
        } catch (Exception e) {
            System.err.println("Erro ao chamar a API Gutendex: " + e.getMessage());
            return null;
        }
    }
}
