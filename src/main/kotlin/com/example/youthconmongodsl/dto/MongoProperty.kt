package com.example.youthconmongodsl.dto

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.data.mongodb")
data class MongoProperty(
    val uri: String,
)