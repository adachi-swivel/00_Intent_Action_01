
package com.example.a00_intent_action_01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
//import com.example.a00_intent_action_01.R
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Action Barの戻るボタン追加
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "入力画面に戻ります"

        //データを受け取って、表示する
        //MainActivityからデータを受け取る
        //getStringExtra()

        //companion objectで作成したEXTRA_TEXTDATAで指定して、データを取得する。
        //Intent に登録されているデータを取得できるので、引数にキー名を指定してあげればOK
        //intentTextView.text = intent.getStringExtra(MainActivity.EXTRA_TEXTDATA)

        //この書き方もOK

        val data2 = intent.getStringExtra(MainActivity.EXTRA_TEXTDATA2)
        intentTextView1.setText("$data2")


        val data1 = intent.getStringExtra(MainActivity.EXTRA_TEXTDATA1)
        intentTextView2.setText("")


        //うけとるデータをidで選別して、条件分岐させたい場合。
        //val id = intent.getIntExtra(MainActivity.EXTRA_TEXTDATA)

    }
    //戻るボタンのIDは android.R.id.home が割り当てられているの
    // で、そのときは Activity を終了する finish() を呼び出す
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}
