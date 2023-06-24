package sh.astrid.buffered.data

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.*
import sh.astrid.buffered.Buffered
import java.util.logging.Level

// core functions for accessing the db stuff directly

object Database {
    private val conn: MongoClient
    private val db: MongoDatabase
    private var connected: Boolean = false

    init {
        val plugin = Buffered.instance
        plugin.logger.info("Connecting to database... Things might lag!")

        val connectionStr: String = plugin.config.getString("mongo_uri") ?: ""

        if(connectionStr.isEmpty()) {
            plugin.logger.log(Level.SEVERE, "Could not connect to MongoDB! Please specify a valid Mongo URI in the config.")
        }

        System.setProperty("org.litote.mongo.test.mapping.service", "org.litote.kmongo.jackson.JacksonClassMappingTypeService")

        conn = KMongo.createClient(connectionStr)
        connected = true

        db = conn.getDatabase("Buffered")
    }

    fun close() {
        if(!connected) return
        conn.close()
    }

    fun isConnected() = connected

    fun load() {
        Buffered.instance.logger.info("Successfully loaded MongoDB!")
    }

    fun get() = db
}