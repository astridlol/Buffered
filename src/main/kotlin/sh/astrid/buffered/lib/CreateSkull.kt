package sh.astrid.buffered.lib

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

// Credits to Aroze
fun createSkull(base64: String): ItemStack {
    val head = ItemStack(Material.PLAYER_HEAD)
    val meta = head.itemMeta as SkullMeta
    val profile = GameProfile(UUID.randomUUID(), null)
    profile.properties.put("textures", Property("textures", base64))
    val field = meta::class.java.getDeclaredField("profile")
    field.isAccessible = true
    field.set(meta, profile)
    head.itemMeta = meta
    return head
}