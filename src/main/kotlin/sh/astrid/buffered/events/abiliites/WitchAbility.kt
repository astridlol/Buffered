package sh.astrid.buffered.events.abiliites

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.lib.extensions.hasNBT
import sh.astrid.buffered.lib.extensions.mm
import sh.astrid.buffered.lib.extensions.prettyNamespace
import sh.astrid.buffered.lib.randomChance

class WitchAbility: Listener {
    init {
        Buffered.instance.server.pluginManager.registerEvents(this, Buffered.instance)
    }

    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        val victim = event.entity
        val attacker = event.damager

        if(victim !is Player || attacker !is Player) return

        val heldItem = attacker.inventory.itemInMainHand
        if(!heldItem.hasNBT("witchSword")) return

        // 50% chance to give them an effect.
        if(!randomChance(0.50)) return

        val type = PotionEffectType.values().random()
        val effect = PotionEffect(type, 200, 1)
        victim.addPotionEffect(effect)

        val effectName = type.key.toString().prettyNamespace()

        attacker.sendActionBar("<p><s>${victim.name}</s> was given the <s>$effectName</s> effect.".mm())
    }

    @EventHandler
    fun rightClick(event: PlayerInteractEvent) {
        val rightClick = event.action === Action.RIGHT_CLICK_AIR || event.action === Action.RIGHT_CLICK_BLOCK
        if(!rightClick) return

        val p = event.player

        val heldItem = p.inventory.itemInMainHand
        if(!heldItem.hasNBT("witchApple")) return

        val type = PotionEffectType.values().random()
        val effect = PotionEffect(type, 200, 1)
        p.addPotionEffect(effect)

        val effectName = type.key.toString().prettyNamespace()

        p.sendMessage("<p>You were given the <s>$effectName</s> effect from that Witch apple c:".mm())

        if (p.inventory.containsAtLeast(heldItem, 1)) {
            for ((index, item) in p.inventory.contents.withIndex()) {
                if(item === null) continue
                if (item.isSimilar(heldItem)) {
                    p.inventory.setItem(index, item.subtract())
                    break
                }
            }
        }
    }
}