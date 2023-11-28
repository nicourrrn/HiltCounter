package com.nicourrrn.testprojectmobile

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.nicourrrn.testprojectmobile.repo.CounterService
import com.nicourrrn.testprojectmobile.ui.theme.TestProjectMobileTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltAndroidApp
class CounterApplication: Application()

data class CounterState(
    val count: Int = 0
)

sealed class CounterEvent {
    object Init : CounterEvent()
    object Inc : CounterEvent()
    object Dec : CounterEvent()
}

@HiltViewModel
class CounterViewModel @Inject constructor(
    private val counterService: CounterService
) : ViewModel() {
    private var _state = mutableStateOf(CounterState())
    val state: State<CounterState> = _state

    @OptIn(DelicateCoroutinesApi::class)
    fun onEvent(event: CounterEvent){
        when (event) {
            is CounterEvent.Init -> {
                GlobalScope.launch {
                    _state.value = _state.value.copy(count = counterService.getCounter())
                }
            }
            is CounterEvent.Inc -> {
                GlobalScope.launch {
                    counterService.incCounter()
                }
                _state.value = _state.value.copy(count = state.value.count+1)
            }
            is CounterEvent.Dec -> {
                _state.value = _state.value.copy(count = state.value.count-1)
            }
        }
    }

}

@AndroidEntryPoint
class CounterActivity : ComponentActivity() {

    val viewModel: CounterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Text("Counter: ${viewModel.state.value.count}")
                        Button(onClick = {viewModel.onEvent(CounterEvent.Inc)}) {
                            Text("Inc")
                        }
                        Button(onClick = { viewModel.onEvent(CounterEvent.Init) }) {
                            Text("Update")
                        }
                    }
                }
        }
    }
}

