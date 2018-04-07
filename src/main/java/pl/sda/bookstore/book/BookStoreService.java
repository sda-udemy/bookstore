package pl.sda.bookstore.book;

import pl.sda.bookstore.common.Mapper;
import pl.sda.bookstore.facade.*;
import pl.sda.bookstore.remote.NotEnoughMoneyException;
import pl.sda.bookstore.remote.PaymentService;

import java.util.Optional;

public class BookStoreService implements BookStore {

	private BookRepository bookRepository;

	private PaymentService paymentService;

	private Mapper<Book, BookDto> mapper = new BookMapper();

	@Override
	public ResultDto buy(ISBNDto isbnDto, ClientDto clientDto) {

		Optional<Book> bookOptional = bookRepository.find(isbnDto.getIsbn());
		if (!bookOptional.isPresent()) {
			return ResultDto.ofBookNotFound(
					String.format("Book with isbn=%s not found", isbnDto.getIsbn())
			);
		}

		Book book = bookOptional.get();

		try {
			paymentService.pay(clientDto.getCardId(), book.getPrice());
		} catch (NotEnoughMoneyException e) {
			return ResultDto.ofPaymentError(e.getMessage());
		}

		BookDto bookDto = mapper.map(book);

		return ResultDto.ofSuccess(bookDto);
	}
}
