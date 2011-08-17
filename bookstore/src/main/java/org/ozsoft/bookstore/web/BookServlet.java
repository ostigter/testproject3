package org.ozsoft.bookstore.web;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ozsoft.bookstore.entities.Book;
import org.ozsoft.bookstore.services.BookService;

@WebServlet("/books")
public class BookServlet extends HttpServlet {

    private static final long serialVersionUID = -5760635279925620022L;
    
    @EJB
    private BookService bookService;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>Books</h1>");
        List<Book> books = bookService.findAll();
        if (books.isEmpty()) {
            sb.append("<p>No books available.</p>");
        } else {
            sb.append("<p><ul>");
            for (Book book : books) {
                sb.append(String.format("<li>%s</li>", book));
            }
            sb.append("</ul></p>");
        }
        response.getWriter().write(sb.toString());
    }

}
