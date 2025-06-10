package com.example.literallor;

import com.example.literallor.entity.Author;
import com.example.literallor.entity.Book;
import com.example.literallor.model.BookDTO;
import com.example.literallor.repository.BookRepository;
import com.example.literallor.service.GutendexService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MainApplication implements CommandLineRunner {

    private final GutendexService gutendexService;
    private final BookRepository bookRepository;

    public MainApplication(GutendexService gutendexService, BookRepository bookRepository) {
        this.gutendexService = gutendexService;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- INICIANDO TESTE DE PERSISTÊNCIA ---");

        String bookTitle = "Pride and Prejudice";
        var response = gutendexService.searchBook(bookTitle);

        if (response != null && !response.results().isEmpty()) {
            BookDTO bookDTO = response.results().get(0);

            // Converte DTO para Entidade
            Author author = new Author(
                    bookDTO.authors().get(0).name(),
                    bookDTO.authors().get(0).birthYear(),
                    bookDTO.authors().get(0).deathYear()
            );

            Book book = new Book(
                    bookDTO.title(),
                    bookDTO.languages().get(0),
                    bookDTO.downloadCount(),
                    author
            );

            // Salva no banco de dados
            bookRepository.save(book);

            System.out.println("Livro salvo com sucesso no banco de dados!");
            System.out.println(book);

        } else {
            System.out.println("Nenhum livro encontrado com o título: " + bookTitle);
        }

        System.out.println("--- TESTE FINALIZADO ---");
    }
}
