package com.example.wallet_manager.service

import com.example.wallet_manager.dto.AccountUpdateRequest
import com.example.wallet_manager.model.entities.AccountDocument
import com.example.wallet_manager.model.utils.exceptions.AccountNotFound
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class KafkaConsumer (
        private val objectMapper: ObjectMapper,
        private val transactionService: TransactionService
){
    @KafkaListener(topics = ["transactions"])
    fun listen(record: ConsumerRecord<String, String>) {
        println("[${LocalDateTime.now()}] message received: ${record.value()}")
        val accountUpdateRequest = objectMapper.readValue(record.value(), AccountUpdateRequest::class.java)

        insertAccountUpdateRequestToDatabase(accountUpdateRequest)
    }

    fun insertAccountUpdateRequestToDatabase(updateRequest: AccountUpdateRequest) {
        transactionService.newTransaction(updateRequest)
    }
}