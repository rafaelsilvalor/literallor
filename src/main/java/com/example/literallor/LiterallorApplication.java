package com.example.literallor;

import com.example.literallor.model.BookDTO;
import com.example.literallor.model.GutendexResponseDTO;
import com.example.literallor.service.GutendexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Importe a classe abaixo
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// Adicione o 'exclude' na anotação
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class LiterallorApplication implements CommandLineRunner {

	@Autowired
	private GutendexService gutendexService;

	public static void main(String[] args) {
		SpringApplication.run(LiterallorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("--- INICIANDO TESTE DE CHAMADA À API ---");

		String bookTitle = "Don Quixote";
		GutendexResponseDTO response = gutendexService.searchBook(bookTitle);

		if (response != null && !response.results().isEmpty()) {
			BookDTO firstBook = response.results().get(0);
			System.out.println("Livro encontrado!");
			System.out.println("Título: " + firstBook.title());
			System.out.println("Autor: " + firstBook.authors().get(0).name());
			System.out.println("Downloads: " + firstBook.downloadCount());
		} else {
			System.out.println("Nenhum livro encontrado com o título: " + bookTitle);
		}

		System.out.println("--- TESTE FINALIZADO ---");
	}
}
