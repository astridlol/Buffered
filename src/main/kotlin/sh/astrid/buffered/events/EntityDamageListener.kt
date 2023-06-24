package sh.astrid.buffered.events

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.data.player.BufferedPlayer
import sh.astrid.buffered.lib.extensions.*

fun Player.parseKillMessage(killer: Player): Component {
    val mm = MiniMessage.miniMessage()
    val messages = Buffered.messageData

    val item = killer.inventory.itemInMainHand
    val defaultResolvers = listOf<TagResolver>(
            Placeholder.parsed("victim", this.name),
            Placeholder.parsed("killer", killer.name),
    ).toTypedArray()

    if(item.type === Material.AIR) {
        return mm.deserialize(
                messages.killMessages.random(),
                *defaultResolvers,
                Placeholder.parsed("item", "their fists"),
                *getMMResolvers()
        ).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    val meta = item.itemMeta!!

    val displayName = meta.displayName() ?: Component.text(item.materialName())

    val enchants = buildString {
        meta.enchants.forEach { (enchantment, level) ->
            val name = enchantment.key.toString().prettyNamespace()
            val romanLevel = level.toRoman()
            append("$name $romanLevel\n")
        }
    }

    val itemComponent = "<hover:show_text:'<s>Enchants:</s>\n$enchants'>".mm().append(displayName)

    return mm.deserialize(
            messages.killMessages.random(),
            *defaultResolvers,
            Placeholder.component("item", itemComponent),
            *getMMResolvers()
    ).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
}

class EntityDamageListener : Listener {
    init {
        Buffered.instance.server.pluginManager.registerEvents(this, Buffered.instance)
    }

    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        val victim = event.entity
        val killer = event.damager

        if(victim !is Player) return

        // If they're not dead yet, return
        if(victim.health - event.finalDamage > 0) return

        event.isCancelled = true
        victim.health = 20.0
        victim.tpToSpawn()

        // If they weren't killed by a player, return
        if(killer !is Player) return

        val killerData = BufferedPlayer(killer.uniqueId)
        val victimData = BufferedPlayer(victim.uniqueId)

        if(victim.uniqueId == killer.uniqueId) return

        Bukkit.broadcast(victim.parseKillMessage(killer))

        killer.inventory.addItem(ItemStack(Material.MUSHROOM_STEW))

        killerData.addKill()
        victimData.addDeath()

        killer.updateScoreboard()
        victim.updateScoreboard()

        // Reset the players current kit
        // In the future, check if they have re-kit enabled. The permission node buffered.rekit would be set for the toggle command.
        victimData.setKit(null)
    }
}