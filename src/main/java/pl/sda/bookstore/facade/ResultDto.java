package pl.sda.bookstore.facade;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResultDto implements Serializable {
	public enum Status {
		SUCCESS,
		PAYMENT_ERROR,
		BOOK_NOT_FOUND;
	}

	private Status status;
	private String message;
	private List<BookDto> bookDtos;

	public static ResultDto ofSuccess(BookDto bookDto) {
		return new ResultDto(Status.SUCCESS, "", Arrays.asList(bookDto));
	}

	public static ResultDto ofPaymentError(String message) {
		return new ResultDto(Status.PAYMENT_ERROR, message, Collections.emptyList());
	}

	public static ResultDto ofBookNotFound(String message) {
		return new ResultDto(Status.BOOK_NOT_FOUND, message, Collections.emptyList());
	}

	private ResultDto(Status status, String message, List<BookDto> bookDtos) {
		this.status = status;
		this.message = message;
		this.bookDtos = bookDtos;
	}

	public Status getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public List<BookDto> getBookDtos() {
		return bookDtos;
	}
}

