package pl.sda.bookstore.book;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.sda.bookstore.facade.BookStore;
import pl.sda.bookstore.facade.ClientDto;
import pl.sda.bookstore.facade.ISBNDto;
import pl.sda.bookstore.facade.ResultDto;
import pl.sda.bookstore.remote.NotEnoughMoneyException;
import pl.sda.bookstore.remote.PaymentService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookStoreServiceTest {

	@Mock
	private BookRepository bookRepository;
	@Mock
	private PaymentService paymentService;
	@Mock
	private ISBNDto isbnDto;
	@Mock
	private ClientDto clientDto;
	@Mock
	private Book book;

	private String isbn = "1234567890";
	private long cardId = 555666777L;
	private double price = 24.99;

	@InjectMocks
	private BookStore bookStore = new BookStoreService();

	@Before
	public void setUp() throws Exception {
		when(isbnDto.getIsbn()).thenReturn(isbn);
	}

	@Test
	public void shouldReturnBookNotFoundIfBookNotExists() throws Exception {
		// given
		String errorMesssage = "Book with isbn=" + isbn + " not found";
		when(bookRepository.find(isbn)).thenReturn(Optional.empty());

		// when
		ResultDto actual = bookStore.buy(isbnDto, clientDto);

		// then
		assertResult(actual, ResultDto.Status.BOOK_NOT_FOUND, errorMesssage, 0);
		verify(paymentService, never()).pay(eq(clientDto.getCardId()), anyDouble());
	}

	@Test
	public void shouldReturnPaymentError() throws Exception {
		// given
		String errorMessage = "Payment error";
		when(clientDto.getCardId()).thenReturn(cardId);
		when(book.getPrice()).thenReturn(price);
		when(bookRepository.find(isbn)).thenReturn(Optional.of(book));
		doThrow(new NotEnoughMoneyException(errorMessage)).when(paymentService).pay(cardId, price);

		// when
		ResultDto actual = bookStore.buy(isbnDto, clientDto);

		// then
		assertResult(actual, ResultDto.Status.PAYMENT_ERROR, errorMessage, 0);
	}

	@Test
	public void shouldReturnSuccess() throws Exception {
		// given
		when(clientDto.getCardId()).thenReturn(cardId);
		when(book.getPrice()).thenReturn(price);
		when(bookRepository.find(isbn)).thenReturn(Optional.of(book));
		doNothing().when(paymentService).pay(cardId, price);

		// when
		ResultDto actual = bookStore.buy(isbnDto, clientDto);

		// then
		assertResult(actual, ResultDto.Status.SUCCESS, "", 1);
		verify(paymentService, times(1)).pay(cardId, price);
	}

	// TODO: add test(s) for BookMapper

	private void assertResult(ResultDto actual, ResultDto.Status expectedStatus, String expectedMessage, int expectedSize) {
		assertThat(actual.getStatus()).isEqualTo(expectedStatus);
		assertThat(actual.getMessage()).isEqualTo(expectedMessage);
		assertThat(actual.getBookDtos().size()).isEqualTo(expectedSize);
	}
}







