package jpabook.jpashop.exception;

public class NotEnoughStockExcepion extends RuntimeException{

    public NotEnoughStockExcepion() {
        super();
    }

    public NotEnoughStockExcepion(String message) {
        super(message);
    }

    public NotEnoughStockExcepion(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockExcepion(Throwable cause) {
        super(cause);
    }
}
