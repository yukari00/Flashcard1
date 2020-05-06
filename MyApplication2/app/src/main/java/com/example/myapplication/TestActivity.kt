package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.activity_word_list.*
import java.util.*
import kotlin.collections.ArrayList

class TestActivity : AppCompatActivity(), View.OnClickListener {

    var boolMemoryFrag = false
    var intState = 0
    val BEFORE_START = 1
    val RUNNING_QUESTION = 2
    val RUNNING_ANSWER = 3
    val TEST_FINISHED = 4
    lateinit var realm : Realm
    lateinit var results : RealmResults<WordDB>
    lateinit var word_list : ArrayList<WordDB>
    var intLength = 0
    var intCount = 0
    var boolMemorized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        constraintLayoutTest.setBackgroundResource(intBackGroundColor)

        //Todo 画面が開いたとき
        //  Todo 1.MainActivityからのIntent(テスト条件)受け取り
        val bundle = intent.extras!!
        boolMemoryFrag =bundle.getBoolean(getString(R.string.intent_key_memory_status))

        // 3.テスト状態を「開始前」に+カード画像非表示
        intState = BEFORE_START
        imageViewFlashQuestion.visibility = View.INVISIBLE
        imageViewFlashAnswer.visibility = View.INVISIBLE

        //4.ボタン①を「テストをはじめる」に
        buttonNext.setBackgroundResource(R.drawable.image_button_test_start)

        //5.ボタン②を「かくにんテストをやめる」に
        buttonEndTest.setBackgroundResource(R.drawable.image_button_end_test)


        //ボタン①（上のボタン）を押したとき
        buttonNext.setOnClickListener(this)
        //ボタン②（下のボタン）を押したとき：確認ダイアログ
        buttonEndTest.setOnClickListener(this)
        //checkBox
        checkBox.setOnClickListener{
            boolMemorized = checkBox.isChecked
        }

    }

    override fun onResume() {
        super.onResume()
        //  Todo 5.DBからテストデータ取得(テスト条件で処理分岐)
        realm = Realm.getDefaultInstance()
        if (boolMemoryFrag){
            results = realm.where(WordDB::class.java).equalTo(getString(R.string.db_field_bool_memory_Frag), false).findAll()
        }else{
            results = realm.where(WordDB::class.java).findAll()
        }
        //  Todo 6. 5で取得したテストデータをシャッフル
        word_list = ArrayList(results)
        Collections.shuffle(word_list)

        intLength = results.size
    textViewRemaining.text = intLength.toString()
}

    override fun onPause() {
        super.onPause()
        realm.close()
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.buttonNext ->
                when(intState){
                    BEFORE_START ->{
                        intState = RUNNING_QUESTION
                        showQuestion()
                    }
                    RUNNING_QUESTION ->{
                        intState = RUNNING_ANSWER
                        showAnswer()
                    }
                    RUNNING_ANSWER ->{
                        intState = RUNNING_QUESTION
                        showQuestion()
                    }

                }
            R.id.buttonEndTest ->{
           //ボタン②（下のボタン）を押したとき：確認ダイアログ
                    //      1. テスト画面を閉じてMainActivityに戻る
                    val dialog = AlertDialog.Builder(this@TestActivity).apply { 
                        setTitle("終了")
                        setMessage("終了しますか？")
                        setPositiveButton("はい"){dialog, which ->
                            finish()
                        }
                        setNegativeButton("いいえ"){dialog, which ->  }
                        show()
                    }
                     if(intState==TEST_FINISHED) {
                //      ＝＞テスト状態が「テスト終了」の場合
                //        //             => 最後の問題の暗記済フラグをDBに登録(更新)
                      val selectedDB = realm.where(WordDB::class.java).equalTo(
                      getString(R.string.db_field_Question),
                      word_list[intCount - 1].strQuestion
                      ).findFirst()
                      realm.beginTransaction()
                      selectedDB!!.boolMemoryFrag = boolMemorized
                      realm.commitTransaction()
                      }

                      }


        }


    }


    private fun showQuestion() {
        //   Todo 1. 前の問題の暗記済フラグをDB登録（更新）
        if(intCount > 0){
            val selectedDB = realm.where(WordDB::class.java).equalTo(getString(R.string.db_field_Question), word_list[intCount - 1].strQuestion).findFirst()
            realm.beginTransaction()
            selectedDB!!.boolMemoryFrag = boolMemorized
            realm.commitTransaction()
        }
        //   Todo 2. のこり問題数を１つ減らして表示
        intCount ++
        textViewRemaining.text = (intLength - intCount).toString()

        //   Todo 3. 今回の問題表示・前の問題消去（画像と文字）
        imageViewFlashQuestion.visibility = View.VISIBLE
        textViewFlashQuestion.text = word_list[intCount-1].strQuestion
        imageViewFlashAnswer.visibility = View.INVISIBLE
        textViewFlashAnswer.text = ""
        //   Todo 4. ボタン①を「こたえを見る」ボタンに
        buttonNext.setBackgroundResource(R.drawable.image_button_go_answer)
        //   Todo 5. 問題の単語が暗記済の場合はチェックを入れる
        checkBox.isChecked = word_list[intCount -1].boolMemoryFrag
        boolMemorized = checkBox.isChecked

    }
    private fun showAnswer() {
        //   Todo 1. こたえの表示（画像・文字）
        imageViewFlashAnswer.visibility = View.VISIBLE
        textViewFlashAnswer.text = word_list[intCount - 1].strAnswer
        //   Todo 2. ボタン①を「次の問題にすすむ」に
        buttonNext.setBackgroundResource(R.drawable.image_button_go_next_question)
        //   Todo 3. 最後の問題まで来たら
        if(intLength == intCount){
            textViewTestDone.text = "テスト終了"
            buttonNext.visibility = View.INVISIBLE
            buttonNext.isEnabled = false
            buttonEndTest.setBackgroundResource(R.drawable.image_button_back)
            intState = TEST_FINISHED
        }
        //         => Todo 1. テスト状態を「終了」にしてメッセージ表示
        //         => Todo 2. ボタン①を見えない＆使えないように
        //         => Todo 3. ボタン②を「もどる」に
    }


}
