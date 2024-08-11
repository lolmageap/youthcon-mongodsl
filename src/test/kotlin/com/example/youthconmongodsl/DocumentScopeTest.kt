package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.YoungAuthor
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import org.junit.jupiter.api.Test
import org.springframework.data.mongodb.core.query.BasicQuery

class DocumentScopeTest {
    @Test
    fun `document scope 내에서 field 를 사용 하면 BasicQuery 로 변환 된다`() {
        val document = document {
            and(
                { field(YoungAuthor::name) eq "John" },
                { field(YoungAuthor::age) eq 18 },
            )
        }
        assert(document == BasicQuery("{ \"\$and\" : [{ \"name\" : \"John\"}, { \"age\" : 18}]}"))
    }
}