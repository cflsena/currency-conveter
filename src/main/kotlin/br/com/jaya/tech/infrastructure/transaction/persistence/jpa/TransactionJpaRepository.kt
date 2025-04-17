package br.com.jaya.tech.infrastructure.transaction.persistence.jpa

import br.com.jaya.tech.infrastructure.transaction.persistence.TransactionEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionJpaRepository : JpaRepository<TransactionEntity, String> {
    fun findAllByUserId(userId: String, pageable: Pageable): Page<TransactionEntity>
}