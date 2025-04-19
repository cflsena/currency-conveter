package br.com.jaya.tech.infrastructure.common.exception

import br.com.jaya.tech.domain.common.exception.BusinessException
import br.com.jaya.tech.domain.common.exception.DomainException
import br.com.jaya.tech.domain.common.exception.NotFoundException
import br.com.jaya.tech.domain.common.exception.ResourceAlreadyCreatedException
import br.com.jaya.tech.shared.exception.BaseException
import br.com.jaya.tech.shared.exception.Error
import feign.FeignException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    companion object {
        private const val BAD_REQUEST_DEFAULT_MESSAGE = "Invalid Request. Please check payload request"
        private const val UNAVAILABLE_SERVICE_MESSAGE = "Unable to process request. Please try again later"
    }

    private val log: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(
        DomainException::class,
        BusinessException::class,
        ResourceAlreadyCreatedException::class,
    )
    fun handle(ex: BaseException): ResponseEntity<Error> = ResponseEntity<Error>(Error.with(ex.message!!), HttpStatus.UNPROCESSABLE_ENTITY)

    @ExceptionHandler(NotFoundException::class)
    fun handle(ex: NotFoundException): ResponseEntity<Error> = ResponseEntity<Error>(Error.with(ex.message!!), HttpStatus.NOT_FOUND)

    @ExceptionHandler(FeignException::class)
    fun handle(ex: FeignException): ResponseEntity<Error> {
        log.error("Failed to call external service. Details: ", ex)
        return ResponseEntity<Error>(Error.with(UNAVAILABLE_SERVICE_MESSAGE), HttpStatus.SERVICE_UNAVAILABLE)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handle(ex: MethodArgumentNotValidException): ResponseEntity<Error> {
        val fieldError: FieldError? = ex.bindingResult.fieldErrors.firstOrNull()
        val message = fieldError?.let { "${it.field} ${it.defaultMessage}" } ?: BAD_REQUEST_DEFAULT_MESSAGE
        return ResponseEntity<Error>(Error.with(message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleBadRequest(ex: HttpMessageNotReadableException): ResponseEntity<Error> =
        ResponseEntity.badRequest().body(Error.with(BAD_REQUEST_DEFAULT_MESSAGE))
}
