package br.com.jaya.tech.domain.common.exception

import br.com.jaya.tech.shared.exception.BaseException
import br.com.jaya.tech.shared.exception.Error

class DomainException private constructor(
    error: Error,
) : BaseException(error) {
    companion object {
        fun with(message: String): DomainException = DomainException(Error.with(message))
    }
}

class NotFoundException private constructor(
    error: Error,
) : BaseException(error) {
    companion object {
        fun with(message: String): NotFoundException = NotFoundException(Error.with(message))
    }
}

class ResourceAlreadyCreatedException private constructor(
    error: Error,
) : BaseException(error) {
    companion object {
        fun with(message: String): ResourceAlreadyCreatedException = ResourceAlreadyCreatedException(Error.with(message))
    }
}

class BusinessException private constructor(
    error: Error,
) : BaseException(error) {
    companion object {
        fun with(message: String): BusinessException = BusinessException(Error.with(message))
    }
}
