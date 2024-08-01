package kr.co._29cm.homework.application.service

import kr.co._29cm.homework.domain.Item
import kr.co._29cm.homework.domain.TotalOrder
import kr.co._29cm.homework.interfaces.dto.OrderDto

/**
 * 주문데이터 처리 서비스 인터페이스
 */
interface OrderService {
    /* 모든 상품 데이터 select(리스트) */
    fun getItemList(): List<Item>
    /* 상품번호에 해당하는 상품데이터 select(단건) */
    fun getItemByProductNo(productNo: String): Item
    /* 주문데이터 처리 */
    fun processOrder(orderDtos: OrderDto): TotalOrder

}