package sh.astrid.buffered.data

data class Kit(
        val name: String,
        val guiItem: String = "minecraft:wooden_sword",
        val description: String = "No description provided",
        val items: List<KitItem>,
        val armor: List<KitItem>
) {
    data class KitItem(
        val material: String,
        val name: String?,
        val amount: Int = 1,
        val lore: List<String> = listOf(""),
        val enchantments: List<String>?,
        val nbt: String?
    )
}

data class Kits(
    val kits: Map<String, Kit>
)