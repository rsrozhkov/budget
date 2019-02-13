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
    @Positive (message = "Id должен быть больше нуля")
    private Long id;

    @NotNull (message = "Дата должна присутствовать")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = DATE_PATTERN)
    @PastOrPresent(message = "Значение даты не должно быть больше текущей даты")
    private Date date;

    @ManyToOne
    @NotNull (message = "Член семьи не должен быть null")
    private Member member;

    @NotNull (message = "Объем не может быть null")
    private Long amount;

    @NotBlank (message = "Комментарий не может быть пустым")
    @Size(min = MIN_COMMENT_LEN, max = MAX_COMMENT_LEN,
            message = "Длина комментария должна быть от " + MIN_COMMENT_LEN + " до " + MAX_COMMENT_LEN + " символов")
    @Pattern(regexp = COMMENT_PATTERN)
    private String comment;

    public Transaction(Member member, Long amount, String comment) {
        this.date = new Date();
        this.member = member;
        this.amount = amount;
        this.comment = comment;
    }
}
