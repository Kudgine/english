package com.deadely.itgenglish.ui.mapsfragments

import android.content.Intent
import androidx.fragment.app.viewModels
import com.deadely.itgenglish.R
import com.deadely.itgenglish.base.BaseFragment
import com.deadely.itgenglish.extensions.makeGone
import com.deadely.itgenglish.extensions.makeVisible
import com.deadely.itgenglish.extensions.snack
import com.deadely.itgenglish.ui.firstscrene.ConditionActivity
import com.deadely.itgenglish.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_city_map.*

@AndroidEntryPoint
class MapCityFragment : BaseFragment(R.layout.fragment_city_map) {
    private val viewModel: MapCityViewModel by viewModels()
    var hasArtefakt = false

    override fun initView() {
        if (hasArtefakt) {
            pasta.makeVisible()
        } else {
            pasta.makeGone()
        }
    }

    override fun setListeners() {
        pointOne.setOnClickListener {
            openConditionScreen(
                R.drawable.shop_condition,
                getString(R.string.dialog_city_shop_condition_start),
                false
            )
        }
        pointTwo.setOnClickListener {
            if (!hasArtefakt) {
                snack(imageView, getString(R.string.dialog_access_denied))
            } else {
                //openConditionScreen()
            }
        }
    }

    private fun openConditionScreen(image: Int, message: String, isFinal: Boolean) {
        val intent = Intent(activity, ConditionActivity::class.java).apply {
            putExtra(IMAGE, image)
            putExtra(MESSAGE, message)
            putExtra(IS_FINAL, isFinal)
            putExtra(SCENE_NUMBER, 0)
        }
        startActivityForResult(intent, OPEN_CONDITION_SCREEN)
    }

    override fun initObserver() {
        viewModel.isHasArtefakt.observe(this, {
            hasArtefakt = it
            initView()
        })
    }

    override fun getExtras() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            OPEN_CONDITION_SCREEN -> {
                viewModel.update()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}