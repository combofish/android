package com.example.pokerrem2

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.*


class DictationActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "DictationActivity"
    private var changedGroup = false
    private lateinit var tv: TextView
    private lateinit var selectKing: RadioGroup
    private lateinit var selectColor: RadioGroup
    private lateinit var selectNumber1: RadioGroup
    private lateinit var selectNumber2: RadioGroup
    private lateinit var selectNumber3: RadioGroup
    private lateinit var upPage: Button
    private lateinit var nextPage: Button
    private lateinit var endDictation: Button
    private lateinit var tv2: TextView
    private var blankRadioButton = mutableListOf<RadioButton>()

    // 记忆用时
    private lateinit var rememberTime: String
    private lateinit var rememberTimeView: TextView

    // 回忆用时
    private lateinit var chronometerDictation: Chronometer
    private lateinit var dictationTime: String

    var kingStr: String = ""
    var colorStr: String = ""
    var numberStr: String = ""

    private val NUM_PAGES = 54
    private var currentPager = 1
    var dictationPokers = mutableListOf<String>()
    private lateinit var originalBundle: Bundle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictation)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        initView()
        registerListener()
        initBlockPokers()
        dictation(currentPager)
        initOriginalPokers()
        showRememberTime()
        initDictationTimer()
    }

    private fun initDictationTimer() {
        chronometerDictation = findViewById(R.id.chronometer_dictation)
        chronometerDictation.base = SystemClock.elapsedRealtime()
        chronometerDictation.format = "${applicationContext.getString(R.string.recallTime)}%s"
        chronometerDictation.start()
    }

    private fun showRememberTime() {
        rememberTime = intent.getStringExtra("RememberTime")
        Log.i(TAG, "rememberTime: $rememberTime")

        rememberTimeView = findViewById(R.id.tv_remember_time)
        val string = applicationContext.getString(R.string.rememberTime)
        // rememberTimeView.setText("记忆用时: $rememberTime")
        rememberTimeView.setText("${string}$rememberTime")
    }

    private fun initOriginalPokers() {
        originalBundle = intent.getBundleExtra("pokerBundle")
        Log.i(TAG, originalBundle.toString())
        Log.i(TAG, originalBundle.get("0").toString())
    }


    private fun dictation(i: Int) {
        clearAllRadioGroup()
        currentPager = i

        var pageAdd = false
        kingStr = ""
        numberStr = ""
        colorStr = ""
        //tv.setText("第${i}
        tv.setText("$i")
        if (!dictationPokers[i - 1].isBlank()) tv2.setText(dictationPokers[i - 1])
        else tv2.setText("${applicationContext.getString(R.string.pleaseSelect)}")

        if (currentPager == 54) endDictation.setText("${applicationContext.getString(R.string.endRecite)}")
        else endDictation.setText("${applicationContext.getString(R.string.pauseRecite)}")

    }

    private fun updateDictationPoker(str: String) {
        dictationPokers[currentPager - 1] = str
        if (str in listOf("大王", "小王")) {
            numberStr = ""
            colorStr = ""
        } else {
            kingStr = ""
        }
        updateTextView()
    }

    private fun updateTextView() {
        tv2.setText(dictationPokers[currentPager - 1])
    }

    inner class MyRadioGroupCheckedChangedListener() : RadioGroup.OnCheckedChangeListener {

        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
            Log.i("RadioButton checked", "select: $checkedId")
            if (!(checkedId === null)) {
                if (!changedGroup) {
                    changedGroup = true
                    when (group) {
                        selectKing -> {
                            kingStr = findViewById<RadioButton>(checkedId).text as String
                            updateDictationPoker(kingStr)
                            selectKingClearOtherRadioGroup()
                        }
                        selectColor -> {
                            colorStr = findViewById<RadioButton>(checkedId).text as String
                            updateColor()
                        }
                        selectNumber1 -> {
                            numberStr = findViewById<RadioButton>(checkedId).text as String
                            updateColor()
                            selectNumberClearOtherNumberRadioGroup(1)
                        }
                        selectNumber2 -> {
                            numberStr = findViewById<RadioButton>(checkedId).text as String
                            updateColor()
                            selectNumberClearOtherNumberRadioGroup(2)

                        }
                        selectNumber3 -> {
                            numberStr = findViewById<RadioButton>(checkedId).text as String
                            updateColor()
                            selectNumberClearOtherNumberRadioGroup(3)
                        }
                    }
                    changedGroup = false

                }
            }
        }
    }

    /**
     * 选择花色后续，判断是否存储
     */
    private fun updateColor() {
        selectColorClearOtherRadioGroup()
        if (!colorStr.isBlank() && !numberStr.isBlank()) {
            updateDictationPoker("${colorStr}${numberStr}")
        }
    }

    private fun selectNumberClearOtherNumberRadioGroup(i: Int) {
        when (i) {
            1 -> {
                blankRadioButton[3].isChecked = true
                blankRadioButton[4].isChecked = true
            }
            2 -> {
                blankRadioButton[2].isChecked = true
                blankRadioButton[4].isChecked = true
            }
            3 -> {
                blankRadioButton[2].isChecked = true
                blankRadioButton[3].isChecked = true
            }
        }
    }

    private fun registerListener() {
        selectKing.setOnCheckedChangeListener(MyRadioGroupCheckedChangedListener())
        selectColor.setOnCheckedChangeListener(MyRadioGroupCheckedChangedListener())
        selectNumber1.setOnCheckedChangeListener(MyRadioGroupCheckedChangedListener())
        selectNumber2.setOnCheckedChangeListener(MyRadioGroupCheckedChangedListener())
        selectNumber3.setOnCheckedChangeListener(MyRadioGroupCheckedChangedListener())

        upPage.setOnClickListener(this)
        nextPage.setOnClickListener(this)
        endDictation.setOnClickListener(this)
    }

    private fun initView() {
        tv = findViewById(R.id.tv1)
        selectKing = findViewById(R.id.select_king)
        selectColor = findViewById(R.id.select_color)
        selectNumber1 = findViewById(R.id.select_number1)
        selectNumber2 = findViewById(R.id.select_number2)
        selectNumber3 = findViewById(R.id.select_number3)

        upPage = findViewById(R.id.upPage)
        nextPage = findViewById(R.id.nextPage)
        endDictation = findViewById(R.id.submit)
        tv2 = findViewById(R.id.currentPoker)

        blankRadioButton.add(findViewById(R.id.blankInKingGroup))
        blankRadioButton.add(findViewById(R.id.blankInColorGroup))
        blankRadioButton.add(findViewById(R.id.blankInNumberGroup1))
        blankRadioButton.add(findViewById(R.id.blankInNumberGroup2))
        blankRadioButton.add(findViewById(R.id.blankInNumberGroup3))

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.upPage -> {
                if (currentPager == 1) dictation(NUM_PAGES)
                else dictation(currentPager - 1)
            }
            R.id.nextPage -> {
                if (currentPager == 54) dictation(1)
                else dictation(currentPager + 1)
            }
            R.id.submit -> {
                goToResultPage()
                // Toast.makeText(applicationContext, "正在提交", Toast.LENGTH_SHORT)
                //    .show()
            }
        }
    }

    /**
     * 跳转去结果页面
     */
    private fun goToResultPage() {
        var intent = Intent(this, ResultActivity::class.java)
        var dictationBundle = Bundle()
        for (i in 0..53) {
            dictationBundle.putString("$i", dictationPokers[i])
        }
        intent.putExtra("dictationBundle", dictationBundle)
        intent.putExtra("originalBundle", originalBundle)

        // 停掉回忆计时器
        //chronometerDictation.format = "%s"
        chronometerDictation.stop()
        val toStr = chronometerDictation.text.toString()
        dictationTime = toStr.substring(toStr.length - 5 )
        intent.putExtra("DictationTime", dictationTime)
        intent.putExtra("RememberTime", rememberTime)

        Log.i(TAG, "回忆用时： $dictationTime")
        Log.i(TAG, "记忆用时: $rememberTime")
        //bundle.putSerializable("dictationPokers", this.dictationPokers)
        //intent.putParcelableArrayListExtra("dictationPokers",dictationPokers.toTypedArray()cncn)
        startActivity(intent)
    }

    private fun clearAllRadioGroup() {
        for (rb in blankRadioButton) {
            rb.isChecked = true
        }
        Log.i("clear", "clearAllRadioGroup")
        /**
        selectKing.clearCheck()
        selectColor.clearCheck()
        selectNumber1.clearCheck()
        selectNumber2.clearCheck()
        selectNumber3.clearCheck()
         */
    }

    private fun selectKingClearOtherRadioGroup() {
        Log.i("clear", "selectKingClearOtherRadioGroup")
        for (i in 1..4) {
            blankRadioButton[i].isChecked = true
        }
    }

    private fun selectColorClearOtherRadioGroup() {
        Log.i("clear", "selectColorClearOtherRadioGroup")
        blankRadioButton[0].isChecked = true
    }

    /**
     * 初始化空的扑克牌字符串序列
     */
    private fun initBlockPokers() {
        for (i in 1..NUM_PAGES) {
            Log.i("init", "$i") // 1-54
            dictationPokers.add("")
        }
        Log.i("init", "dictationPokers.size: ${dictationPokers.size}")
    }
}