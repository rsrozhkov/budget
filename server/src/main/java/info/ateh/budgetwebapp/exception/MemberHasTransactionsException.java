package info.ateh.budgetwebapp.exception;

public class MemberHasTransactionsException extends RuntimeException{

    public MemberHasTransactionsException(Long id) {
        super("Член семьи с ID" + id + " не может быть удалён, так как у него есть транзакции");
    }
}
