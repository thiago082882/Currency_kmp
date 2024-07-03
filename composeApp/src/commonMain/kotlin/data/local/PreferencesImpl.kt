package data.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import domain.PreferencesRepository
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime

class PreferencesImpl(
    private val settings: Settings
):PreferencesRepository {

    companion object {
        const val TIMESTAMP_KEY = "lastUpdated"
    }
    @OptIn(ExperimentalSettingsApi::class)
    private val flowSettings : FlowSettings = (settings as ObservableSettings).toFlowSettings()
    @OptIn(ExperimentalSettingsApi::class)
    override  suspend fun saveLastUpdated(lastUpdated: String) {
        flowSettings.putLong(
            key= TIMESTAMP_KEY,
            value = Instant.parse(lastUpdated).toEpochMilliseconds()
        )
    }

    @OptIn(ExperimentalSettingsApi::class)
    override suspend fun isDataFresh(currentTimeStamp: Long): Boolean {
        val savedTimestamp = flowSettings.getLong(key = TIMESTAMP_KEY, defaultValue = 0L)
        return if(savedTimestamp != 0L){
            val currentTimeInstant = Instant.fromEpochMilliseconds(currentTimeStamp)
            val savedTimeStamp = Instant.fromEpochMilliseconds(savedTimestamp)

            val currentDate = currentTimeInstant.toLocalDateTime(TimeZone.currentSystemDefault())
            val savedDate = savedTimeStamp.toLocalDateTime(TimeZone.currentSystemDefault())
            val diff = currentDate.date.daysUntil(savedDate.date)

            (diff < 1)

        } else {
            false
        }
    }
}