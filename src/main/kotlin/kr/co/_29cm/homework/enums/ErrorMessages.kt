package kr.co._29cm.homework.enums

enum class ErrorMessages(val value: String) {
    SOLD_OUT_EXCEPTION_MSG("SoldOutException 발생. 주문한 상품량이 재고량보다 큽니다."),
    EMPTY_RESULT_EXCEPTION_MSG("EmptyResultDataAccessException 발생. 입력한 상품번호에 해당하는 데이터가 없습니다."),
    NUMBER_FORMAT_EXCEPTION_MSG("NumberFormatException 발생. 수량은 숫자로 입력해주세요.")
}