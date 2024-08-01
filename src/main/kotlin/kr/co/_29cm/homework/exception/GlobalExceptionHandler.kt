package kr.co._29cm.homework.exception
import kr.co._29cm.homework.enums.ErrorMessages
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(SoldOutException::class)
    fun handleSoldOutException(e: SoldOutException):String {
        return ErrorMessages.SOLD_OUT_EXCEPTION_MSG.value
    }

    @ExceptionHandler(EmptyItemException::class)
    fun handleEmptyItemException(e: EmptyItemException): String {
        return ErrorMessages.EMPTY_RESULT_EXCEPTION_MSG.value
    }

    @ExceptionHandler(NumberFormatException::class)
    fun handleNumberFormatException(e: NumberFormatException): String {
        return ErrorMessages.EMPTY_RESULT_EXCEPTION_MSG.value
    }

}