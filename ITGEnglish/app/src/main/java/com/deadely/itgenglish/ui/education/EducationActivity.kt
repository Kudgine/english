package com.deadely.itgenglish.ui.education

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.speech.tts.TextToSpeech
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.deadely.itgenglish.R
import com.deadely.itgenglish.base.BaseActivity
import com.deadely.itgenglish.extensions.snack
import com.deadely.itgenglish.model.Ask
import com.deadely.itgenglish.model.Lessons
import com.deadely.itgenglish.utils.DataState
import com.deadely.itgenglish.utils.LESSON
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dictionary.*
import kotlinx.android.synthetic.main.fragment_education.*
import java.util.*


@AndroidEntryPoint
class EducationActivity : BaseActivity(R.layout.fragment_education) {

    private val viewModel: EducationViewModel by viewModels()

    private var isRecording: Boolean = false

    var asks = listOf<Ask>()

    var index: Int = 0
    private var ttsEnabled: Boolean = false
    private lateinit var tts: TextToSpeech
    var translatedAsk = ""

    override fun initView() {
        title = getString(R.string.education)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        initTts()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setListeners() {
        pointTwo.setOnClickListener { }
        pointOne.setOnClickListener { }
//        ivPlay.setOnTouchListener { _, event ->
//            when (event.actionMasked) {
//                MotionEvent.ACTION_DOWN -> {
//                    if (!isRecording)
//                        startRecorder()
//                }
//                MotionEvent.ACTION_UP -> {
//                    if (isRecording) {
//                        stopRecorder()
//                    } else {
//                        Timer().schedule(1000) {
//                            if (isRecording) {
//                                stopRecorder()
//                            }
//                        }
//                    }
//                }
//            }
//            true
//        }
//        ivVolume.setOnClickListener {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                asks[index].text?.let { text -> sayWordGreater21(text) }
//            } else {
//                asks[index].text?.let { text -> sayWordUnder20(text) }
//            }
//        }
    }

    private fun startRecorder() {
        if (ContextCompat.checkSelfPermission(
                baseContext,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                baseContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(
                this,
                permissions,
                0
            )
        } else {
            viewModel.startRecord()
        }
    }

    private fun stopRecorder() {
        viewModel.stopRecord()
    }

    override fun initObserver() {
        viewModel.isRecord.observe(this, {
            isRecording = it
//            if (it) {
//                ivPlay.setImageResource(R.drawable.ic_baseline_mic_on)
//            } else {
//                ivPlay.setImageResource(R.drawable.ic_baseline_mic_off_24)
//            }
        })
        viewModel.translatedText.observe(this, {
            when (it) {
                is DataState.Loading -> {
//                    ivPlay.makeGone()
//                    pvLoad.makeVisible()
                }
                is DataState.Error -> {
//                    ivPlay.makeVisible()
//                    pvLoad.makeGone()
//                    snack(ivPlay, "Возникла ошибка соединения. Попробуйте еще раз")
                }
                is DataState.Success -> {
//                    ivPlay.makeVisible()
//                    pvLoad.makeGone()
                    translatedAsk = it.data
                    viewModel.compareData(it.data, asks[index].text)
                }
            }
        })
        viewModel.lessons.observe(this, {
            it.asks?.let { data -> asks = data }
            initAsk()
        })
        viewModel.isValid.observe(this, {
            if (!it) {
//                if (translatedAsk.isNullOrEmpty()) {
//                    snack(ivPlay, getString(R.string.no_right_def))
//                } else {
//                    snack(ivPlay, getString(R.string.no_right).format("\"$translatedAsk\" \n"))
//                }
            } else {
                if (index + 1 == asks.size) {
                    val dialog = Dialog(this)
                    dialog.apply {
                        setContentView(R.layout.next_lesson_dialog)
                        findViewById<TextView>(R.id.btnOk).setOnClickListener {
                            dismiss()
                            finish()
                        }
                        setOnDismissListener {
                            finish()
                        }
                    }
                    dialog.show()
                } else {
                    index += 1
                    initAsk()
                }
            }
        })
    }

    private fun initAsk() {
//        tvAsk.text = asks[index].text
    }

    override fun getExtras() {
        intent?.extras?.let {
            it.getBundle(LESSON)?.getParcelable<Lessons>(LESSON)
                ?.let { lesson -> viewModel.setLesson(lesson) }
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

    private fun initTts() {
        tts = TextToSpeech(baseContext) { initStatus ->
            if (initStatus == TextToSpeech.SUCCESS) {
                tts.language = Locale.ENGLISH
                tts.setPitch(1.3f)
                tts.setSpeechRate(0.5f)
                ttsEnabled = true
            } else {
                snack(rvWords, getString(R.string.happened_error))
                ttsEnabled = false
            }
        }
    }
}