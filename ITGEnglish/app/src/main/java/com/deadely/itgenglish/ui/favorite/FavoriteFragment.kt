package com.deadely.itgenglish.ui.favorite

import android.os.Build
import android.speech.tts.TextToSpeech
import android.view.View
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
import com.deadely.itgenglish.utils.CustomAdapter
import com.deadely.itgenglish.utils.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.row_word.view.*
import kotlinx.android.synthetic.main.view_search.*
import java.util.*
import kotlin.collections.HashMap

@AndroidEntryPoint
class FavoriteFragment : BaseFragment(R.layout.fragment_favorite) {

    private val viewModel: FavoriteViewModel by viewModels()

    private var ttsEnabled: Boolean = false
    private lateinit var tts: TextToSpeech

    private val wordsAdapter = CustomAdapter<Word>(R.layout.row_word) { viewHolder, word, _, _ ->
        viewHolder.itemView.tvWord.text = word.word
        viewHolder.itemView.tvDescription.text = word.translate
        viewHolder.itemView.tvTr.text = word.tr
        viewHolder.itemView.ivFavorite.setImageResource(R.drawable.ic_baseline_favorite_red)
        viewHolder.itemView.ivFavorite.setOnClickListener {
            word.favorite = !word.favorite
            viewModel.deleteFavorites(word)
            viewHolder.itemView.ivFavorite.setImageResource(R.drawable.ic_baseline_favorite_grey)
        }
        viewHolder.itemView.ivVolume.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sayWordGreater21(word.word)
            } else {
                sayWordUnder20(word.word)
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

    override fun initView() {
        initTts()
        setActivityTitle(R.string.favorite)
        rvWords.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = wordsAdapter
        }
    }

    override fun setListeners() {
        etSearch.addTextChangedListener {
            viewModel.onSearchTextChanged(it.toString())
            if (it.toString().isNotEmpty()) {
                ivClose.visibility = View.VISIBLE
            } else {
                ivClose.visibility = View.GONE
            }
        }
        ivClose.setOnClickListener { etSearch.text.clear() }
    }

    override fun initObserver() {
        viewModel.favoriteList.observe(viewLifecycleOwner, {
            when (it) {
                is DataState.Loading -> {
                }
                is DataState.Error -> {
                    it.exception.printStackTrace()
                }
                is DataState.Success -> {
                    wordsAdapter.setData(it.data)
                    checkList(it.data)
                }
            }
        })
        viewModel.filteredList.observe(viewLifecycleOwner, {
            wordsAdapter.setData(it)
            checkList(it)
        })
    }

    fun checkList(list: List<Word>) {
        if (list.isNullOrEmpty()) {
            ivEmptyList.makeVisible()
            rvWords.makeGone()
        } else {
            ivEmptyList.makeGone()
            rvWords.makeVisible()
        }
    }

    override fun getExtras() {

    }
}
