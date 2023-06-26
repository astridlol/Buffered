package sh.astrid.buffered.scoreboard

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.entity.Player
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.data.player.BufferedPlayer
import sh.astrid.buffered.lib.extensions.getMMResolvers
import sh.astrid.buffered.lib.extensions.mm
import sh.astrid.buffered.lib.extensions.parse
import sh.astrid.buffered.scoreboard.fastboard.FastBoard

fun String.parseMM(player: Player): Component {
    // would be better if we had some sort of internal caching system to reduce stress
    val playerData = BufferedPlayer(player.uniqueId).getPlayer()

    val defaultResolvers = listOf<TagResolver>(
            Placeholder.parsed("kills", playerData.kills.toString()),
            Placeholder.parsed("killstreak", playerData.killstreak.toString()),
            Placeholder.parsed("deaths", playerData.deaths.toString()),
            Placeholder.parsed("coins", playerData.balance.toString()),
    ).toTypedArray()

    val mm = MiniMessage.miniMessage()

    return mm.deserialize(this, *getMMResolvers(), *defaultResolvers)
            .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
}

private fun FastBoard.setLines() {
    val srv = Buffered.instance.server

    val online = srv.onlinePlayers.size
    val total = srv.maxPlayers
    val bullet = "<s> •<p>"

    val ping = "%player_ping%".parse(this.player)
    val tps = "%server_tps_1%".parse(this.player)

    this.updateTitle("<p><b><u>Buffered</u></b> <dark_gray>(<p>$online/$total</p>)".mm());
    // Change the lines
    this.updateLines(
            listOf(
                    "",
                    "<s>Player",
                    "$bullet Rank: <t>%vault_rank%".parse(this.player),
                    "$bullet Coins: <t><coins>",
                    "",
                    "<s>Stats",
                    "$bullet Kills: <t><kills>",
                    "$bullet Killstreak: <t><killstreak>",
                    "$bullet Deaths: <t><deaths>",
                    "",
                    "<s>Server",
                    "$bullet TPS: <t>$tps</t>, Ping: <t>$ping</t>",
            ).map {
                it.parseMM(this.player)
            }
    );
}

fun Player.createScoreboard(): FastBoard {
    Buffered.instance.logger.info("Scoreboard create for ${this.uniqueId}")
    val scoreboard = FastBoard(this)
    scoreboard.setLines()
    Buffered.boards[this.uniqueId] = scoreboard
    return scoreboard
}

fun FastBoard.updateScoreboard() {
    this.setLines()
}

fun Player.updateScoreboard() {
    val board = Buffered.boards[this.uniqueId] ?: throw Exception("Could not find scoreboard for player ${this.uniqueId}")
    board.updateScoreboard()
}

fun Player.getFastboard(): FastBoard {
    return Buffered.boards[this.uniqueId] ?: throw Exception("Could not find scoreboard for player ${this.uniqueId}")
}