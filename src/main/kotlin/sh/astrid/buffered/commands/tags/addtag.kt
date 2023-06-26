@file:Command(
        "addtag",
        permission = "buffered.admin",
        description = "Add a tag to a player"
)

package sh.astrid.buffered.commands.tags

import me.honkling.commando.annotations.Command
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.data.player.BufferedPlayer
import sh.astrid.buffered.lib.extensions.mm

fun addtag(executor: ConsoleCommandSender, player: Player, tag: String) {
    val tagData = Buffered.tagData.tags[tag]

    if(tagData === null) {
        executor.sendMessage("<error> A tag with the ID of $tag does not exist.".mm())
        return
    }

    BufferedPlayer(player.uniqueId).addTag(tag)

    executor.sendMessage("<success> Successfully added the ${tagData.displayName} tag to ${player.name}".mm())
}