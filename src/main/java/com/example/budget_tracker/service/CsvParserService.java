package com.example.budget_tracker.service;

import com.example.budget_tracker.domain.Transaction;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CsvParserService {
    List<Transaction> parseCsv(MultipartFile file);
    boolean supports(String[] header);
}
