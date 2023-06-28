@file:Command(
        "report",
        permission = "",
)

package sh.astrid.buffered.commands

import me.honkling.commando.annotations.Command
import me.honkling.pocket.GUI
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.data.ReportData
import sh.astrid.buffered.data.reports.Reports
import sh.astrid.buffered.lib.extensions.mm

private fun Player.sendReport(report: String, suspect: Player) {
  val reportInfo = ReportData(
          suspectUUID = suspect.uniqueId,
          creatorUUID = this.uniqueId,
          reason = report
  )

  Reports().createReport(reportInfo)

  val onlineStaff = Bukkit.getOnlinePlayers().filter {
    it.hasPermission("buffered.reports")
  }

  val msg = buildString {
    append("<s><bold>!! REPORT !!</bold>")
    append("\n")
    append("<p><s>${suspect.name}</s> was just reported for <s>$report</s>")
  }

  onlineStaff.map {
    it.sendMessage(msg.mm())
  }

  this.sendMessage("<success> Successfully submitted your report.".mm())
}

fun report(executor: Player, suspect: Player) {
  if(executor.uniqueId == suspect.uniqueId) {
    executor.sendMessage("<error> Don't try to report yourself, silly.".mm())
    return
  }

  val template = """
      xxxxxxxxx
      xx1x2x3xx
      xxxxxxxxz
    """.trimIndent()

  val gui = GUI(Buffered.instance, template, "Report ${suspect.name}")

  val pvpItem = ItemStack(Material.DIAMOND_SWORD)

  gui.put('1', pvpItem) {
    executor.sendReport("PvP Hacks", suspect)
  }

  val movementItem = ItemStack(Material.CHAIN)

  gui.put('2', movementItem) {
    executor.sendReport("Movement Hacks", suspect)
  }

  val chatItem = ItemStack(Material.NETHER_STAR)

  gui.put('3', chatItem) {
    executor.sendReport("Chat Violations", suspect)
  }

  gui.open(executor)
}