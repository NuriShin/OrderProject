package kr.co._29cm.homework.config.database

import jakarta.transaction.Transactional
import kr.co._29cm.homework.domain.Item
import kr.co._29cm.homework.repository.ItemRepository
import org.springframework.stereotype.Service
import org.apache.commons.csv.CSVFormat
import java.nio.file.Files
import java.nio.file.Paths

@Service
class DatabaseInitializeService(private val itemRepository: ItemRepository) {

    @Transactional
    fun itemsInitialize() {
        val path = Paths.get("src/main/resources/items.csv")
        Files.newBufferedReader(path).use { reader ->
            val csvParser = CSVFormat.DEFAULT
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build()
                .parse(reader)

            csvParser.forEach { record ->
                val productNo = record.get("productNo")
                val productName = record.get("productName")
                val sellPrice = record.get("sellPrice").toLong()
                val inventQuantity = record.get("inventQuantity").toInt()

                val item = Item(
                    productNo = productNo,
                    productName = productName,
                    sellPrice = sellPrice,
                    inventQuantity = inventQuantity
                )
                itemRepository.save(item)
            }
        }
    }
}
