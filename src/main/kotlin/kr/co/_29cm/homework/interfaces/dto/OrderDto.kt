package kr.co._29cm.homework.interfaces.dto

data class OrderDto (
    val orderList: MutableList<OrderDetailDto>
)

data class OrderDetailDto(
    val productNo: String,
    val quantity: String
)