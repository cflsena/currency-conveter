package br.com.jaya.tech.infrastructure.transaction.persistence.jpa

import br.com.jaya.tech.infrastructure.transaction.persistence.TransactionEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TransactionJpaRepository : JpaRepository<TransactionEntity, UUID> {
    fun findAllByUserIdOrderByCreatedAtAsc(
        userId: UUID,
        pageable: Pageable,
    ): Page<TransactionEntity>
}
