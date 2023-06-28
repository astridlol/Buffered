package sh.astrid.buffered.lib

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.data.Kit
import sh.astrid.buffered.lib.extensions.parseEnchant
import sh.astrid.buffered.lib.extensions.setDescription
import sh.astrid.buffered.lib.extensions.setName

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

  // Allows custom NBT to be passed in easily, to be used for custom abilities.
  if(itemData.nbt !== null) {
    item = NBTEditor.set(item, "true", itemData.nbt, "item")
  }

  return item
}

fun Player.giveKit(kit: String): Boolean {
  val storedKit = Buffered.kitData.kits[kit]

  if(storedKit === null) return false

  this.inventory.clear()

  storedKit.items.forEach {
    val item = createItemStack(it)
    item.amount = it.amount
    this.inventory.addItem(item)
  }

  storedKit.armor.forEach {
    val item = createItemStack(it)
    setArmor(item, this)
  }

  return true
}