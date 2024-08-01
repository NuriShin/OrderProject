package kr.co._29cm.homework.exception

/**
 * 주문한 상품수량이 재고수량보다 많을 경우의 예외 클래스
 */
class SoldOutException(message: String) : RuntimeException(message)
/**
 * 입력한 상품번호가 존재하지 않을 경우의 예외 클래스
 */
class EmptyItemException(message: String) : RuntimeException(message)
/**
 * 입력한 수량이 숫자가 아닐 경우의 예외 클래스
 */
class NumberFormatException(message: String) : RuntimeException(message)
