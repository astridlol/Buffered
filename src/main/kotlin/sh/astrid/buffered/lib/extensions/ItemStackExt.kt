package sh.astrid.buffered.lib.extensions

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.lib.NBTEditor

fun ItemStack.setName(name: String): ItemStack {
    val meta = this.itemMeta
    name.let { meta.displayName(it.mm()) }
    this.itemMeta = meta
    return this
}

fun ItemStack.materialName(): String {
    return this.type.name.prettify()
}

fun ItemStack.hasNBT(nbt: String): Boolean {
    return NBTEditor.contains(this, nbt, "item")
}

fun ItemStack.setDescription(lore: String): ItemStack {
    val meta = this.itemMeta
    lore.let {
        meta.lore(listOf(lore.mm()))
    }
    this.itemMeta = meta
    return this
}

fun ItemStack.setDescription(lores: List<String>): ItemMeta {
    val meta = this.itemMeta

    Buffered.instance.logger.info("Received method call for ${lores.size} lores.")

    meta.lore(lores.map {
        it.mm()
    })

    this.itemMeta = meta
    return meta
}