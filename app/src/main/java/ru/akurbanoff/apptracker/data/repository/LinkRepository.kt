package ru.akurbanoff.apptracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.akurbanoff.apptracker.data.mapper.LinkDtoToLinkMapper
import ru.akurbanoff.apptracker.data.mapper.LinkToDtoMapper
import ru.akurbanoff.apptracker.domain.model.Link
import ru.akurbanoff.apptracker.storage.AppTrackerDatabase
import ru.akurbanoff.apptracker.storage.dao.LinkDao
import javax.inject.Inject

class LinkRepository @Inject constructor(
    private val database: AppTrackerDatabase,
    private val linkDao: LinkDao,
    private val linkToDtoMapper: LinkToDtoMapper,
    private val linkDtoToLinkMapper: LinkDtoToLinkMapper
) {

    suspend fun getAllLinks(): Flow<List<Link>>{
        return linkDao.getAll().map { linkDtos ->
            linkDtos.map { linkDto ->
                linkDtoToLinkMapper.invoke(linkDto)
            }
        }
    }

    suspend fun createLink(link: Link){
        linkDao.insertOrUpdate(linkToDtoMapper.invoke(link))
    }
}