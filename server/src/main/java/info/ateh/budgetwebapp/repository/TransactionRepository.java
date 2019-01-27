package info.ateh.budgetwebapp.repository;

import info.ateh.budgetwebapp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select t from Transaction t where t.member.id = ?1")
    List<Transaction> findByMemberId(Long id);

    @Query("select t from Transaction t where t.amount<0 and t.date between ?1 and ?2")
    List<Transaction> findWithdrawBetweenDates(Date startDate, Date endDate);
}
