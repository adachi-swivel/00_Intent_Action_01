
// 00_Intent_Action_00 画面遷移
// (1) 5病後にアラームがなり３種類の文字列からランダムで１文字列を選択しその文字列を表示
//　　　「今が起きる時」「目覚めよ」、「おはよう」
// (2) 同時にBGM bgm_beatalarm.oggを鳴らす。
// (3) 入力画面に選択された文字列と同じ文字列を入力する
// (4) STOPボタンを押すと、正誤判定後、正しい場合は結果画面に遷移する。
// (5) 画面遷移後のActivityで受け取ったデータテキストが表示され、アラームが止まる。
//     se_alarm_stop.ogg
// 参考サイト
// https://www.usaco-pg.com/2017/05/01/kotlin-android-intent/
// https://damejinjiol.com/731.html

package com.example.a00_intent_action_01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
//import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.activity_result.*
import kotlin.random.Random
import android.media.AudioAttributes; //soundpool
import android.media.SoundPool //soundpool
import android.media.MediaPlayer



class MainActivity : AppCompatActivity() {

    //Kotlinではstatic(定数)は書けないので、companion objectで書く。これはのちにIntentでデータを渡す際に使用する
    //キー名の"EXTRA_TEXTDATA"をEXTRA_TEXTDATAに代入することで、リザルト画面のMainActivity.EXTRADATAがと書ける。
    companion object {
        const val EXTRA_TEXTDATA1 = "EXTRA_TEXTDATA1"
        const val EXTRA_TEXTDATA2 = "EXTRA_TEXTDATA2"
    }

    //時間遅延メソッドをインスタンス化
    private val mHandler = Handler()

    // mediaplayerクラスのインスタンス保持のためのプロパティ
    private lateinit var player: MediaPlayer

    // soundPoolのインスタンス化
    // リソースを app/res/raw内にコピー
    private lateinit var soundPool: SoundPool
    private var se_alarm_stop = 0
    private var se_false = 1


    override fun onCreate(savedInstanceState: Bundle?) {

        //初期化の定型文
        //Activityクラスの中でsetContentViewメソッドは3種類定義されています。
        //setContentView(int layoutResID)
        //setContentView(View view)
        //setContentView(View view, ViewGroup.LayoutParams params)
        //2番目と3番目のメソッドでは、引数にアクティビティに配置するViewクラスのオブジェクトを指定している。
        //それに対して1番目のメソッドでは別途XMLファイルで作成したレイアウトを表すIDを指定している。
        //setContentViewメソッドの引数に指定されている「R.layout.activity_main」がこのIDにあたる。

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // 効果音の登録
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()

        soundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            // ストリーム数に応じて
            .setMaxStreams(2)
            .build()

        // seをロードしておく
        se_alarm_stop = soundPool.load(this, R.raw.se_alarm_stop, 1)
        se_false = soundPool.load(this, R.raw.se_false, 1)


        // 遅延 2000 2秒
        Handler().postDelayed(Runnable {

            // bgmをならす

            player = MediaPlayer.create(this,R.raw.bgm_beatalarm)
            player.start();
            player.isLooping =true

            // ３種類のテキストをランダムに表示させる。
            val t = Random.nextInt(3)

            if (t == 0){
                timerText.setText("今が起きる時")

            }else if (t == 1){
                timerText.setText("目覚めよ")
            }else {
                timerText.setText("おはよう")
            }



        }, 2000)






        // ボタンを作成
        // val button2 = Button(this)
        // button2.setText("実行")
        // layout.addView(button2)
        // ボタンをタップした時の処理

        button2.setOnClickListener {

            // 現在の時間のミリ秒を System.currentTimeMillis で取得して、それに5000ミリ秒(5秒)を足す
            //val alarmTime = System.currentTimeMillis() + 5000
            //timerText.text = "$alarmTime"
            /*
            // 実行したいクラスから Intent を作成
            val alarmIntent = Intent(this, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            */
            /*
            // AlarmManager で pendingIntent を指定時間後に実行するように設定
            //val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            //manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
            //
            */

        }



        ///////////////////////////////////////////////

        button.setOnClickListener {

            //str1変数にtimerTextの文字のString型変換をした後代入
            // str2変数にeditText2に書かれたテキスト型変換した後代入
            val str1:String = timerText.text.toString()
            val str2:String = editText2.text.toString()

            //str1とstr2が同じかいなか
            if(str1.equals(str2)){
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra(EXTRA_TEXTDATA1, editText2.text.toString())
                intent.putExtra(EXTRA_TEXTDATA2, timerText.text.toString())

                startActivity(intent)
                player.stop()

                // ボタンが押されるたびにse_button_01.ogg の再生
                // play(ロードしたID, 左音量, 右音量, 優先度, ループ, 再生速度)
                soundPool.play(se_alarm_stop, 1.0f, 1.0f, 0, 0, 1.0f)

            } else if(editText2.text.toString() == ""){
                editText2.error = "何か書いて〜"
                soundPool.play(se_false, 1.0f, 1.0f, 0, 0, 1.0f)
                //editText2.text = "間違い"
            } else {
                editText2.error = "だめ〜"
                soundPool.play(se_false, 1.0f, 1.0f, 0, 0, 1.0f)
            }

        }
    }

        //外部に関数を持ってくる方法
        //button.setOnClickListener { onTapped() }
/*
    fun onTapped() {

            //①画面遷移の登録
            val intent = Intent(this, ResultActivity::class.java)
            //②データを渡す　順番が大事！！
            //渡したいデータを Intent に登録 putExtra()
            //第一引数はキー名、第二引数に渡したいデータを設定
            //EditTextもTextView などと同じようにレイアウトファイルの EditText の id で設定
            //入力された文字を取り出す：editText.text.toString()

            intent.putExtra(EXTRA_TEXTDATA1, editText2.text.toString())
            startActivity(intent)

        }
*/
}
