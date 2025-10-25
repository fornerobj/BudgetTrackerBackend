package com.example.budget_tracker.service.csvParserImpls;

import com.example.budget_tracker.domain.Transaction;
import com.example.budget_tracker.service.CsvParserService;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiscoverCardCsvParser implements CsvParserService {

    @Override
    public boolean supports(String[] header) {
        String[] expectedHeader = {"Trans. Date", "Post Date", "Description", "Amount", "Category"};
        if (header.length != expectedHeader.length) return false;
        for (int i = 0; i < header.length; i++) {
            if (!header[i].trim().equalsIgnoreCase(expectedHeader[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Transaction> parseCsv(MultipartFile file) {
        List<Transaction> transactions = new ArrayList<>();

        try(CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            int i = 0;
            while ((line = reader.readNext()) != null) {
                if (i++ == 0) continue; // skip header
                LocalDate date = LocalDate.parse(line[0], formatter);
                String description = line[2];
                String category = null;
                String type = null;
                Double amount = Double.parseDouble(line[3]) * -1;

                Transaction txn = new Transaction(date, description, category, type, amount);
                transactions.add(txn);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return transactions;
    }
}
