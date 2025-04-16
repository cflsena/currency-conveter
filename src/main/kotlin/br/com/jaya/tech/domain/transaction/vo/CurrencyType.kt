package br.com.jaya.tech.domain.transaction.vo

import br.com.jaya.tech.domain.common.exception.DomainException
import java.util.*

enum class CurrencyType {

    BRL {
        override fun locale(): Locale = Locale("pt", "BR")
    },
    USD {
        override fun locale(): Locale = Locale.US
    },
    EUR {
        override fun locale(): Locale = Locale.GERMANY // fixed europe country
    },
    JPY {
        override fun locale(): Locale = Locale.JAPAN
    };

    abstract fun locale(): Locale

    companion object {
        fun findByName(name: String) : CurrencyType {
            return entries.firstOrNull { it.name.equals(name, ignoreCase = true) }
                ?: throw DomainException.with("currency $name invalid, available values $entries")
        }
    }

}
