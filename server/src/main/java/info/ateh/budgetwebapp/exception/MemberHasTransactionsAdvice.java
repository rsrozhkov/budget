package info.ateh.budgetwebapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MemberHasTransactionsAdvice {

    @ResponseBody
    @ExceptionHandler(MemberHasTransactionsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String memberNotFoundHandler(MemberNotFoundException ex) {
        return ex.getMessage();
    }
}
