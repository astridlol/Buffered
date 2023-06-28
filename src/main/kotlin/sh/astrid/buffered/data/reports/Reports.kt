package sh.astrid.buffered.data.reports

import org.litote.kmongo.getCollection
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.data.Database
import sh.astrid.buffered.data.PlayerEntry
import sh.astrid.buffered.data.ReportData
import sh.astrid.buffered.data.ReportEntry

class Reports {
  private val db = Database.get()

  fun createReport(data: ReportData): ReportEntry {
    val col = db.getCollection<ReportEntry>("reports")
    val report = ReportEntry(
            suspectUUID = data.suspectUUID.toString(),
            creatorUUID = data.creatorUUID.toString(),
            reason = data.reason,
            createdAt = System.currentTimeMillis()
    )
    col.insertOne(report)
    return report
  }
}