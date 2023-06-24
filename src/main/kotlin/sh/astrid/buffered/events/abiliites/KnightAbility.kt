package sh.astrid.buffered.events.abiliites

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.lib.extensions.hasNBT
import sh.astrid.buffered.lib.extensions.mm
import java.util.concurrent.TimeUnit


class KnightAbility: Listener {
    init {
        Buffered.instance.server.pluginManager.registerEvents(this, Buffered.instance)
    }

    @EventHandler
    fun rightClick(event: PlayerInteractEvent) {
        val rightClick = event.action === Action.RIGHT_CLICK_AIR || event.action === Action.RIGHT_CLICK_BLOCK
        if(!rightClick) return

        val p = event.player
        val cd = Buffered.cooldowns

        val heldItem = p.inventory.itemInMainHand
        if(!heldItem.hasNBT("knightBlade")) return

        val cdKey = "knight-${p.uniqueId}"

        if(cd.hasCooldown(cdKey)) {
            val amount = cd.getRemainingTime(cdKey)

            p.sendActionBar("<error>You're currently on cooldown for <b>${amount / 1000}</b> seconds!".mm())
            return
        }

        val speed = PotionEffect(PotionEffectType.SPEED, 200, 1)
        p.addPotionEffect(speed)

        p.sendActionBar("<p>Used your <s>knight</s> effect!".mm())

        cd.setCooldown(cdKey, 60, TimeUnit.SECONDS)
    }
}