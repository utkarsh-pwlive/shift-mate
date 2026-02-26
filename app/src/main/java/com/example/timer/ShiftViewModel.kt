package com.example.timer

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class ShiftViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs: SharedPreferences =
        application.getSharedPreferences("shift_prefs", Context.MODE_PRIVATE)

    private fun str(key: String, default: String = "") =
        prefs.getString(key, default) ?: default

    private var _shiftHours by mutableStateOf(str("shiftHours"))
    var shiftHours: String
        get() = _shiftHours
        set(value) {
            _shiftHours = value; prefs.edit().putString("shiftHours", value).apply()
        }

    private var _shiftMinutes by mutableStateOf(str("shiftMinutes"))
    var shiftMinutes: String
        get() = _shiftMinutes
        set(value) {
            _shiftMinutes = value; prefs.edit().putString("shiftMinutes", value).apply()
        }

    private var _hour by mutableStateOf(str("hour"))
    var hour: String
        get() = _hour
        set(value) {
            _hour = value; prefs.edit().putString("hour", value).apply()
        }

    private var _minute by mutableStateOf(str("minute"))
    var minute: String
        get() = _minute
        set(value) {
            _minute = value; prefs.edit().putString("minute", value).apply()
        }

    private var _second by mutableStateOf(str("second"))
    var second: String
        get() = _second
        set(value) {
            _second = value; prefs.edit().putString("second", value).apply()
        }

    private var _selectedPeriod by mutableStateOf(str("selectedPeriod", "AM"))
    var selectedPeriod: String
        get() = _selectedPeriod
        set(value) {
            _selectedPeriod = value; prefs.edit().putString("selectedPeriod", value).apply()
        }

    private var _checkoutTimeStr by mutableStateOf(str("checkoutTimeStr"))
    var checkoutTimeStr: String
        get() = _checkoutTimeStr
        set(value) {
            _checkoutTimeStr = value; prefs.edit().putString("checkoutTimeStr", value).apply()
        }

    private var _timeLeftHoursValue by mutableStateOf(
        str("timeLeftHours", "-1.0").toDoubleOrNull() ?: -1.0
    )
    var timeLeftHoursValue: Double
        get() = _timeLeftHoursValue
        set(value) {
            _timeLeftHoursValue = value; prefs.edit().putString("timeLeftHours", value.toString())
                .apply()
        }

    private var _shiftCompleted by mutableStateOf(prefs.getBoolean("shiftCompleted", false))
    var shiftCompleted: Boolean
        get() = _shiftCompleted
        set(value) {
            _shiftCompleted = value; prefs.edit().putBoolean("shiftCompleted", value).apply()
        }

    var error by mutableStateOf("")

    var expanded by mutableStateOf(false)
}
