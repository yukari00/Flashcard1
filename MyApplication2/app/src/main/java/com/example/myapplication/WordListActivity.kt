package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_word_list.*

class WordListActivity : AppCompatActivity(), AdapterView.OnItemClickListener,
    AdapterView.OnItemLongClickListener {

    lateinit var realm: Realm
    lateinit var results: RealmResults<WordDB>
    lateinit var word_list: ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)

        constraintLayoutWordList.setBackgroundResource(intBackGroundColor)

        buttonAddNewVoc.setOnClickListener {
            val intent = Intent(this@WordListActivity, EditActivity::class.java)
            intent.putExtra(getString(R.string.intent_key_status), getString(R.string.status_add))
            startActivity(intent)
        }
        buttonBack.setOnClickListener { finish() }
        ListView.onItemClickListener = this
        ListView.onItemLongClickListener = this

        buttonSort.setOnClickListener {
            results = realm.where(WordDB::class.java).findAll()
                .sort(getString(R.string.db_field_bool_memory_Frag))
            word_list.clear()
            val length = results.size
            results.forEach{
                if(it.boolMemoryFrag){
                    word_list.add(it.strAnswer + " : " + it.strQuestion + " 【暗記済】")
                }else{
                    word_list.add(it.strAnswer + " : " + it.strQuestion)
                }
            }
            ListView.adapter = adapter

        }
    }

    override fun onResume() {
        super.onResume()

        realm = Realm.getDefaultInstance()

        results = realm.where(WordDB::class.java).findAll().sort(getString(R.string.db_field_Question))

        word_list = ArrayList<String>()

      //  val length = results.size
      //  for (i in 0..length - 1) {
      //  if(results[i]!!.boolMemoryFrag){
      //          word_list.add(results[i]!!.strAnswer + " : " + results[i]!!.strQuestion + " 【暗記済】")
      //      }else{
      //          word_list.add(results[i]!!.strAnswer + " : " + results[i]!!.strQuestion)
      //      }
      //  }

        results.forEach{
            if(it.boolMemoryFrag){
                word_list.add(it.strAnswer + " : " + it.strQuestion + " 【暗記済】")
            }else{
                word_list.add(it.strAnswer + " : " + it.strQuestion)
            }
        }

        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, word_list)
        ListView.adapter = adapter

    }

    override fun onPause() {
        super.onPause()
        realm.close()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //編集
        val selectedDB = results[position]!!
        val strSelectedQuestion = selectedDB.strQuestion
        val strSelectedAnswer = selectedDB.strAnswer
        val intent = Intent(this@WordListActivity, EditActivity::class.java)
        intent.putExtra(getString(R.string.intent_key_position), position)
        intent.putExtra(getString(R.string.intent_key_question), strSelectedQuestion)
        intent.putExtra(getString(R.string.intent_key_answer), strSelectedAnswer)
        intent.putExtra(getString(R.string.intent_key_status), getString(R.string.status_change))
        startActivity(intent)
    }

    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {

        val selectedDB = results[position]!!

        val dialog = AlertDialog.Builder(this@WordListActivity).apply {
            setTitle(selectedDB.strAnswer + "の削除")
            setMessage("削除してもいいですか？")
            setPositiveButton("はい"){dialog, which ->
                realm.beginTransaction()
                selectedDB.deleteFromRealm()
                realm.commitTransaction()
                word_list.removeAt(position)
                ListView.adapter = adapter }
            setNegativeButton("いいえ"){ dialog, which ->  }
            show()
        }
        return true
    }
}

