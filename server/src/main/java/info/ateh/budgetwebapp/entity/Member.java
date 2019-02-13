package info.ateh.budgetwebapp.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import static info.ateh.budgetwebapp.utils.Constants.*;

@Data
@Entity
@NoArgsConstructor
@ToString
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive (message = "Число должно быть больше нуля")
    private Long id;

    @NotNull (message = "Имя должно присутствовать")
    @NotBlank (message = "Имя не может быть пустым")
    @Size(min = MIN_NAME_LEN, max = MAX_NAME_LEN,
            message = "Длина имени должна быть от " + MIN_NAME_LEN + "  до " + MAX_NAME_LEN + " символов")
    @Pattern(regexp = NAME_REGEXP, message = "Имя должно содержать только буквы, пробелы и дефисы между словами")
    @Column(unique = true)
    private String name;

   public Member(String name) {
        this.name=name;
    }
}
