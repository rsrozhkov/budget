package info.ateh.budgetwebapp.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

import static info.ateh.budgetwebapp.utils.Constants.*;

@Data
@Entity
@NoArgsConstructor
@ToString
public class Transaction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive (message = "Id must be positive")
    private Long id;

    @NotNull (message = "Date cannot be null")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = DATE_PATTERN)
    @PastOrPresent(message = "date value cannot be in a future")
    private Date date;

    @ManyToOne
    @NotNull (message = "Member cannot be null")
    private Member member;

    @NotNull (message = "Amount cannot be null")
    private Long amount;

    @NotBlank (message = "Comment cannot be blank")
    @Size(min = MIN_COMMENT_LEN, max = MAX_COMMENT_LEN,
            message = "Comment must be between " + MIN_COMMENT_LEN + " and " + MAX_COMMENT_LEN + " characters")
    @Pattern(regexp = COMMENT_PATTERN)
    private String comment;

    public Transaction(Member member, Long amount, String comment) {
        this.date = new Date();
        this.member = member;
        this.amount = amount;
        this.comment = comment;
    }

    /** Этот конструктор в данномм варианте приложения нужен только для тестирования,
     * так как даты генерируются автоматически в момент создания транзакции*/
    public Transaction(Date date, Member member, Long amount, String comment) {
        this(member,amount,comment);
        this.date = date;
    }
}
