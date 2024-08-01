package kr.co._29cm.homework.interfaces.runner

import kr.co._29cm.homework.application.service.OrderService
import kr.co._29cm.homework.interfaces.dto.OrderDetailDto
import kr.co._29cm.homework.interfaces.dto.OrderDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.Scanner
import kotlin.system.exitProcess

/**
 * 프로그램 실행시 자동 실행되는 콘솔 입력 클래스
 * - h2 database config 및 트랜잭션 완료 후 컴포넌트가 올라갑니다
 * - junit 테스트 실행 시에는 해당 컴포넌트가 빈처리 되지 않습니다
 */
@Component
@Profile("applicationRunner")
class ApplicationRunner {

    @Autowired
    lateinit var orderService: OrderService
    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReady(event: ApplicationReadyEvent) {
        run()
    }
    fun run() {

        var scanner = Scanner(System.`in`)

        /**
         * 콘솔 입력창으로 주문을 입력받습니다
         * - 'o' 입력 시 주문데이터 입력
         * - 주문데이터를 모두 입력받은 후 유효성 검사를 진행하고 예외상황 발생 시 에러메시지를 출력합니다
         * - 'q' 입력 전까지 콘솔 입력은 종료되지 않습니다
         */
        while(true) {

            print("입력(o[order]: 주문, q[quit]: 종료) : ")
            val input = scanner.nextLine()
            when(input) {

                "o" -> {
                    /**
                     * 주문
                     * - 상품목록 출력
                     */
                    println("상품번호\t상품명\t\t\t판매가격\t재고수")
                    orderService.getItemList().forEach { item ->
                        println("${item.productNo}\t${item.productName}\t\t\t${item.sellPrice}\t\t${item.inventQuantity}")
                    }

                    val orderDtoList = mutableListOf<OrderDetailDto>()
                    while(true) {
                        /**
                         * 주문
                         * - 주문데이터를 입력합니다
                         */
                        print("상품번호 : ")
                        var productNo = scanner.nextLine().trim()
                        print("수량 : ")
                        val quantity = scanner.nextLine().trim()

                        /**
                         * 주문
                         * - 입력받은 주문데이터가 null일 경우 (space + enter) 주문을 종료합니다
                         */
                        if(productNo.isBlank() && quantity.isBlank()) {
                            break
                        }
                        orderDtoList.add(OrderDetailDto(productNo, quantity))
                    }

                    try {

                        /**
                         * 결제정보 확인
                         * - 입력받은 주문데이터의 유효성 검사 및 업데이트를 진행하고 결제정보를 계산합니다
                         */
                        val totalOrder = orderService.processOrder(OrderDto(orderDtoList))
                        /**
                         * 결제정보 확인
                         * - 입력한 주문 내역 확인
                         */
                        println("주문내역 :")
                        println("-----------------------------------------------")
                        totalOrder.orders.forEach { order ->
                            println("${order.productName}\t-\t${order.orderQuantity}개")
                        }
                        println("-----------------------------------------------")

                        /**
                         * 결제정보 확인
                         * - 입력한 주문 내역 확인
                         */
                        println("주문금액 : ${totalOrder.orderAmount}")
                        if (totalOrder.deliveryAmount > 0)
                            println("배송비 : ${totalOrder.deliveryAmount}")
                        println("-----------------------------------------------")

                        println("지불금액 : ${totalOrder.totalAmount}")

                    } catch (e: Exception) {
                        /**
                         * 예외상황 처리
                         * - 입력받은 주문데이터의 유효성 검사 및 트랜잭션 과정에서 예외상황 발생 시 오류 메시지가 출력됩니다
                         */
                        println(e.message)
                    }

                }

                "q" -> {
                    println("고객님의 주문 감사합니다.")
                    exitProcess(0)
                }
                else -> {
                    println("유효한 명령을 입력해주세요.")
                }
            }
        }

    }
}