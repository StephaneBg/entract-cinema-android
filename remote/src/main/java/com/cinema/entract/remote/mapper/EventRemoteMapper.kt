package com.cinema.entract.remote.mapper

import com.cinema.entract.data.model.EventData
import com.cinema.entract.remote.model.EventRemote

class EventRemoteMapper : RemoteMapper<EventRemote, EventData> {

    override fun mapToData(model: EventRemote) = EventData(model.lien)
}