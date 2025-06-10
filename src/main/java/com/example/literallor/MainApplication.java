package com.example.literallor;

import com.example.literallor.entity.Author;
import com.example.literallor.repository.AuthorRepository;
import com.example.literallor.repository.BookRepository;
import com.example.literallor.service.GutendexService;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.util.List;
import java.util.Scanner;

@Component
public class MainApplication implements CommandLineRunner {

    private final GutendexService gutendexService;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    // Atualize o construtor para receber o AuthorRepository
    public MainApplication(GutendexService gutendexService, BookRepository bookRepository, AuthorRepository authorRepository) {
        this.gutendexService = gutendexService;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int option = -1;

        while (option != 0) {
            displayMenu();
            try {
                option = Integer.parseInt(scanner.nextLine());
                handleOption(option, scanner);
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Por favor, digite um número.");
            }
        }
        System.out.println("Saindo da aplicação. Até mais!");
    }

    private void displayMenu() {
        System.out.println("\n--- MENU LITERALLOR ---");
        System.out.println("Escolha uma das opções abaixo:");
        System.out.println("1. Buscar livro por título");
        System.out.println("2. Listar todos os livros cadastrados");
        System.out.println("3. Listar todos os autores cadastrados");
        System.out.println("4. Listar autores vivos em um determinado ano");
        System.out.println("5. Listar livros por idioma");
        System.out.println("0. Sair");
        System.out.print("Sua opção: ");
    }

    private void handleOption(int option, Scanner scanner) {
        switch (option) {
            case 1:
                // Lógica para buscar livro por título (a implementar)
                System.out.println("Funcionalidade ainda não implementada.");
                break;
            case 2:
                // Lógica para listar todos os livros (a implementar)
                System.out.println("Funcionalidade ainda não implementada.");
                break;
            case 3:
                // Lógica para listar todos os autores (a implementar)
                System.out.println("Funcionalidade ainda não implementada.");
                break;
            case 4:
                listAuthorsAliveInYear(scanner);
                break;
            case 5:
                // Lógica para listar livros por idioma (a implementar)
                System.out.println("Funcionalidade ainda não implementada.");
                break;
            case 0:
                break; // O loop principal cuidará da saída
            default:
                System.out.println("Opção inválida.");
        }
    }

    private void listAuthorsAliveInYear(Scanner scanner) {
        System.out.print("Digite o ano para a busca: ");
        try {
            int year = Integer.parseInt(scanner.nextLine());
            List<Author> authors = authorRepository.findAuthorsAliveInYear(year);

            if (authors.isEmpty()) {
                System.out.println("Nenhum autor encontrado vivo no ano " + year + ".");
            } else {
                System.out.println("--- Autores Vivos em " + year + " ---");
                authors.forEach(author ->
                        System.out.printf("Nome: %s (Nascimento: %d, Falecimento: %s)\n",
                                author.getName(),
                                author.getBirthYear(),
                                author.getDeathYear() != null ? author.getDeathYear().toString() : "N/A")
                );
            }
        } catch (NumberFormatException e) {
            System.out.println("Ano inválido. Por favor, digite um número.");
        }
    }
}
