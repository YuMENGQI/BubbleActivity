package com.android.byc.bubbleactivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val text = listOf("取消置顶", "置顶", "删除")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        menu_layout.apply {
            textList = text
            onClickListener = object: MenuLayout.OnClickListener{
                override fun onClick(index: Int) {
                    debug_text.text = text[index]
                }

            }

        }
    }
}
