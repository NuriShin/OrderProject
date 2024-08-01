package kr.co._29cm.homework.config.database

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DatabaseInitializer(val databaseInitializeService: DatabaseInitializeService) : CommandLineRunner {
    override fun run(vararg args: String?) {
        databaseInitializeService.itemsInitialize()
    }
}