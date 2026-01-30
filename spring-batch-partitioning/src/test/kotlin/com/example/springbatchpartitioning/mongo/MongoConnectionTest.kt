package com.example.springbatchpartitioning.mongo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import kotlin.test.Test

@SpringBootTest
class MongoConnectionTest {

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Test
    fun testConnection() {
        val isConnected = mongoTemplate.db.runCommand(org.bson.Document("ping", 1))
        println("MongoDB Connection Status: ${isConnected["ok"]}")
    }
}