package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import io.realm.Realm
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {

    lateinit var realm : Realm
    var intPosition = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        constraintLayoutEdit.setBackgroundResource(intBackGroundColor)

        val bundle = intent.extras
        val strQuestion = bundle!!.getString(getString(R.string.intent_key_question))
        val strAnswer = bundle.getString(getString(R.string.intent_key_answer))
        val strStatus = bundle.getString(getString(R.string.intent_key_status))
        textViewStatus.text = strStatus
        
        intPosition = bundle.getInt(getString(R.string.intent_key_position))

        if(strStatus == getString(R.string.status_change)){
            editTextQuestion.setText(strQuestion)
            editTextQuestion.isEnabled = false
            editTextAnswer.setText(strAnswer)
            //修正の場合は問題が修正できないようにする
        }

        buttonSave.setOnClickListener {
            if(strStatus == getString(R.string.status_add)){
                addNewWord()
            }else{
                changeWord()
            }
        }
        buttonBack2.setOnClickListener {
            finish()
        }


    }

    override fun onResume() {
        super.onResume()

        realm = Realm.getDefaultInstance()
    }

    override fun onPause() {
        super.onPause()
        realm.close()

    }

    private fun addNewWord() {

        //   Todo 1.単語(主キー)の重複チェック
        //    => Todo 1-1.重複していない場合
        
        //   　　Todo 1-1-1.DB登録処理
        //	(主キー・暗記済フラグ設定に伴う変更)
        //    => Todo 1-2.重複している場合
        //　　　Todo 1-2-1.登録不可メッセージ(Toast)
            
        val dialog = AlertDialog.Builder(this@EditActivity).apply { 
            setTitle("登録")
            setMessage("登録してもいいですか？")
            setPositiveButton("はい") { dialog, which ->
                try {
                    realm.beginTransaction()
                    val wordDB =
                        realm.createObject(WordDB::class.java, editTextQuestion.text.toString())
                    wordDB.strAnswer = editTextAnswer.text.toString()
                    realm.commitTransaction()

                    Toast.makeText(this@EditActivity, "登録が完了しました", Toast.LENGTH_SHORT).show()

                } catch (e: RealmPrimaryKeyConstraintException) {
                    Toast.makeText(this@EditActivity, "この単語はすでに登録されてあります", Toast.LENGTH_SHORT).show()

                } finally {
                    editTextQuestion.setText("")
                    editTextAnswer.setText("")

                }
            }
                setNegativeButton("いいえ"){dialog, which ->  }
                show()
            
        }
        


    }
    private fun changeWord() {
        //   Todo 1.単語(主キー)の重複チェック
        //    => Todo 1-1.重複していない場合
        //   　　Todo 1-1-1.DB変更処理(主キー設定に伴う変更)
        //	(主キー・暗記済フラグ設定に伴う変更)
        //    => Todo 1-2.重複している場合
        //　　　Todo 1-2-1.登録不可メッセージ(Toast)
        val results = realm.where(WordDB::class.java).findAll().sort(getString(R.string.db_field_Question))
        val wordDB = results[intPosition]!!
        val dialog = AlertDialog.Builder(this@EditActivity).apply{
            setTitle("修正")
            setMessage("修正をしてもいいですか？")
            setPositiveButton("はい"){dialog, which ->
                realm.beginTransaction()
                //wordDB!!.strQuestion = editTextQuestion.text.toString()
                wordDB.strAnswer = editTextAnswer.text.toString()
                wordDB.boolMemoryFrag = false
                realm.commitTransaction()

                Toast.makeText(this@EditActivity, "修正が完了しました", Toast.LENGTH_SHORT).show()
                editTextQuestion.setText("")
                editTextAnswer.setText("")
                finish()
            }
            setNegativeButton("いいえ"){dialog, which ->  }
            show()
            
        }
        



    }
}
