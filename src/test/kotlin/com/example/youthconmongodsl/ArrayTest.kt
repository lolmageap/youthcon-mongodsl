package com.example.youthconmongodsl

import com.example.youthconmongodsl.clazz.embeddedDocument
import com.example.youthconmongodsl.collection.Book
import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import com.example.youthconmongodsl.extension.find
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootTest
class ArrayTest(
    @Autowired private val mongoTemplate: MongoTemplate,
) {

    @BeforeEach
    fun setUp() {
        mongoTemplate.dropCollection(Author::class.java)
        mongoTemplate.insertAll(
            listOf(
                Author.of(
                    name = "John",
                    age = 18,
                    books = mutableListOf(
                        createBook("book1"),
                        createBook("book2"),
                    ),
                ),
                Author.of(
                    name = "Jane",
                    age = 20,
                    books = mutableListOf(
                        createBook("book3"),
                        createBook("book4"),
                    ),
                ),
            )
        )
    }

    @Test
    fun `배열 필드에 대한 equal 연산 테스트`() {
        val books = mutableListOf(
            createBook("book1"), createBook("book2")
        )

        val document = document {
            and(
                { field(Author::books) eq books },
            )
        }

        val author = mongoTemplate.find(document, Author::class).first()
        val titles = author.books.map { it.title }
        assert(titles == mutableListOf("book1", "book2"))
    }

    @Test
    fun `배열 필드에 대한 not equal 연산 테스트`() {
        val books = mutableListOf(
            createBook("book1"), createBook("book2")
        )

        val document = document {
            and(
                { field(Author::books) ne books },
            )
        }

        val author = mongoTemplate.find(document, Author::class).first()
        val titles = author.books.map { it.title }
        assert(
            !titles.containsAll(
                mutableListOf("book1", "book2")
            )
        )
    }

    @Test
    fun `배열 필드에 대한 equal 연산 테스트3`() {
        val document = document {
            embeddedDocument(Author::books).elemMatch({
                or(
                    { field(Book::title) eq "book1" },
                )
            })
        }

        val author = mongoTemplate.find(document, Author::class).first()
        val titles = author.books.map { it.title }

        assert(titles == mutableListOf("book1", "book2"))
    }
}

private fun createBook(
    title: String,
    price: Long = 10000L,
    isbn: String = "isbn",
    description: String? = null,
) = Book.of(
    title,
    price,
    isbn,
    description,
)