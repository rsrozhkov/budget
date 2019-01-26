package info.ateh.budgetwebapp.exception;

public class MemberHasTransactionsException extends RuntimeException{

    public MemberHasTransactionsException(Long id) {
        super("Member with ID" + id + " can not be deleted because he has transactions");
    }
}