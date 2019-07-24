package com.seanshubin.condorcet.domain.db

import java.sql.ResultSet

class ResourceEventDbQueries(private val dbFromResource: DbFromResource) :
        EventDbQueries,
        DbFromResource by dbFromResource {
    override fun eventsToSync(lastEventSynced: Int): List<EventDetail> =
            query(::createInitiatorAndEvent, "list-unsynced-events.sql", lastEventSynced)

    override fun lastSynced(): Int? =
            queryZeroOrOneInt("get-last-synced.sql")

    private fun createInitiatorAndEvent(resultSet: ResultSet): EventDetail {
        val id = resultSet.getInt("id")
        val source = resultSet.getString("source")
        val type = resultSet.getString("type")
        val owner = resultSet.getString("owner")
        val text = resultSet.getString("text")
        val whenHappened = resultSet.getTimestamp("when").toInstant()
        val event = Event.parse(type, text)
        return EventDetail(id, source, owner, whenHappened, event)
    }
}
