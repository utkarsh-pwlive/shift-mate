package com.example.timer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeCalculator(
    vm: ShiftViewModel = viewModel()
) {

    val isShiftValid = vm.shiftHours.isNotEmpty() && vm.shiftMinutes.isNotEmpty()

    val checkoutTime = vm.checkoutTimeStr
        .takeIf { it.isNotEmpty() }
        ?.let { LocalTime.parse(it) }

    val timeLeftHours = vm.timeLeftHoursValue
        .takeIf { it >= 0 }

    val formatter = DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.ENGLISH)
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Shift Calculator",
                fontSize = 26.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Enter Shift Duration")

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedTextField(
                    value = vm.shiftHours,
                    onValueChange = {
                        vm.shiftHours = it.filter { c -> c.isDigit() }.take(2)
                    },
                    label = { Text("Shift HH") },
                    modifier = Modifier.width(120.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = vm.shiftMinutes,
                    onValueChange = {
                        vm.shiftMinutes = it.filter { c -> c.isDigit() }.take(2)
                    },
                    label = { Text("Shift MM") },
                    modifier = Modifier.width(120.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier.alpha(if (isShiftValid) 1f else 0.4f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("Enter Punch In Time")

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    OutlinedTextField(
                        value = vm.hour,
                        onValueChange = {
                            if (isShiftValid)
                                vm.hour = it.filter { c -> c.isDigit() }.take(2)
                        },
                        label = { Text("HH") },
                        modifier = Modifier.width(80.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        enabled = isShiftValid,
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = vm.minute,
                        onValueChange = {
                            if (isShiftValid)
                                vm.minute = it.filter { c -> c.isDigit() }.take(2)
                        },
                        label = { Text("MM") },
                        modifier = Modifier.width(80.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        enabled = isShiftValid,
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = vm.second,
                        onValueChange = {
                            if (isShiftValid)
                                vm.second = it.filter { c -> c.isDigit() }.take(2)
                        },
                        label = { Text("SS") },
                        modifier = Modifier.width(80.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        enabled = isShiftValid,
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = vm.expanded,
                    onExpandedChange = {
                        if (isShiftValid)
                            vm.expanded = !vm.expanded
                    }
                ) {

                    OutlinedTextField(
                        value = vm.selectedPeriod,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("AM / PM") },
                        modifier = Modifier.menuAnchor(),
                        enabled = isShiftValid,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults
                                .TrailingIcon(vm.expanded)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = vm.expanded,
                        onDismissRequest = { vm.expanded = false }
                    ) {

                        listOf("AM", "PM").forEach {

                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    vm.selectedPeriod = it
                                    vm.expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    enabled = isShiftValid,
                    onClick = {
                        keyboardController?.hide()
                        try {

                            val shiftH = vm.shiftHours.toLong()
                            val shiftM = vm.shiftMinutes.toLong()

                            val h = vm.hour.toInt()
                            val m = vm.minute.toInt()
                            val s = vm.second.toInt()

                            if (h !in 1..12 ||
                                m !in 0..59 ||
                                s !in 0..59 ||
                                shiftM !in 0..59
                            ) {
                                throw Exception()
                            }

                            val hour24 = when {
                                vm.selectedPeriod == "AM" && h == 12 -> 0
                                vm.selectedPeriod == "PM" && h != 12 -> h + 12
                                else -> h
                            }

                            val punchIn = LocalTime.of(hour24, m, s)
                            val checkout = punchIn
                                .plusHours(shiftH)
                                .plusMinutes(shiftM)

                            vm.checkoutTimeStr = checkout.toString()

                            val now = LocalTime.now()
                            val minutesLeft = Duration.between(now, checkout).toMinutes()

                            if (minutesLeft <= 0) {
                                vm.shiftCompleted = true
                                vm.timeLeftHoursValue = -1.0
                            } else {
                                vm.shiftCompleted = false
                                vm.timeLeftHoursValue = minutesLeft / 60.0
                            }

                            vm.error = ""

                        } catch (e: Exception) {

                            vm.error = "Enter valid values"
                            vm.checkoutTimeStr = ""
                            vm.timeLeftHoursValue = -1.0
                            vm.shiftCompleted = false
                        }
                    }
                ) {
                    Text("Calculate")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            checkoutTime?.let {

                Text(
                    "Checkout Time: ${it.format(formatter)}",
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            when {

                vm.shiftCompleted -> {

                    Text(
                        "🎉 Hurray! Your shift is completed",
                        fontSize = 20.sp,
                        color = Color(0xFF2E7D32)
                    )
                }

                timeLeftHours != null -> {

                    Text(
                        "Time Left: ${
                            String.format(
                                "%.2f",
                                timeLeftHours
                            )
                        } hours",
                        fontSize = 20.sp
                    )
                }
            }

            if (vm.error.isNotEmpty()) {

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    vm.error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}