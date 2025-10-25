package com.example.budget_tracker.service;

import com.opencsv.CSVReader;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.List;

@Component
public class CsvParserFactory {

    private final List<CsvParserService> parsers;

    public CsvParserFactory(List<CsvParserService> parsers) {
        this.parsers = parsers;
    }

    public CsvParserService getParser(MultipartFile file) throws Exception {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] header = reader.readNext();
            return parsers.stream()
                    .filter(parser -> parser.supports(header))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No suitable parser found for the provided CSV file."));
        }
    }
}
