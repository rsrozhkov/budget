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
    @Positive (message = "Id must be positive")
    private Long id;

    @NotNull (message = "Name cannot be null")
    @NotBlank (message = "Name cannot be blank")
    @Size(min = MIN_NAME_LEN, max = MAX_NAME_LEN,
            message = "Name must be between " + MIN_NAME_LEN + "  and " + MAX_NAME_LEN + " characters")
    @Pattern(regexp = NAME_REGEXP, message = "Name must contain only letters spaces and hyphen between letters")
    @Column(unique = true)
    private String name;

   public Member(String name) {
        this.name=name;
    }
}
