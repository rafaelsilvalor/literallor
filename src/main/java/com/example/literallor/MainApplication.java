package com.example.literallor;

import com.example.literallor.entity.Author;
import com.example.literallor.entity.Book;
import com.example.literallor.repository.AuthorRepository;
import com.example.literallor.repository.BookRepository;
import com.example.literallor.service.GutendexService;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class MainApplication implements CommandLineRunner {

    private final GutendexService gutendexService;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

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
                searchAndSaveBook(scanner);
                break;
            case 2:
                listAllBooks();
                break;
            case 3:
                listAllAuthors();
                break;
            case 4:
                listAuthorsAliveInYear(scanner);
                break;
            case 5:
                listBooksByLanguage(scanner);
                break;
            case 0:
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }


    private void searchAndSaveBook(Scanner scanner) {
        System.out.print("Digite o título do livro para buscar: ");
        String title = scanner.nextLine();

        var bookDTO = gutendexService.searchBook(title).results().stream()
                .findFirst()
                .orElse(null);

        if (bookDTO == null) {
            System.out.println("Livro não encontrado na API Gutendex.");
            return;
        }

        // Verifica se o livro já existe no banco
        Optional<Book> existingBook = bookRepository.findByTitleIgnoreCase(bookDTO.title());
        if (existingBook.isPresent()) {
            System.out.println("Este livro já está cadastrado no banco de dados.");
            return;
        }

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

        bookRepository.save(book);
        System.out.println("Livro '" + book.getTitle() + "' salvo com sucesso!");
    }

    private void listAllBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            System.out.println("Nenhum livro cadastrado no banco de dados.");
        } else {
            System.out.println("--- Todos os Livros Cadastrados ---");
            books.forEach(book ->
                    System.out.printf("Título: %s, Autor: %s, Idioma: %s\n",
                            book.getTitle(),
                            book.getAuthor().getName(),
                            book.getLanguage())
            );
        }
    }

    private void listAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            System.out.println("Nenhum autor cadastrado no banco de dados.");
        } else {
            System.out.println("--- Todos os Autores Cadastrados ---");
            authors.forEach(author -> {
                System.out.printf("Nome: %s (Nascimento: %d, Falecimento: %s)\n",
                        author.getName(),
                        author.getBirthYear(),
                        author.getDeathYear() != null ? author.getDeathYear().toString() : "N/A");
                List<String> bookTitles = author.getBooks().stream()
                        .map(Book::getTitle)
                        .toList();
                System.out.println("  Livros: " + bookTitles);
            });
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

    private void listBooksByLanguage(Scanner scanner) {
        System.out.println("Digite o idioma para a busca (ex: 'en' para inglês, 'pt' para português):");
        String language = scanner.nextLine();
        List<Book> books = bookRepository.findByLanguage(language);

        if (books.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o idioma '" + language + "'.");
        } else {
            System.out.printf("--- Livros em '%s' ---\n", language);
            books.forEach(book ->
                    System.out.printf("Título: %s, Autor: %s\n",
                            book.getTitle(),
                            book.getAuthor().getName())
            );
        }
    }
}
