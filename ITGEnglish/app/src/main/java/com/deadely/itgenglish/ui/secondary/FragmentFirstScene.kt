package com.deadely.itgenglish.ui.secondary

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.speech.tts.TextToSpeech
import android.view.MotionEvent
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.deadely.itgenglish.R
import com.deadely.itgenglish.base.BaseFragment
import com.deadely.itgenglish.extensions.makeGone
import com.deadely.itgenglish.extensions.makeVisible
import com.deadely.itgenglish.extensions.snack
import com.deadely.itgenglish.model.Text
import com.deadely.itgenglish.model.toFemaleSpeech
import com.deadely.itgenglish.model.toUserSpeech
import com.deadely.itgenglish.ui.firstscrene.ConditionActivity
import com.deadely.itgenglish.ui.firstscrene.FragmentFirstViewModel
import com.deadely.itgenglish.utils.DataState
import com.deadely.itgenglish.utils.IS_FINAL
import com.deadely.itgenglish.utils.Utils.printText
import com.deadely.itgenglish.utils.Word
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dictionary.*
import kotlinx.android.synthetic.main.fragment_first_scene.*
import java.util.*
import kotlin.concurrent.schedule


@AndroidEntryPoint
class FragmentFirstScene : BaseFragment(R.layout.fragment_first_scene) {
    val viewModel: FragmentFirstViewModel by viewModels()

    var asks = listOf<Text>()
    private var isRecording: Boolean = false
    var index: Int = 0
    private var ttsEnabled: Boolean = false
    private lateinit var tts: TextToSpeech
    var translatedAsk = ""

    override fun initView() {
        val dialog = context?.let { Dialog(it) }
        dialog?.apply {
            setContentView(R.layout.next_lesson_dialog)
            findViewById<TextView>(R.id.btnOk).setOnClickListener {
                dismiss()
            }
            setOnDismissListener {
                viewModel.initData()
            }
        }
        dialog?.show()
        initTts()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setListeners() {
        rlMic.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    if (!isRecording)
                        startRecorder()
                }
                MotionEvent.ACTION_UP -> {
                    if (isRecording) {
                        stopRecorder()
                    } else {
                        Timer().schedule(1000) {
                            if (isRecording) {
                                stopRecorder()
                            }
                        }
                    }
                }
            }
            true
        }
        icMic.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    if (!isRecording)
                        startRecorder()
                }
                MotionEvent.ACTION_UP -> {
                    if (isRecording) {
                        stopRecorder()
                    } else {
                        Timer().schedule(1000) {
                            if (isRecording) {
                                stopRecorder()
                            }
                        }
                    }
                }
            }
            true
        }
        llDialog.setOnClickListener { }
        tvAsk.setOnClickListener {
            index++
            initAsk()
        }
    }

    private fun startRecorder() {
        context?.let { context ->
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val permissions = arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                activity?.let { activity ->
                    ActivityCompat.requestPermissions(
                        activity,
                        permissions,
                        0
                    )
                }
            } else {
                viewModel.startRecord()
            }
        }
    }

    private fun stopRecorder() {
        viewModel.stopRecord()
    }

    override fun initObserver() {
        viewModel.isRecord.observe(this, {
            isRecording = it
            if (it) {
                icMic.setImageResource(R.drawable.ic_baseline_mic_on)
            } else {
                icMic.setImageResource(R.drawable.ic_baseline_mic_off_24)
            }
        })
        viewModel.translatedText.observe(this, {
            when (it) {
                is DataState.Loading -> {
                    icMic.makeGone()
                    pvLoad.makeVisible()
                }
                is DataState.Error -> {
                    icMic.makeVisible()
                    pvLoad.makeGone()
                    snack(tvAsk, "Возникла ошибка соединения. Попробуйте еще раз")
                }
                is DataState.Success -> {
                    icMic.makeVisible()
                    pvLoad.makeGone()
                    translatedAsk = it.data
                    viewModel.compareData(it.data, asks[index].text)
                }
            }
        })
        viewModel.asks.observe(this, {
            asks = it
            initAsk()
        })
        viewModel.isValid.observe(this, {
            if (!it) {
                if (translatedAsk.isNullOrEmpty()) {
                    tvAsk.text = toFemaleSpeech(getString(R.string.no_right_def))
                    Timer().schedule(1000) {
                        initAsk()
                    }
                } else {
                    tvAsk.text = toFemaleSpeech(getString(R.string.no_right_def_one))
                }
            } else {
                if (index + 1 == asks.size) {
                    openFirstScene()
                } else {
                    index += 1
                    initAsk()
                }
            }
        })
    }

    private fun initAsk() {
        if (index + 1 == asks.size) {
            openFirstScene()
        } else {
            if (asks[index].isUser) {
                activity?.runOnUiThread {
                    tvAsk.text = toUserSpeech(asks[index].text)
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sayWordGreater21(asks[index].text)
                } else {
                    sayWordUnder20(asks[index].text)
                }
                val word = Word(100, toFemaleSpeech(asks[index].text)).apply { offset = 50 }
                printText(word, tvAsk)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun sayWordGreater21(word: String) {
        val utteranceId = this.hashCode().toString() + ""
        tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    private fun sayWordUnder20(word: String) {
        val map: HashMap<String, String> = HashMap()
        map[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "MessageId"
        tts.speak(word, TextToSpeech.QUEUE_FLUSH, map)
    }

    override fun getExtras() {

    }

    private fun initTts() {
        context?.let { context ->
            tts = TextToSpeech(context) { initStatus ->
                if (initStatus == TextToSpeech.SUCCESS) {
                    tts.language = Locale.ENGLISH
                    tts.setPitch(1.3f)
                    tts.setSpeechRate(1f)
                    ttsEnabled = true
                } else {
                    snack(rvWords, getString(R.string.happened_error))
                    ttsEnabled = false
                }
            }
        }
    }

    private fun openFirstScene() {
        val intent = Intent(activity, ConditionActivity::class.java).apply {
            putExtra(IS_FINAL, true)
        }
        startActivity(intent)
    }
}
