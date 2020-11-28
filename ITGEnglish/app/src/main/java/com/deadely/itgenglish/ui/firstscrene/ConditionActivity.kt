package com.deadely.itgenglish.ui.firstscrene

import android.annotation.SuppressLint
import android.app.Dialog
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import com.deadely.itgenglish.R
import com.deadely.itgenglish.base.BaseActivity
import com.deadely.itgenglish.ui.scenes.FragmentFirstCurortScene
import com.deadely.itgenglish.ui.scenes.FragmentFirstShopScene
import com.deadely.itgenglish.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.scene_shop.*

@AndroidEntryPoint
class ConditionActivity : BaseActivity(R.layout.scene_shop) {

    private val viewModel: ConditionViewModel by viewModels()

    companion object {
        const val SHOP = 0
        const val OBSERVATORY = 1

    }

    var isFinal = false
    var message = ""
    var image = 0
    var sceneNumber = 0

    @SuppressLint("SetTextI18n")
    override fun initView() {
        supportActionBar?.hide()
        tvDialog.text = message
        imageView.setImageResource(image)
        if (isFinal) {
            when (sceneNumber) {
                SHOP -> {
                    tvDialog.text = getString(R.string.dialog_city_shop_condition_finish)
                }
                OBSERVATORY -> {
                    tvDialog.text = getString(R.string.dialog_city_observatory_condition_finish)
                }
            }
            ivArrow.setImageResource(R.drawable.ic_baseline_arrow_back_ios_24)
        } else {
            ivArrow.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24)
        }
    }

    override fun setListeners() {
        ivArrowArea.setOnClickListener {
            openScene()
        }
        ivArrow.setOnClickListener {
            openScene()
        }
    }

    private fun openScene() {
        if (isFinal) {
            when (sceneNumber) {
                SHOP -> {
                    val dialog = Dialog(this)
                    dialog.apply {
                        setContentView(R.layout.next_artefakt_dialog)
                        findViewById<TextView>(R.id.btnOk).setOnClickListener {
                            dismiss()
                        }
                        findViewById<ImageView>(R.id.ivArtefakt).setImageResource(R.drawable.pasta)
                        setOnDismissListener {
                            viewModel.setArtefakt()
                            setResult(FINISH)
                            finish()
                        }
                    }
                    dialog.show()
                }
                OBSERVATORY -> {
//                    val dialog = Dialog(this)
//                    dialog.apply {
//                        setContentView(R.layout.next_artefakt_dialog)
//                        findViewById<TextView>(R.id.btnOk).setOnClickListener {
//                            dismiss()
//                            finish()
//                        }
//                        findViewById<ImageView>(R.id.ivArtefakt).setImageResource(R.drawable.star)
//                        setOnDismissListener {
//                            finish()
//                        }
//                    }
//                    dialog.show()
//                    setResult(FINISH)
//                    finish()
                }
            }
        } else {
            when (sceneNumber) {
                SHOP -> {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.clContainer, FragmentFirstShopScene()).commitAllowingStateLoss()
                }
                OBSERVATORY -> {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.clContainer, FragmentFirstCurortScene()).commitAllowingStateLoss()
                }
            }
        }
    }

    override fun initObserver() {

    }

    override fun getExtras() {
        intent?.extras?.let {
            isFinal = it.getBoolean(IS_FINAL, false)
            message = it.getString(MESSAGE, "")
            sceneNumber = it.getInt(SCENE_NUMBER, 0)
            image = it.getInt(IMAGE, 0)
        }

    }

}