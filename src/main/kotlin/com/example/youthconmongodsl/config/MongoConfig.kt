package com.example.youthconmongodsl.config

import com.example.youthconmongodsl.dto.MongoProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.mapping.MongoMappingContext

@Configuration
class MongoConfig(
    private val mongoProperty: MongoProperty,
) {
    @Bean
    fun mongoTemplate(): MongoTemplate {
        val factory = SimpleMongoClientDatabaseFactory(mongoProperty.uri)
        val converter = MappingMongoConverter(
            DefaultDbRefResolver(factory),
            MongoMappingContext(),
        )
        return MongoTemplate(factory, converter)
    }
}