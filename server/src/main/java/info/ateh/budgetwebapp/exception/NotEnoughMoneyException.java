package info.ateh.budgetwebapp.exception;

public class NotEnoughMoneyException extends RuntimeException {

    public NotEnoughMoneyException() {
        super("Недостаточно средств в бюджете");
    }
}
