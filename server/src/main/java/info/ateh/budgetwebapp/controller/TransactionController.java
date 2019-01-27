package info.ateh.budgetwebapp.controller;

import info.ateh.budgetwebapp.entity.Transaction;
import info.ateh.budgetwebapp.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

import static info.ateh.budgetwebapp.utils.Constants.DATE_PATTERN;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

    private final TransactionService service;

    TransactionController(TransactionService service) {
        this.service = service;
    }

    /** GET: возвращает текущий баланс */
    @GetMapping("/transactions/balance")
    private Long balance() {
        return  service.getBalance();
    }

    /** GET: возвращает все транзакции */
    @GetMapping("/transactions")
    private List<Transaction> all() {
        return service.getAllTransactions();
    }

    /** GET: возвращает расходные операции в пределах дат */
    @GetMapping("/transactions/withdrawBetweenDates/{start}/{end}")
    private List<Transaction> withdrawBetweenDates(@PathVariable @DateTimeFormat(pattern = DATE_PATTERN) Date start,
                                                  @PathVariable @DateTimeFormat(pattern = DATE_PATTERN) Date end) {
        return service.getWithdrawBetweenDates(start, end);
    }

    /** GET: Возвращает результат проверки члена семьи на наличие у него транзакций*/
    @GetMapping("/transactions/checkForOwner/{id}")
    private boolean checkForOwner(@PathVariable Long id) {
        return service.checkMemberForTransactions(id);
    }

    /** POST: добавляет новую транзакцию в репозиторий */
    @PostMapping("/transactions")
    private Transaction newTransaction(@RequestBody Transaction newTransaction) {
        return service.addTransaction(newTransaction);
    }
}
