package bci.core;

import bci.core.exception.InvalidArgumentsException;
import bci.core.exception.UnrecognizedEntryException;
import bci.core.work.Book;
import bci.core.work.Dvd;
import bci.core.work.WorkCategory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImportFileParser {
    private final Library _library;

    ImportFileParser(Library lib) {
        _library = lib;
    }

    void parseFile(String filename) throws UnrecognizedEntryException, IOException {
        String line;

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            while ((line = in.readLine()) != null)
                parseLine(line);
        }
    }

    private void parseLine(String line) throws UnrecognizedEntryException {
        String[] components = line.split(":");

        switch (components[0]) {
            case "USER":
                parseUser(components, line);
                break;

            case "DVD":
                parseDvd(components, line);
                break;

            case "BOOK":
                parseBook(components, line);
                break;

            default:
                throw new UnrecognizedEntryException("Tipo inválido " + components[0] + " na linha " + line);
        }
    }

    private void parseUser(String[] components, String line) throws UnrecognizedEntryException {
        try {
            if (components.length != 3)
                throw new UnrecognizedEntryException ("Número inválido de campos (3) na descrição de um utente: " + line);

            _library.registerUser(components[1], components[2]);
        } catch (InvalidArgumentsException e) {
            throw new UnrecognizedEntryException(e.getArgSpecification());
        }
    }

    private void parseBook(String[] components, String line) throws UnrecognizedEntryException {
        if (components.length != 7)
            throw new UnrecognizedEntryException ("Número inválido de campos (7) na descrição de um Book: " + line);

        try {
            List<Creator> authors = new ArrayList<>();
            for (String name : components[2].split(",")) {
                Creator author = _library.registerCreator(name.trim());
                authors.add(author);
            }

            Book.Builder builder = new Book.Builder()
                    .title(components[1])
                    .price(Integer.parseInt(components[3]))
                    .category(WorkCategory.valueOf(components[4]))
                    .totalCopies(Integer.parseInt(components[6]))
                    .isbn(components[5])
                    .authors(authors);
            Book book = _library.registerWork(builder);

            for (Creator author : authors)
                author.addWork(book);
        } catch (InvalidArgumentsException e) {
            throw new UnrecognizedEntryException(e.getArgSpecification());
        }
    }

    private void parseDvd(String[] components, String line) throws UnrecognizedEntryException {
        if (components.length != 7)
            throw new UnrecognizedEntryException ("Número inválido de campos (7) na descrição de um DVD: " + line);

        try {
            Creator director = _library.registerCreator(components[2].trim());

            Dvd.Builder builder = new Dvd.Builder()
                    .title(components[1])
                    .price(Integer.parseInt(components[3]))
                    .category(WorkCategory.valueOf(components[4]))
                    .totalCopies(Integer.parseInt(components[6]))
                    .igac(components[5])
                    .director(director);
            Dvd dvd = _library.registerWork(builder);

            director.addWork(dvd);
        } catch (InvalidArgumentsException e) {
            throw new UnrecognizedEntryException(e.getArgSpecification());
        }
    }
}
