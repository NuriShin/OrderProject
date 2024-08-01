package kr.co._29cm.homework.application.service

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.co._29cm.homework.domain.Order
import kr.co._29cm.homework.domain.TotalOrder
import kr.co._29cm.homework.enums.ErrorMessages
import kr.co._29cm.homework.exception.EmptyItemException
import kr.co._29cm.homework.exception.SoldOutException
import kr.co._29cm.homework.interfaces.dto.OrderDetailDto
import kr.co._29cm.homework.interfaces.dto.OrderDto
import kr.co._29cm.homework.repository.ItemRepository
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.test.assertFailsWith

@SpringBootTest
@ActiveProfiles("notApplicationRunner")
class OrderServiceTest (){

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Autowired
    private lateinit var orderService: OrderService

    val logger = KotlinLogging.logger {}

    @Test
    @DisplayName("multithread요청으로 SoldOutException이 정상 동작하는지 확인하는 단위테스트")
    fun soldOutExceptionTest() {
        /**
         * mock 주문데이터
         * - soldoutexception을 임의로 발생시키기 위해 100-200 사이의 수를 랜덤하게 넣습니다
         */
        val orderDto = OrderDto(mutableListOf(
            OrderDetailDto("768848",getRandomQuantity()),
            OrderDetailDto("748943",getRandomQuantity()),
            OrderDetailDto("779943",getRandomQuantity()),
            OrderDetailDto("759928",getRandomQuantity()),
            OrderDetailDto("377169",getRandomQuantity())
        ))

        orderDto.orderList.forEach { orderDetail ->
            logger.info{ "입력된 데이터 : ${orderDetail.quantity}" }
        }

        /**
         * 스레드풀을 10개 생성하고 20개의 작업을 수행시킵니다
         * SoldOutException이 발생한 경우 assertFailsWith로 성공입니다
         * 모든 스레드풀이 종료할 때까지 1초간 대기합니다
         */
        val executor = Executors.newFixedThreadPool(10)
        val futures = mutableListOf<java.util.concurrent.Future<Unit>>()

        repeat(20) {
            val future = executor.submit<Unit> {
                assertFailsWith<SoldOutException> {
                    orderService.processOrder(orderDto)
                }
            }
            futures.add(future)
        }

        executor.shutdown()
        executor.awaitTermination(1, TimeUnit.SECONDS)
    }
    fun getRandomQuantity(): String{
        return (100..200).random().toString()
    }

    @Test
    @DisplayName("주문 완료 후 재고수량이 업데이트 되는지 확인")
    fun updateInventQuantityTest() {
        val productNo = "779989"
        val orderQuantity = 4
        val item = itemRepository.findByProductNo(productNo).orElseThrow()
        item.inventQuantity -= orderQuantity
        itemRepository.save(item)

        assertEquals(39, itemRepository.findByProductNo(productNo).orElseThrow().inventQuantity)
    }

    @Test
    @DisplayName("주문 후 주문금액 및 전체금액 확인")
    fun calculateOrderAmountTest() {
        val productNo = "760709"
        val orderQuantity = "4"
        val item = itemRepository.findByProductNo(productNo).orElseThrow()
        val totalOrder = TotalOrder(
            mutableListOf(
                Order(
                    item.productNo
                    , item.productName
                    , orderQuantity
                    , item.inventQuantity
                    , item.sellPrice)),0,0)
        totalOrder.calculateOrderAmount()
        totalOrder.calculateTotalAmount()
        assertEquals(3300, totalOrder.totalAmount)
    }

    @Test
    @DisplayName("존재하는 모든 상품 확인")
    fun getAllItemsTest() {
        assertEquals(19, orderService.getItemList().size)
    }

    @Test
    @DisplayName("입력된 상품번호 조회여부 확인")
    fun getItemByProductNoTest() {
        assertEquals(260800, orderService.getItemByProductNo("517643").sellPrice)
    }

    @Test
    @DisplayName("상품번호 오입력시 예외처리 메시지 확인")
    fun ItemNotFoundExceptionTest() {
        val orders = OrderDto(
            mutableListOf(OrderDetailDto("1", "4")))

        val exception = assertThrows(EmptyItemException::class.java) {
            orders.orderList.forEach { orderDetail ->
                orderService.getItemByProductNo(orderDetail.productNo)
            }
        }
        assertEquals(ErrorMessages.EMPTY_RESULT_EXCEPTION_MSG.value, exception.message)
    }

}