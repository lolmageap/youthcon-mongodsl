package com.example.youthconmongodsl.config

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JasyptConfig {
    @Bean
    fun jasyptStringEncryptor() =
        StandardPBEStringEncryptor().apply {
            setAlgorithm("PBEWITHMD5ANDDES")
            setPassword("YOUTHCON")
        }
}