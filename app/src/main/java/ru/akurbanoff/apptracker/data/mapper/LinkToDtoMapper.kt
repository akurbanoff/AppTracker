package ru.akurbanoff.apptracker.data.mapper

import ru.akurbanoff.apptracker.domain.model.Link
import ru.akurbanoff.apptracker.storage.dto.AppDto
import ru.akurbanoff.apptracker.storage.dto.LinkDto
import javax.inject.Inject

class LinkToDtoMapper @Inject constructor() {
    operator fun invoke(link: Link): LinkDto = LinkDto(
        title = link.title,
        link = link.link,
        enabled = link.enabled
    )
}