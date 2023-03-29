package com.gildedrose.persistence.exposed

import com.gildedrose.persistence.ItemsContract
import org.jetbrains.exposed.sql.SchemaUtils.createMissingTablesAndColumns
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach

class DatabaseItemsTests : ItemsContract<ExposedTXContext>(
    items = DatabaseItems(testDatabase)
)
{

    @BeforeEach
    fun resetDB() {
        transaction(testDatabase) {
            drop(DatabaseItems.ItemsTable)
            createMissingTablesAndColumns(DatabaseItems.ItemsTable)
        }
    }
}