@file:Command(
    "kit",
    permission = ""
)

package sh.astrid.buffered.commands.kits

import me.honkling.commando.annotations.Command
import org.bukkit.entity.Player
import sh.astrid.buffered.Buffered.Companion.kitData
import sh.astrid.buffered.data.player.BufferedPlayer
import sh.astrid.buffered.lib.*
import sh.astrid.buffered.lib.extensions.*

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

    val grantedKit = executor.giveKit(kit)

    if(!grantedKit) {
        executor.sendMessage("<error> Couldn't give you that kit.".mm())
        return
    }

    BufferedPlayer(executor.uniqueId).setKit(kit)

    executor.sendMessage("<p>Successfully claimed the <s>${storedKit.name}</s> kit.".mm())
}