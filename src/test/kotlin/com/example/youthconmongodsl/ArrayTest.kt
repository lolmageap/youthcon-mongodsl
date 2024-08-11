package com.example.youthconmongodsl

import com.example.youthconmongodsl.clazz.embeddedDocument
import com.example.youthconmongodsl.collection.Book
import com.example.youthconmongodsl.collection.YoungAuthor
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
        mongoTemplate.dropCollection(YoungAuthor::class.java)
        mongoTemplate.insertAll(
            listOf(
                YoungAuthor.of(
                    name = "John",
                    age = 18,
                    books = mutableListOf(
                        createBook("book1"),
                        createBook("book2"),
                    ),
                ),
                YoungAuthor.of(
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
                { field(YoungAuthor::books) eq books },
            )
        }

        val youngAuthor = mongoTemplate.find(document, YoungAuthor::class).first()
        val titles = youngAuthor.books.map { it.title }
        assert(titles == mutableListOf("book1", "book2"))
    }

    @Test
    fun `배열 필드에 대한 not equal 연산 테스트`() {
        val books = mutableListOf(
            createBook("book1"), createBook("book2")
        )

        val document = document {
            and(
                { field(YoungAuthor::books) ne books },
            )
        }

        val youngAuthor = mongoTemplate.find(document, YoungAuthor::class).first()
        val titles = youngAuthor.books.map { it.title }
        assert(
            !titles.containsAll(
                mutableListOf("book1", "book2")
            )
        )
    }

    @Test
    fun `배열 필드에 대한 equal 연산 테스트3`() {
        val document = document {
            embeddedDocument(YoungAuthor::books).elemMatch({
                or(
                    { field(Book::title) eq "book1" },
                )
            })
        }

        val youngAuthor = mongoTemplate.find(document, YoungAuthor::class).first()
        val titles = youngAuthor.books.map { it.title }

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