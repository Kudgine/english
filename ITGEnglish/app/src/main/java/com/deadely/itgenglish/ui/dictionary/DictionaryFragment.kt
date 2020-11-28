package com.deadely.itgenglish.ui.dictionary

import android.os.Build
import android.speech.tts.TextToSpeech
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.deadely.itgenglish.R
import com.deadely.itgenglish.base.BaseFragment
import com.deadely.itgenglish.extensions.makeGone
import com.deadely.itgenglish.extensions.makeVisible
import com.deadely.itgenglish.extensions.setActivityTitle
import com.deadely.itgenglish.extensions.snack
import com.deadely.itgenglish.model.Word
import com.deadely.itgenglish.utils.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dictionary.*
import kotlinx.android.synthetic.main.view_search.*
import java.util.*
import kotlin.collections.HashMap

@AndroidEntryPoint
class DictionaryFragment : BaseFragment(R.layout.fragment_dictionary) {

    private val viewModel: DictionaryViewModel by viewModels()

    var wordsAdapter = DictionaryAdapter()

    private var ttsEnabled: Boolean = false
    private lateinit var tts: TextToSpeech

    override fun initView() {
        initTts()
        setActivityTitle(R.string.dictionary)
        rvWords.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = wordsAdapter
        }
    }

    override fun setListeners() {
        etSearch.addTextChangedListener {
            viewModel.onSearchTextChanged(it.toString())
            if (it.toString().isNotEmpty()) {
                ivClose.makeVisible()
            } else {
                ivClose.makeGone()
            }
        }
        ivClose.setOnClickListener { etSearch.text.clear() }
        wordsAdapter.setOnClickListener(object : DictionaryAdapter.OnItemClickListener {
            override fun doFavorite(word: Word) {
                viewModel.addFavorite(word)
            }

            override fun removeFromFavorite(word: Word) {
                viewModel.deleteFavorites(word)
            }

            override fun onVolumeClick(word: Word) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sayWordGreater21(word.word)
                } else {
                    sayWordUnder20(word.word)
                }
            }
        })
    }

    override fun initObserver() {
        viewModel.listWords.observe(this, {
            wordsAdapter.setData(it)
            if (it.isNullOrEmpty()) {
                ivEmptyList.makeVisible()
                rvWords.makeGone()
            } else {
                ivEmptyList.makeGone()
                rvWords.makeVisible()
            }
        })
        viewModel.favoriteList.observe(this, {
            when (it) {
                is DataState.Success -> {
                    wordsAdapter.setData(it.data)
                    if (it.data.isNullOrEmpty()) {
                        ivEmptyList.makeVisible()
                        rvWords.makeGone()
                    } else {
                        ivEmptyList.makeGone()
                        rvWords.makeVisible()
                    }
                }
                else -> {
                }
            }
        })
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
        tts = TextToSpeech(context) { initStatus ->
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

    override fun getExtras() {}
}
