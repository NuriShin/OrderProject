package kr.co.order.application.service

import kr.co.order.domain.Item
import kr.co.order.domain.TotalOrder
import kr.co.order.interfaces.dto.OrderDto

/**
 * 주문데이터 처리 서비스 인터페이스
 */
interface OrderService {
    /* 모든 상품 데이터 select(리스트) */
    fun getItemList(): List<kr.co.order.domain.Item>
    /* 상품번호에 해당하는 상품데이터 select(단건) */
    fun getItemByProductNo(productNo: String): kr.co.order.domain.Item
    /* 주문데이터 처리 */
    fun processOrder(orderDtos: kr.co.order.interfaces.dto.OrderDto): kr.co.order.domain.TotalOrder

}