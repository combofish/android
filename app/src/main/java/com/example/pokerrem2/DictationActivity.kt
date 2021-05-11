package com.example.pokerrem2

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.*
import com.example.pokerrem2.data.DataGlobal


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
        // Log.i(TAG, originalBundle.get("0").toString())
    }


    private fun dictation(i: Int) {
        clearAllRadioGroup()
        currentPager = i


        kingStr = ""
        numberStr = ""
        colorStr = ""

        tv.setText("$i")
        if (dictationPokers[i - 1].isNotBlank()) tv2.setText(dictationPokers[i - 1])
        else tv2.setText("${applicationContext.getString(R.string.pleaseSelect)}")

        if (currentPager == 54) endDictation.setText("${applicationContext.getString(R.string.endRecite)}")
        else endDictation.setText("${applicationContext.getString(R.string.pauseRecite)}")

    }

    // 尽量做到与外界无关
    private fun updateLocalDictationPoker(str: String, i: Int) {

    }

    private fun endStr(str: String): String {
        return when {
            str.endsWith("A") -> "A"
            str.endsWith("2") -> "2"
            str.endsWith("3") -> "3"
            str.endsWith("4") -> "4"
            str.endsWith("5") -> "5"
            str.endsWith("6") -> "6"
            str.endsWith("7") -> "7"
            str.endsWith("8") -> "8"
            str.endsWith("9") -> "9"
            str.endsWith("10") -> "10"
            str.endsWith("J") -> "J"
            str.endsWith("Q") -> "Q"
            str.endsWith("K") -> "K"
            else -> ""
        }
    }

    // 尽量做到与外界无关
    // 有点麻烦
    private fun updateClassifiedDictationPoker(str: String, i: Int) {
        if(!DataGlobal.classifiedDictionPokerInit){
            for (i in 1..NUM_PAGES) {
                Log.i("init", "$i") // 1-54
                DataGlobal.classifiedDictionPokers.add("")
            }
            DataGlobal.classifiedDictionPokerInit = true
        }

        val endString = endStr(str)
        when {
            str.equals(applicationContext.getString(R.string.King)) -> {
                DataGlobal.classifiedDictionPokers.add(i, "classE")
            }

            str.equals(applicationContext.getString(R.string.littleKing)) -> {
                DataGlobal.classifiedDictionPokers.add(i, "classF")
            }
            str.startsWith(applicationContext.getString(R.string.spades)) -> {
                DataGlobal.classifiedDictionPokers.add(i, "classA${endString}")
            }
            str.startsWith(applicationContext.getString(R.string.hearts)) -> {
                DataGlobal.classifiedDictionPokers.add(i, "classB${endString}")
            }
            str.startsWith(applicationContext.getString(R.string.plumBlossom)) -> {
                DataGlobal.classifiedDictionPokers.add(i, "classC${endString}")
            }
            str.startsWith(applicationContext.getString(R.string.squarePiece)) -> {
                DataGlobal.classifiedDictionPokers.add(i, "classD${endString}")
            }
        }
        // 是花色牌
        Log.i(TAG, "Now Data global Classified pokers is :${DataGlobal.classifiedDictionPokers}")
    }

    private fun updateDictationPoker(str: String) {
        Log.i(TAG, "Update Poker get Str: ${str}. Setting to position: ${currentPager - 1}")
        dictationPokers[currentPager - 1] = str

        // 更新标准化扑克牌
        updateClassifiedDictationPoker(str, currentPager - 1)
        Log.i(TAG, "Now dictation pokers is: ${dictationPokers}")

        /**
        numberStr = ""
        colorStr = ""
        kingStr = ""
         */

        //if (str in listOf("大王", "小王")) {


        if (str in listOf(
                "${applicationContext.getString(R.string.King)}",
                "${applicationContext.getString(R.string.littleKing)}"
            )
        ) {
            Log.i(TAG, "Clear NumberStr and ColorStr")
            numberStr = ""
            colorStr = ""
        } else {
            Log.i(TAG, "Clear KingStr")
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

                            if (kingStr.isNotBlank()) {
                                Log.i(
                                    TAG,
                                    "selectKing. then update dictation pokers with kingStr: ${kingStr}"
                                )
                                updateDictationPoker(kingStr)
                                // 典型化
                                //updateDictationPoker(kingStrToClassifiedStr(kingStr))
                                selectKingClearOtherRadioGroup()
                            }
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

            // 典型化
            //updateDictationPoker("${colorStrToClassifiedStr(colorStr)}${numberStr}")
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
        // dictationPokers = updateDictationPokersToClassifiedPokers(dictationPokers)
        Log.i(TAG, "Classified dictation pokers: ${dictationPokers}")

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
        dictationTime = toStr.substring(toStr.length - 5)
        intent.putExtra("DictationTime", dictationTime)
        intent.putExtra("RememberTime", rememberTime)

        Log.i(TAG, "回忆用时： $dictationTime")
        Log.i(TAG, "记忆用时: $rememberTime")
        //bundle.putSerializable("dictationPokers", this.dictationPokers)
        //intent.putParcelableArrayListExtra("dictationPokers",dictationPokers.toTypedArray()cncn)
        startActivity(intent)
    }

    private fun colorStrToClassifiedStr(str: String): String {
        return when (str) {
            applicationContext.getString(R.string.spades) -> "classA"
            applicationContext.getString(R.string.hearts) -> "classB"
            applicationContext.getString(R.string.squarePiece) -> "classC"
            applicationContext.getString(R.string.plumBlossom) -> "classD"
            else -> ""
        }
    }

    private fun kingStrToClassifiedStr(str: String): String {
        return when (str) {
            applicationContext.getString(R.string.littleKing) -> "classF"
            applicationContext.getString(R.string.King) -> "classE"
            else -> ""
        }
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