package fr.stromans.service.file.loader;

import fr.stromans.domain.Transaction;

import java.util.List;

public interface IFileLoader {

    public List<Transaction> parse();
}
