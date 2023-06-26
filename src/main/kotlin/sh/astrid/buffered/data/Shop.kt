package sh.astrid.buffered.data

data class ShopItem(
        val name: String,
        val material: String,
        val price: Int
)

data class Shop(
        val shop: Map<String, ShopItem>
)