package ru.akurbanoff.apptracker.ui.app_list

import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.akurbanoff.apptracker.AppTrackerApplication
import ru.akurbanoff.apptracker.data.repository.AppsRepository
import ru.akurbanoff.apptracker.data.repository.LinkRepository
import ru.akurbanoff.apptracker.domain.model.App
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.domain.model.Link
import ru.akurbanoff.apptracker.domain.model.LinkWithRules
import ru.akurbanoff.apptracker.domain.model.Rule
import java.util.Random
import javax.inject.Inject

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val appsRepository: AppsRepository,
    private val linkRepository: LinkRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AppListState())
    val state: StateFlow<AppListState> = _state

    private val shouldShowAllApps = MutableStateFlow(true)
    private val searchQueryState = MutableStateFlow("")

    private var apps: List<AppWithRules> = listOf()
    private var links: List<LinkWithRules> = listOf()

    private var imageJob: Job? = null

    private val _bitmapMap = MutableStateFlow<HashMap<String, Bitmap>>(hashMapOf())
    val bitmapMap: StateFlow<HashMap<String, Bitmap>> = _bitmapMap

    init {
        updateState(apps = appsRepository.cachedApps)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getApps() {
        viewModelScope.launch(Dispatchers.IO) {
            searchQueryState.flatMapLatest { query ->
                shouldShowAllApps.flatMapLatest { show ->
                    appsRepository.getAllApps(allApps = show, query = query)
                        .map {
                            Result.success(it)
                        }.catch {
                            Result.failure<Throwable>(it)
                        }
                }
            }.collectLatest { appsResult ->
                when {
                    appsResult.isFailure -> {
                        _state.update {
                            it.copy(
                                isAppsFailure = appsResult.exceptionOrNull()
                            )
                        }
                    }

                    appsResult.isSuccess -> {
                        _state.update {
                            it.copy(
                                isAppsFailure = null,
                            )
                        }
                        updateState(appsResult.getOrNull() ?: emptyList())
                    }
                }
            }
        }
    }

    fun getLinks(){
        viewModelScope.launch(Dispatchers.IO) {
            linkRepository.getAllLinks().map {
                Result.success(it)
            }.catch {
                Result.failure<Throwable>(it)
            }.collectLatest { linkResult ->
                when{
                    linkResult.isSuccess -> {
                        updateLinkState(linkResult.getOrNull() ?: emptyList())
                    }
                }
            }
        }
    }

    fun switchAllApps() {
        val isAllAppsEnabled = !_state.value.isAllAppsEnabled
        shouldShowAllApps.value = isAllAppsEnabled
        _state.update { it.copy(isAllAppsEnabled = isAllAppsEnabled) }
    }

    fun checkApp(itemApp: AppWithRules, enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            appsRepository.checkApp(itemApp.app.packageName, enabled)
        }
    }

    fun checkLink(itemLink: LinkWithRules, enabled: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            linkRepository.checkLink(itemLink.link.link, enabled)
        }
    }

    private fun requestImagesFor(itemApps: List<AppWithRules>) {
        val list = ArrayList(itemApps)
        if (imageJob != null) return
        if (list.size == bitmapMap.value.size) return

        imageJob = viewModelScope.launch(Dispatchers.IO) {
            for (itemApp in list) {
                val packageManager = AppTrackerApplication.INSTANCE?.packageManager
                val appIcon = packageManager?.getApplicationIcon(itemApp.app.packageName)?.toBitmap() ?: return@launch
                _bitmapMap.value[itemApp.app.packageName] = appIcon
            }

            _bitmapMap.value = _bitmapMap.value
        }

        updateState(apps)
    }

    private fun updateState(apps: List<AppWithRules>) = _state.update {
        this.apps = apps
        requestImagesFor(apps)

        it.copy(
            apps = apps.sortedBy { apps -> apps.app.name },
            amountOfEnabledApps = apps.count { app -> app.app.enabled }
        )
    }

    private fun updateLinkState(links: List<LinkWithRules>) = _state.update {
        this.links = links
        it.copy(
            links = links.sortedBy { link -> link.link.title }
        )
    }

    fun onSearch(query: String) {
        searchQueryState.value = query
    }

    fun getIconFor(packageManager: PackageManager, app: App) =
        packageManager.getApplicationIcon(app.packageName).toBitmap()

    fun setTimeLimitRule(packageName: String, enabled: Boolean, hour: Int, minute: Int) {
        val limitInSeconds = ((hour * 60) + minute) * 60
        val rule = Rule.TimeLimitRule(
            id = Random(System.currentTimeMillis()).nextInt(),
            packageName = packageName,
            enabled = enabled,
            limitInSeconds = limitInSeconds,
            link = "",
        )
        viewModelScope.launch(Dispatchers.IO) {
            appsRepository.addRule(rule)
        }
    }

    fun setLinkTimeLimitRule(link: String, enabled: Boolean, hour: Int, minute: Int){
        val limitInSeconds = ((hour * 60) + minute) * 60
        val rule = Rule.TimeLimitRule(
            id = Random(System.currentTimeMillis()).nextInt(),
            packageName = "",
            enabled = enabled,
            limitInSeconds = limitInSeconds,
            link = link,
        )
        viewModelScope.launch(Dispatchers.IO) {
            appsRepository.addRule(rule)
        }
    }

    fun setHourOfTheDayRangeRule(
        packageName: String,
        enabled: Boolean,
        from: Pair<Int, Int>?,
        to: Pair<Int, Int>?,
    ) = viewModelScope.launch(Dispatchers.IO) {

        val existingRule = appsRepository.getRulesFor(packageName).firstOrNull { it is Rule.HourOfTheDayRangeRule }
        var rule = existingRule as? Rule.HourOfTheDayRangeRule
        if (rule == null) {
            rule = Rule.HourOfTheDayRangeRule(
                id = Random(System.currentTimeMillis()).nextInt(),
                enabled = enabled,
                packageName = packageName,
                link = "",
                fromHour = 0,
                fromMinute = 0,
                toHour = 0,
                toMinute = 0,
            )
        }
        from?.let { rule = rule?.copy(fromHour = from.first, fromMinute = from.second) }
        to?.let { rule = rule?.copy(toHour = to.first, toMinute = to.second) }
        appsRepository.addRule(rule ?: return@launch)
    }

    fun setLinkHourOfTheDayRangeRule(link: String, enabled: Boolean, from: Pair<Int, Int>?, to: Pair<Int, Int>?) = viewModelScope.launch(Dispatchers.IO) {
        val existingRule = linkRepository.getRulesFor(link).firstOrNull { it is Rule.HourOfTheDayRangeRule }
        var rule = existingRule as? Rule.HourOfTheDayRangeRule
        if (rule == null) {
            rule = Rule.HourOfTheDayRangeRule(
                id = Random(System.currentTimeMillis()).nextInt(),
                enabled = enabled,
                packageName = "",
                link = link,
                fromHour = 0,
                fromMinute = 0,
                toHour = 0,
                toMinute = 0,
            )
        }
        from?.let { rule = rule?.copy(fromHour = from.first, fromMinute = from.second) }
        to?.let { rule = rule?.copy(toHour = to.first, toMinute = to.second) }
        linkRepository.addRule(rule ?: return@launch)
    }

    fun createLink(title: String, link: String) {
        viewModelScope.launch(Dispatchers.IO) {
            linkRepository.createLink(Link(title, link, false))
        }
    }

    data class AppListState(
        val apps: List<AppWithRules> = emptyList(),
        val links: List<LinkWithRules> = emptyList(),
        val isAllAppsEnabled: Boolean = true,
        var amountOfEnabledApps: Int = 0,
        var isAppsFailure: Throwable? = null,
    )
}
