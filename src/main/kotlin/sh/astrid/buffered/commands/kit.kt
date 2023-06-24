@file:Command(
    "kit",
    permission = ""
)

package sh.astrid.buffered.commands

import me.honkling.commando.annotations.Command
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import sh.astrid.buffered.Buffered.Companion.kitData
import sh.astrid.buffered.data.Kit
import sh.astrid.buffered.data.player.BufferedPlayer
import sh.astrid.buffered.lib.*
import sh.astrid.buffered.lib.extensions.*

private fun addEnchant(meta: ItemMeta, enchant: String) {
    val (enchantmentName, enchantmentLevel) = enchant.lowercase().split(":")
    val level = enchantmentLevel.toInt()

    val parsedEnchant = enchantmentName.parseEnchant()
        ?: throw Exception("Enchantment $enchantmentName was invalid")

    meta.addEnchant(parsedEnchant, level, true)
}

private fun setArmor(item: ItemStack, player: Player) {
    val material = item.type.name.uppercase()

    when {
        material.contains("BOOTS") -> player.inventory.boots = item
        material.contains("PANTS") || material.contains("LEGGINGS") -> player.inventory.leggings = item
        material.contains("CHESTPLATE") || material.contains("TUNIC") -> player.inventory.chestplate = item
        material.contains("HELMET") || material.contains("CAP") -> player.inventory.helmet = item
    }
}

private fun createItemStack(itemData: Kit.KitItem): ItemStack {
    val mat = Material.matchMaterial(itemData.material) ?: throw Exception("Material ${itemData.material} is invalid.")

    var item = ItemStack(mat)

    if(itemData.name !== null) {
        println("name is not null (${itemData.name})")
        item = item.setName(itemData.name)
    }

    val meta: ItemMeta = item.setDescription(itemData.lore)

    itemData.enchantments?.forEach { enchantment -> addEnchant(meta, enchantment) }
    item.itemMeta = meta

    // Allows custom NBT to be past in easily, to be used for custom abilities.
    if(itemData.nbt !== null) {
        item = NBTEditor.set(item, "true", itemData.nbt, "item")
    }

    return item
}

@Suppress("unused")
fun kit(executor: Player, kit: String) {
    if(!executor.isInRegion("spawn")) {
        executor.sendMessage("<error>You can only change your kit at spawn!".mm())
        return
    }

    val storedKit = kitData.kits[kit]

    if(storedKit === null) {
        executor.sendMessage("<error>That kit doesn't exist.".mm())
        return
    }

    executor.inventory.clear()

    storedKit.items.forEach {
        val item = createItemStack(it)
        item.amount = it.amount
        executor.inventory.addItem(item)
    }

    storedKit.armor.forEach {
        val item = createItemStack(it)
        setArmor(item, executor)
    }

    BufferedPlayer(executor.uniqueId).setKit(kit)

    executor.sendMessage("<p>Successfully claimed the <s>${storedKit.name}</s> kit.".mm())
}