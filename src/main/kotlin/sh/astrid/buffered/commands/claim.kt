@file:Command(
        "claim",
        permission = "",
        description = "Claim linking rewards"
)

package sh.astrid.buffered.commands

import me.honkling.commando.annotations.Command
import org.bukkit.entity.Player
import sh.astrid.buffered.data.player.BufferedPlayer
import sh.astrid.buffered.lib.LinkyAPI
import sh.astrid.buffered.lib.extensions.mm

@Suppress("unused")
fun claim(executor: Player) {
    val response = LinkyAPI.getUser(executor.uniqueId)

    if(response === null) {
        executor.sendMessage("<error> You're not linked yet! Join our <u><click:open_url:'https://discord.gg/SzUKkgvyHS'>Discord</u> and run <b>/link</b>".mm())
        return
    }

    val player = BufferedPlayer(executor.uniqueId)
    val playerData = player.getPlayer()

    val hasLinkTag = playerData.ownedTags.contains("linked")

    if(hasLinkTag) {
        executor.sendMessage("<error> You've already claimed your rewards for linking.".mm())
        return
    }

    player.addBalance(50)
    player.addTag("linked")

    executor.sendMessage("<s>You've earned the linked tag and 50 coins! Enable your tag using <b>/tags</b>.".mm())
}