package kr.co._29cm.homework.domain

import kr.co._29cm.homework.enums.ErrorMessages
import kr.co._29cm.homework.enums.Price
import kr.co._29cm.homework.exception.NumberFormatException
import kr.co._29cm.homework.exception.SoldOutException

/**
 * 주문데이터와 상품데이터 도메인 데이터 클래스
 * - 주문데이터의 유효성 검사의 주체는 해당 도메인입니다
 * - 해당 클래스에서는 재고수량과 입력데이터 유효성 검사 메서드를 제공합니다
 */
data class Order (
    val productNo: String,
    val productName: String,
    val orderQuantity: String,
    val inventQuantity: Int,
    val sellPrice: Long
){

    fun validateInventoryQuantity() {
        if (this.orderQuantity.toInt() > this.inventQuantity) {
            throw SoldOutException(ErrorMessages.SOLD_OUT_EXCEPTION_MSG.value)
        }
    }

    fun validateOrderQuantity(inputQuantity: String){
        if(inputQuantity.toIntOrNull() == null){
            throw NumberFormatException(ErrorMessages.NUMBER_FORMAT_EXCEPTION_MSG.value)
        }
    }
}
/**
 * 결제정보 도메인 데이터 클래스
 * - 주문데이터의 총합계 및 배송비등을 계산하는 메서드를 제공합니다
 */
data class TotalOrder (
    val orders: MutableList<Order>,
    var orderAmount: Long = 0,
    var totalAmount: Long = 0,
    var deliveryAmount: Long = 0
){
    fun calculateOrderAmount() {
        this.orderAmount = orders.sumOf {
            it.orderQuantity.toInt() * it.sellPrice
        }
    }

    fun calculateTotalAmount() {
        this.deliveryAmount = if (this.orderAmount < Price.FREE_DELIVERY_THRESHOLD.value) {
            Price.DELIVERY_PRICE.value
        } else 0

        this.totalAmount = this.orderAmount + this.deliveryAmount
    }
}