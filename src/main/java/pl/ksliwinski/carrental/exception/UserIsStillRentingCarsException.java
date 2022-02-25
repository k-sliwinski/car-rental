package pl.ksliwinski.carrental.exception;

public class UserIsStillRentingCarsException extends RuntimeException {

    public UserIsStillRentingCarsException(String message) {
        super(message);
    }
}
