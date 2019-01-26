package info.ateh.budgetwebapp.exception;

public class NotEnoughMoneyException extends RuntimeException {

    public NotEnoughMoneyException() {
        super("Not enough money in budget");
    }
}
