package pl.sda.bookstore.book;

import pl.sda.bookstore.author.Author;
import pl.sda.bookstore.common.Mapper;
import pl.sda.bookstore.facade.BookDto;

import java.util.function.Function;
import java.util.stream.Collectors;

public class BookMapper implements Mapper<Book, BookDto> {

	@Override
	public BookDto map(Book from) {
		String authors = from.getAuthors()
				.stream()
				.map(AuthorToNameAndSurname.INSTANCE)
				.collect(Collectors.joining(", "));
		return new BookDto(from.getIsbn(), from.getTitle(), authors, String.valueOf(from.getPrice()));
	}

	private enum AuthorToNameAndSurname implements Function<Author, String> {
		INSTANCE;

		@Override
		public String apply(Author author) {
			return author.getName() + " " + author.getSurname();
		}
	}
}
