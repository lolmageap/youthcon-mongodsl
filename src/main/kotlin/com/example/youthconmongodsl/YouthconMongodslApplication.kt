package com.example.youthconmongodsl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("com.example.youthconmongodsl.dto")
class YouthconMongodslApplication

fun main(args: Array<String>) {
    runApplication<YouthconMongodslApplication>(*args)
}