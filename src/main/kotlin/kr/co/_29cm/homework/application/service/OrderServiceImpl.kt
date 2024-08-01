package kr.co._29cm.homework.application.service

import jakarta.transaction.Transactional
import kr.co._29cm.homework.domain.Item
import kr.co._29cm.homework.domain.Order
import kr.co._29cm.homework.domain.TotalOrder
import kr.co._29cm.homework.enums.ErrorMessages
import kr.co._29cm.homework.exception.EmptyItemException
import kr.co._29cm.homework.interfaces.dto.OrderDetailDto
import kr.co._29cm.homework.interfaces.dto.OrderDto
import kr.co._29cm.homework.repository.ItemRepository
import org.springframework.stereotype.Service

/**
 * 주문데이터 처리 서비스 클래스
 * - 모든 상품데이터를 불러옵니다
 * - 상품번호에 해당하는 상품을 불러옵니다
 * - 주문데이터의 유효성 검사를 진행합니다
 * - 결제정보를 계산합니다
 * - 주문된 상품 수량을 업데이트 합니다
 * - Transaction 처리되어 예외상황 발생 시 트랜잭션이 수행되지 않습니다
 * - 예외상황 발생 시 서비스를 호출한 메서드에 throw exception 합니다
 */
@Transactional
@Service
class OrderServiceImpl(
    private val itemRepository: ItemRepository
) : OrderService {

    /* 모든 상품 데이터 select(리스트) */
    override fun getItemList(): List<Item> {
        return itemRepository.findAll()
    }

    /**
     * 상품번호에 해당하는 상품데이터 select(단건)
     * - 상품번호에 해당하는 상품이 select되지 않을 경우 emptyitemexception 발생
     */
    override fun getItemByProductNo(productNo: String): Item {
        return itemRepository.findByProductNo(productNo).orElseThrow {
            throw EmptyItemException(ErrorMessages.EMPTY_RESULT_EXCEPTION_MSG.value)
        }
    }
    /**
     * 주문데이터의 결제정보 계산
     * - processOrderDetail를 호출하여 유효성 검증 및 수량 업데이트
     * - 유효성 검증이 완료된 주문데이터와 조회된 상품데이터를 기반으로 TotalOrder 도메인객체를 생성합니다
     * - TotalOrder 도메인의 결제정보 계산 메서드를 실행하고 해당 객체를 리턴합니다
     */
    override fun processOrder(orderDto: OrderDto): TotalOrder {

        val orders = orderDto.orderList.mapNotNull { orderDetailDto ->
            processOrderDetail(orderDetailDto)
        }.toMutableList()

        return TotalOrder(orders).apply {
            calculateOrderAmount()
            calculateTotalAmount()
        }
    }
    /**
     * 주문데이터 유효성 검증
     * - 주문데이터와 조회된 상품데이터로 Order 도메인 객체를 생성합니다
     * - Order 도메인의 유효성 검사 메서드를 실행합니다
     * - 예외사항이 발생하지 않을 경우 주문한 상품의 수량을 업데이트 합니다
     */
    private fun processOrderDetail(orderDetailDto: OrderDetailDto): Order {
        try {
            val item = getItemByProductNo(orderDetailDto.productNo)
            val order = Order(
                item.productNo,
                item.productName,
                orderDetailDto.quantity,
                item.inventQuantity,
                item.sellPrice
            ).apply {
                validateOrderQuantity(orderDetailDto.quantity)  // 입력된 수량이 숫자인지 체크
                validateInventoryQuantity() // 입력된 수량이 재고수량보다 큰지 체크
            }

            // 재고 수량 업데이트
            item.inventQuantity -= order.orderQuantity.toInt()
            itemRepository.save(item)

            return order
        }catch (e: Exception) {
            // 입력된 수량이 숫자일 경우 (NumberFormatException)
            // 입력된 주문데이터가 재고수량보다 많을 경우 (SoldOutException)
            throw e
        }
    }

}
