package com.example.calculationtraining

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*
import kotlin.concurrent.schedule


class TestActivity : AppCompatActivity(), View.OnClickListener {

    var numberOfRemaining = 0
    var numberOfCorrect = 0
    var numberOfPoint = 0
    var numberOfQuestion = 0
    lateinit var soundPool : SoundPool
    var intSoundId_Correct = 0
    var intSoundId_Incorrect = 0
    lateinit var timer : Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val bundle = intent.extras
        numberOfQuestion = bundle!!.getInt("numberOfQuestions")
        textViewRemaining.text = numberOfQuestion.toString()
        numberOfRemaining = numberOfQuestion
        //Todo2　問題が出されたら
        question()

        //Todo3　電卓ボタンが押されたら
        button0.setOnClickListener(this)
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        buttonMinus.setOnClickListener(this)
        buttonC.setOnClickListener(this)
        //Todo4　「こたえあわせ」ボタンが押されたら
        buttonAnswer.setOnClickListener {
            //Todo5　こたえあわせ処理
            answerCheck()
            }
        //Todo6　「もどる」ボタンが押されたら
        buttonReturn.setOnClickListener {  }


    }

    override fun onResume() {
        super.onResume()
        //効果音出すためのクラス：SoundPoolを準備する（インスタンス化）
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    SoundPool.Builder().setAudioAttributes(
                        AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
                .setMaxStreams(1)
                .build()
        } else {
            SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        }

            intSoundId_Correct = soundPool.load(this, R.raw.sound_correct, 1)
            intSoundId_Incorrect = soundPool.load(this, R.raw.sound_incorrect, 1)

        timer = Timer()
        }

    override fun onPause() {
        super.onPause()
        soundPool.release()
        timer.cancel()
    }

    private fun answerCheck() {
        //戻る、答えあわせ、電卓ボタンをおせなくする
        buttonReturn.isEnabled = false
        buttonAnswer.isEnabled = false
        button0.isEnabled = false
        button1.isEnabled = false
        button2.isEnabled = false
        button3.isEnabled = false
        button4.isEnabled = false
        button5.isEnabled = false
        button6.isEnabled = false
        button7.isEnabled = false
        button8.isEnabled = false
        button9.isEnabled = false
        buttonMinus.isEnabled = false
        buttonC.isEnabled = false



        //のこりの問題数を1つ減らして表示
        numberOfRemaining --
        textViewRemaining.text = numberOfRemaining.toString()
        //〇か✖を表示
        imageView2.visibility = View.VISIBLE
        //自分の入力した答えと本当の答えを比較する
        val myAnswer = textViewAnswer.text.toString().toInt()
        val realAnswer = if (textViewOperator.text == "+") {
            textViewLeft.toString().toInt() + textViewRight.toString().toInt()
        }else{
            textViewLeft.toString().toInt() - textViewRight.toString().toInt()}
        if (myAnswer == realAnswer){
            //合っている場合、正解数を増やし音声をつける
            numberOfCorrect++
            textViewCorrect.text = numberOfCorrect.toString()
            imageView2.setImageResource(R.drawable.pic_correct)
            soundPool.play(intSoundId_Correct, 1.0f, 1.0f, 0,0, 1.0f)
        }else{
            //間違っている場合、音声をつける
            imageView2.setImageResource(R.drawable.pic_incorrect)
            soundPool.play(intSoundId_Incorrect, 1.0f, 1.0f, 0,0,1.0f)
        }

        //正答率を計算して表示
        numberOfPoint = ((numberOfCorrect.toDouble()/(numberOfQuestion - numberOfRemaining).toDouble())*100).toInt()
        textViewPoint.text = numberOfPoint.toString()

        //テストが終わった場合　→　戻りボタン表示、こたえあわせボタン非表示、「テスト終了」表示
        if(numberOfRemaining == 0){
            buttonReturn.isEnabled = true
            textViewMessage.text = "テスト終了"

        }else{
            //のこり問題数がある場合　→　1秒後に次の問題を出す（questionメソッド）
            timer.schedule(1000, {runOnUiThread { question()}})
        }

        //正解なら〇、不正解なら✖のテキストビュー

    }

    private fun question() {
        //戻るボタンを使えなくする
        buttonReturn.isEnabled = false
        //こたえあわせボタンと電卓ボタンをつかえるようにする
        buttonAnswer.isEnabled = true
        button0.isEnabled = true
        button1.isEnabled = true
        button2.isEnabled = true
        button3.isEnabled = true
        button4.isEnabled = true
        button5.isEnabled = true
        button6.isEnabled = true
        button7.isEnabled = true
        button8.isEnabled = true
        button9.isEnabled = true
        buttonMinus.isEnabled = true
        buttonC.isEnabled = true

        //問題の2つの数字を1~100からランダムに設定し表示
        val random = java.util.Random()
        val randomNumberLeft = random.nextInt(100)+1
        val randomNumberRight = random.nextInt(100)+1
        textViewLeft.text = randomNumberLeft.toString()
        textViewRight.text = randomNumberRight.toString()



        //計算方法を+か-どちらかに設定し表示

        val randomTwoNumber = random.nextInt(2)+1
        if (randomTwoNumber == 1){
            textViewOperator.text = "+"
        }else{
            textViewOperator.text = "-"
        }


        //前の問題で入力した自分の答え消す
        textViewAnswer.text = ""
        //〇、✖画像を見えないようにする
        imageView2.visibility = View.INVISIBLE

    }

    //ボタンが押されたときの処理
    override fun onClick(v: View?) {

        val button : Button = v as Button
        when(v?.id){
            R.id.buttonC
            -> textViewAnswer.text = ""
            R.id.buttonMinus
            -> if (textViewAnswer.text.toString() == "")
                textViewAnswer.text = "-"
            //それ以外の場合なにもしないからなにも書かない
            R.id.button0
            -> if (textViewAnswer.text.toString() != "0" && textViewAnswer.text.toString() != "-" )
                textViewAnswer.append(button.text)

            else
                -> if(textViewAnswer.text.toString() == "0")
                textViewAnswer.text = button.text
                else
                textViewAnswer.append(button.text)

        }


    }





}


