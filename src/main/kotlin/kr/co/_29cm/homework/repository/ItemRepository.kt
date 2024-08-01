package kr.co._29cm.homework.repository

import kr.co._29cm.homework.domain.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ItemRepository : JpaRepository<Item, String> {

    fun findByProductNo(productNo: String): Optional<Item>
}
