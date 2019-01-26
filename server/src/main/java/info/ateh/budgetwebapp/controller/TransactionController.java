package info.ateh.budgetwebapp.controller;

import info.ateh.budgetwebapp.entity.Transaction;
import info.ateh.budgetwebapp.exception.NotEnoughMoneyException;
import info.ateh.budgetwebapp.repository.TransactionRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.List;

import static info.ateh.budgetwebapp.utils.Constants.DATE_PATTERN;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

    private final TransactionRepository transactionRepository;

    TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /** GET: возвращает текущий баланс */
    @GetMapping("/transactions/balance")
    private Long balance() {
        List<Transaction> list = transactionRepository.findAll();
        Long sum = 0L;
        for (Transaction t: list) sum += t.getAmount();
        return sum;
    }

    /** GET: возвращает все транзакции найденые в репозитории */
    @GetMapping("/transactions")
    private List<Transaction> all() {
        return transactionRepository.findAll();
    }

    /** GET: возвращает все транзакции в пределах заданных дат.
     * Если дата конца позже, чем дата начала, то меняет их местами */
    @GetMapping("/transactions/betweenDates/{start}/{end}")
    private List<Transaction> betweenDates(@PathVariable @NotNull @PastOrPresent
                                   @DateTimeFormat(pattern = DATE_PATTERN) Date start,
                                   @PathVariable @NotNull @PastOrPresent
                                   @DateTimeFormat(pattern = DATE_PATTERN) Date end) {
        if (start.after(end)) replaceDates(start,end);
        return transactionRepository.findBetweenDates(start,end);
    }

    /** GET: Проверяет числятся ли за членом семьи транзакции
     *  и возвращает результат*/
    @GetMapping("/transactions/checkForOwner/{id}")
    private boolean checkForOwner(@PathVariable @Positive @NotNull Long id) {
        return (transactionRepository.findByMemberId(id).size() > 0);
    }

    /** POST: добавляет новую транзакцию в репозиторий если она в
     * пределах баланса + округление суммы до второго порядка */
    @PostMapping("/transactions")
    private Transaction newTransaction(@RequestBody @NotNull Transaction newTransaction) {
        if (newTransaction.getAmount()+ balance() < 0) throw new NotEnoughMoneyException();
        else return transactionRepository.save(newTransaction);
    }

    private void replaceDates(Date start, Date end) {
        Long time = start.getTime();
        start = end;
        end = new Date(time);
    }


}
