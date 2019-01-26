package info.ateh.budgetwebapp.exception;

public class MemberNotFoundException extends RuntimeException{

    public MemberNotFoundException(Long id) {
        super("Could not find member with Id: " + id);
    }

    public MemberNotFoundException(String name) {
        super("Could not find member with Name: " + name);
    }
}
