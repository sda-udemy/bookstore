package pl.sda.bookstore.remote;

public class NotEnoughMoneyException extends Exception {

	public NotEnoughMoneyException(String message) {
		super(message);
	}
}
