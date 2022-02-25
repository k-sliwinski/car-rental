package pl.ksliwinski.carrental.exception;

public class CarIsNotRentedByThisUserException extends RuntimeException {

    public CarIsNotRentedByThisUserException(String message) {
        super(message);
    }
}
