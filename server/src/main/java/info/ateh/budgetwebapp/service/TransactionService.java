package info.ateh.budgetwebapp.service;

import info.ateh.budgetwebapp.entity.Transaction;
import info.ateh.budgetwebapp.exception.NotEnoughMoneyException;
import info.ateh.budgetwebapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Возвращает текущий баланс
     */
    public Long getBalance() {
        List<Transaction> list = getAllTransactions();
        Long balance = 0L;
        for (Transaction t : list) balance += t.getAmount();
        return balance;
    }

    /**
     * Возвращает все транзакции найденые в репозитории
     */
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * Возвращает отрицательные транзакции в пределах заданных дат.
     * Если дата конца позже, чем дата начала, то меняет их местами
     */
    public List<Transaction> getWithdrawBetweenDates(@NotNull @PastOrPresent Date start,
                                                     @NotNull @PastOrPresent Date end) {
        if (start.after(end)) return transactionRepository.findWithdrawBetweenDates(end, start);
        return transactionRepository.findWithdrawBetweenDates(start, end);
    }

    /**
     * Проверяет числятся ли за членом семьи транзакции и возвращает результат
     */
    public boolean checkMemberForTransactions(@Positive @NotNull Long id) {
        return (transactionRepository.findByMemberId(id).size() > 0);
    }

    /**
     * Добавляет новую транзакцию в репозиторий. Также, роверяет достаточно ли средств на балансе,
     * если не достаточно, то кидает исключение
     */
    public Transaction addTransaction(@NotNull Transaction newTransaction) {
        if (newTransaction.getAmount() + getBalance() < 0) throw new NotEnoughMoneyException();
        String comment = newTransaction.getComment();
        newTransaction.setComment(normalizeSpaces(comment));
        return transactionRepository.save(newTransaction);
    }

    /**
     * Убирает лишние пробелы из строки
     */
    private static String normalizeSpaces(String string) {
        return string.trim().replaceAll("\\s+", " ");
    }

}
