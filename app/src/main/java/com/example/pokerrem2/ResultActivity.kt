package com.example.pokerrem2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokerrem2.adapter.MyRecycleViewAdapter
import com.example.pokerrem2.adapter.ResRecycleViewAdapter
import com.example.pokerrem2.data.DataGlobal
import com.example.pokerrem2.data.Poker
import com.example.pokerrem2.data.ReciteRecord
import com.example.pokerrem2.utils.DBHandler

class ResultActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "ResultActivity"

    private var myRecycleViewAdapter: MyRecycleViewAdapter? = null

    private lateinit var tvRight: TextView
    private lateinit var tvWrong: TextView
    private lateinit var tvCorrectRate: TextView
    private lateinit var tvRememberTime: TextView
    private lateinit var tvDictationTime: TextView
    private lateinit var tvTotalTime: TextView
    private lateinit var dictationBundle: Bundle
    private lateinit var originalBundle: Bundle
    private val pokerNum = 54
    private var rightNum = 0
    private var wrongNum = pokerNum
    private var rightRate = 0.0f

    private var dictationpokers = mutableListOf<String>()
    private var originalpokers = mutableListOf<String>()
    private lateinit var rememberTime: String
    private lateinit var dictationTime: String

    // 最后的控制：再来一遍和保存记录
    private var saveItem = false
    private lateinit var btn_again: Button
    private lateinit var btn_saveItem: Button

    // 数据库
    private lateinit var dbHandler: DBHandler
    private var isSaved = false


    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        initView()
        initTwoPokers()
        comparePoker()
        val recyclerView = findViewById<RecyclerView>(R.id.rv_result)

        val gridLayoutManager = GridLayoutManager(this, 5)
        recyclerView.layoutManager = gridLayoutManager

        // 更新到标准类型判断
        val resRecycleViewAdapter = ResRecycleViewAdapter(DataGlobal.pokers,DataGlobal.classifiedDictionPokers,this)
        // val resRecycleViewAdapter = ResRecycleViewAdapter(originalpokers,dictationpokers,this)
        recyclerView.adapter = resRecycleViewAdapter

        Log.i("tag", "onCreate")

        showResult()
        showTime()

        // 初始化数据库
        dbHandler = DBHandler(this)

    }

    private fun saveRecord() {
        val reciteRecord = ReciteRecord(0, rightNum, rightRate, rememberTime, dictationTime)
        dbHandler.addReciteRecord(reciteRecord)
        Log.i(TAG, "DB add: ${reciteRecord.toString()}")
    }

    private fun showTime() {
        rememberTime = intent.getStringExtra("RememberTime")
        dictationTime = intent.getStringExtra("DictationTime")
        /**
        tvRememberTime.setText("记忆用时: ${rememberTime}")
        tvDictationTime.setText("回忆用时: ${dictationTime}")
        */
        tvRememberTime.setText("${rememberTime}")
        tvDictationTime.setText("${dictationTime}")
        Log.i(TAG, "回忆用时： $dictationTime")
        Log.i(TAG, "记忆用时: $rememberTime")
    }

    /**
     * 显示结果数据
     */
    @SuppressLint("SetTextI18n")
    private fun showResult() {
        tvRight.setText("${rightNum}")
        tvWrong.setText("${wrongNum}")
        tvCorrectRate.setText("${String.format("%.2f", rightRate)}%")
        /**
         *  国际化
        tvRight.setText("正确：${rightNum}")
        tvWrong.setText("错误：${wrongNum}")
        tvCorrectRate.setText("正确率: ${String.format("%.2f", rightRate)}%")
        */
    }

    private fun comparePoker() {
        for (i in 0..53) {
            if (dictationpokers[i] == originalpokers[i]) rightNum += 1
        }
        wrongNum = pokerNum - rightNum
        rightRate = rightNum.toFloat() / pokerNum.toFloat()
    }

    private fun initTwoPokers() {
        /**
        dictationBundle = intent.getBundleExtra("dictationBundle")
        originalBundle = intent.getBundleExtra("originalBundle")

        Log.i(TAG, dictationBundle.getString("0").toString())
        Log.i(TAG, originalBundle.getString("0").toString())

        for (i in 0..53) {
            dictationpokers.add(dictationBundle.getString("$i").toString())
            originalpokers.add(originalBundle.getString("$i").toString())
        }
        */

        // 改用全局数据
        dictationpokers = DataGlobal.classifiedDictionPokers
        originalpokers = DataGlobal.pokers

        Log.i(TAG, dictationpokers.toString())
        Log.i(TAG, originalpokers.toString())

    }

    private fun initView() {
        tvRight = findViewById(R.id.tv_right)
        tvWrong = findViewById(R.id.tv_wrong)
        tvCorrectRate = findViewById(R.id.tv_correctRate)
        tvRememberTime = findViewById(R.id.rememberTime)
        tvDictationTime = findViewById(R.id.dictationTime)
        tvTotalTime = findViewById(R.id.totalTime)

        // 最后的控制，再来一遍和保存记录
        btn_again = findViewById(R.id.again)
        btn_saveItem = findViewById(R.id.saveItem)

        btn_again.setOnClickListener(this)
        btn_saveItem.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        Log.i("tag", "onResume")

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.again -> {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.saveItem -> {
                if (!isSaved) {
                    saveRecord()
                    isSaved = true
                }
                Toast.makeText(applicationContext, "${applicationContext.getString(R.string.alreadySaveReciteRecord)}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun refresh() {
        finish()
        var intent = Intent(this, ResultActivity::class.java)
        startActivity(intent)

    }
}