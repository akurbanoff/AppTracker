package ru.akurbanoff.apptracker.data.mapper

import ru.akurbanoff.apptracker.domain.model.Link
import ru.akurbanoff.apptracker.storage.dto.LinkDto
import javax.inject.Inject

class LinkDtoToLinkMapper @Inject constructor() {
    operator fun invoke(linkDto: LinkDto) = Link(
        title = linkDto.title,
        link = linkDto.link,
        enabled = linkDto.enabled
    )
}