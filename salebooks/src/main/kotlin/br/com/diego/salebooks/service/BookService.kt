package br.com.diego.salebooks.service

import br.com.diego.salebooks.enums.BookStatus
import br.com.diego.salebooks.enums.Errors
import br.com.diego.salebooks.exeptions.NotFoundExeption
import br.com.diego.salebooks.models.BookModel
import br.com.diego.salebooks.models.CustomerModel
import br.com.diego.salebooks.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(
        val bookRepository: BookRepository
) {

    fun create(book: BookModel) {
        bookRepository.save(book)
    }

    fun findAll(pageable: Pageable): Page<BookModel> {
        return bookRepository.findAll(pageable) as Page<BookModel>
    }

    fun findActives(pageable: Pageable): Page<BookModel> {
        return bookRepository.findByStatus(BookStatus.ATIVO, pageable)
    }

    fun findById(id: Int): BookModel {
        return bookRepository.findById(id).orElseThrow { NotFoundExeption(Errors.BK101.message.format(id), Errors.BK101.code) }
    }

    fun delete(id: Int) {
        val book = findById(id)

        book.status = BookStatus.CANCELADO

        update(book)
    }

    fun update(book: BookModel) {
        bookRepository.save(book)
    }

    fun deleteByCustomer(customer: CustomerModel) {
        val books = bookRepository.findByCustomer(customer)
        for (book in books) {
            book.status = BookStatus.DELETADO
        }
        bookRepository.saveAll(books)
    }

    fun findAllByIds(bookIds: Set<Int>): List<BookModel> {
        return bookRepository.findAllById(bookIds).toList()
    }

    fun purchase(books: MutableList<BookModel>) {
        books.map { it.status=BookStatus.VENDIDO }
        bookRepository.saveAll(books)
    }


}

