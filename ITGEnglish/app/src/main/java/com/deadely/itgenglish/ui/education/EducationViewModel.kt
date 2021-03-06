package com.deadely.itgenglish.ui.education

import android.content.Context
import android.media.MediaRecorder
import android.os.Environment
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.deadely.itgenglish.base.BaseViewModel
import com.deadely.itgenglish.model.Lessons
import com.deadely.itgenglish.repository.Repository
import com.deadely.itgenglish.utils.DataState
import com.deadely.itgenglish.utils.POST_AUDIO
import com.deadely.itgenglish.utils.PreferencesManager
import com.deadely.itgenglish.utils.PreferencesManager.get
import com.deadely.itgenglish.utils.TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File

class EducationViewModel @ViewModelInject constructor(
    private val repository: Repository,
    @ApplicationContext private val context: Context,
    @Assisted private val savedStateHandle: SavedStateHandle
) :
    BaseViewModel() {

    private var isRecording: Boolean = false

    private var mTranslatedText = MutableLiveData<DataState<String>>()
    var translatedText: LiveData<DataState<String>> = mTranslatedText

    private var mIsValid = MutableLiveData<Boolean>()
    val isValid: LiveData<Boolean>
        get() = mIsValid

    private var mIsRecord = MutableLiveData<Boolean>()
    var isRecord: LiveData<Boolean> = mIsRecord

    private var mLessons = MutableLiveData<Lessons>()
    var lessons: LiveData<Lessons> = mLessons

    private var recorder: MediaRecorder? = null

    private val fileName = "${Environment.getExternalStorageDirectory().absolutePath}/output.3gp"

    private val preferences = PreferencesManager.defaultPrefs(context)

    fun startRecord() {
        if (!isRecording) {
            releaseRecorder()
            val outFile = File(fileName)
            if (outFile.exists()) {
                outFile.delete()
            }
            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setMaxDuration(10000)
                setOutputFile(fileName)
                prepare()
                start()
            }
            isRecording = true
            mIsRecord.postValue(isRecording)
        } else {
            stopRecord()
        }
    }

    private fun releaseRecorder() {
        recorder?.release()
        recorder = null
    }

    fun stopRecord() {
        if (isRecording) {
            recorder?.apply {
                stop()
            }
            isRecording = false
            mIsRecord.postValue(isRecording)
        }
        sendAudio()
    }

    fun setLesson(data: Lessons) {
        mLessons.postValue(data)
    }

    fun compareData(newValue: String, initValue: String?) {
        val formattedInitValue = initValue?.replace(",", "")?.replace(".", "")
        val parts = formattedInitValue?.split(" ")
        if (newValue.isNullOrEmpty()) {
            mIsValid.postValue(false)
            return
        }
        var result = formattedInitValue
        parts?.forEach {
            if (formattedInitValue.equals(it, true)) {
                result = formattedInitValue.replace(it, "")
            }
        }
        mIsValid.postValue(!result?.trim().isNullOrEmpty())
    }

    private fun sendAudio() {
        val audioFile = File(fileName)
        viewModelScope.launch {
            repository.sendAudio(
                preferences.get(TOKEN, "") ?: "", audioFile
            )
                .onEach { dataState -> subscribeData(dataState, POST_AUDIO) }
                .launchIn(viewModelScope)
        }
    }

    private fun subscribeData(dataState: DataState<Any>, code: String) {
        when (dataState) {
            is DataState.Loading -> {
                when (code) {
                    POST_AUDIO -> {
                        mTranslatedText.postValue(DataState.Loading)
                    }
                }
            }
            is DataState.Error -> {
                when (code) {
                    POST_AUDIO -> {
                        mTranslatedText.postValue(DataState.Error(dataState.exception))
                    }
                }

            }
            is DataState.Success -> {
                when (code) {
                    POST_AUDIO -> {
                        mTranslatedText.postValue(DataState.Success(dataState.data as String))
                    }
                }
            }
        }
    }
}
