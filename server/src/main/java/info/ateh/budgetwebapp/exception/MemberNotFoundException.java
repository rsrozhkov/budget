package info.ateh.budgetwebapp.exception;

public class MemberNotFoundException extends RuntimeException{

    public MemberNotFoundException(Long id) {
        super("Не возможно найти члена семьи с Id: " + id);
    }

    public MemberNotFoundException(String name) {
        super("Не возможно найти члена семьи с Именем: " + name);
    }
}
