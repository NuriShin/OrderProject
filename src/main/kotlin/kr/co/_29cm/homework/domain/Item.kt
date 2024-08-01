package kr.co._29cm.homework.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "items")
data class Item(
    @Id
    val productNo: String = "",
    val productName: String = "",
    val sellPrice: Long = 0L,
    var inventQuantity: Int = 0
)