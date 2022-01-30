package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BookDaoImpl implements BookDao {

    private final JdbcTemplate jdbcTemplate;

    public BookDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Book getById(Long id) {
        return jdbcTemplate.queryForObject("select * from book where id = ?", getRowMapper(), id);
    }

    @Override
    public Book getByTitle(String title) {
        return jdbcTemplate.queryForObject("select * from book where title = ?", getRowMapper(), title);
    }

    @Override
    public Book saveNewBook(Book book) {
        jdbcTemplate.update("insert into book(title, isbn, publisher, author_id) values(?, ?, ? ,?)",
                book.getTitle(), book.getIsbn(), book.getPublisher(), book.getAuthorId());
        Long createdId = jdbcTemplate.queryForObject("select last_insert_id()", Long.class);
        return this.getById(createdId);
    }

    @Override
    public Book updateBook(Book book) {
        jdbcTemplate.update("update book set title = ?, isbn = ?, publisher = ?, author_id = ? where id = ?",
                book.getTitle(), book.getIsbn(), book.getPublisher(), book.getAuthorId(), book.getId());
        return this.getById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        jdbcTemplate.update("delete from book where id = ?", id);
    }

    private RowMapper<Book> getRowMapper() {
        return new BookMapper();
    }
}
