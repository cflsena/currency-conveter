package br.com.jaya.tech.domain.common.exception

import br.com.jaya.tech.shared.exception.BaseException
import br.com.jaya.tech.shared.exception.Error

class DomainException private constructor(error: Error) : BaseException(error) {
    companion object {
        fun with(message: String): DomainException {
            return DomainException(Error.with(message))
        }
    }
}

class NotFoundException private constructor(error: Error) : BaseException(error) {
    companion object {
        fun with(message: String): NotFoundException {
            return NotFoundException(Error.with(message))
        }
    }
}

class ResourceAlreadyCreatedException private constructor(error: Error) : BaseException(error) {
    companion object {
        fun with(message: String): ResourceAlreadyCreatedException {
            return ResourceAlreadyCreatedException(Error.with(message))
        }
    }
}

class BusinessException private constructor(error: Error) : BaseException(error) {
    companion object {
        fun with(message: String): BusinessException {
            return BusinessException(Error.with(message))
        }
    }
}
