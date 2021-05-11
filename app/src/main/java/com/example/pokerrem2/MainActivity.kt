package com.example.pokerrem2

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokerrem2.adapter.MyRecycleViewAdapter
import com.example.pokerrem2.data.DataGlobal
import com.example.pokerrem2.data.Poker
import com.example.pokerrem2.utils.StatusBarUtils


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "MainActivity"
    private var myRecycleViewAdapter: MyRecycleViewAdapter? = null
    private var pokers = mutableListOf<String>()
    private lateinit var timer1: Chronometer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        // getWindow().getDecorView()
        //   .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
         */
        // StatusBarUtils.setStatusBarLightMode(window)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // 进行全局存储
        pokers = Poker().randomPoker
        DataGlobal.pokers = pokers
        Log.i(TAG, "Global pokers: ${DataGlobal.pokers}")


        val recyclerView = findViewById<RecyclerView>(R.id.rv)

        val gridLayoutManager = GridLayoutManager(this, 5)
        recyclerView.layoutManager = gridLayoutManager

        myRecycleViewAdapter = MyRecycleViewAdapter(pokers, this)
        recyclerView.adapter = myRecycleViewAdapter

        findViewById<Button>(R.id.reDirrupt).setOnClickListener(this)
        findViewById<Button>(R.id.startTimer).setOnClickListener(this)
        findViewById<Button>(R.id.startDictation).setOnClickListener(this)
        findViewById<Button>(R.id.history).setOnClickListener(this)
        Log.i("tag", "onCreate")

        // 默认开始时钟
        timer1 = findViewById(R.id.chronometer1)
        timer1.base = SystemClock.elapsedRealtime()
        timer1.start()

    }

    override fun onResume() {
        super.onResume()
        Log.i("tag", "onResume")

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.reDirrupt -> {
                // 更改 pokers
                val randomPoker = Poker().randomPoker
                myRecycleViewAdapter?.changePokers(randomPoker)
                pokers = randomPoker
                // 全局更新
                DataGlobal.pokers = randomPoker
                Log.i(TAG, "Global pokers: ${DataGlobal.pokers}")

                // 清零计时
                timer1.stop()
                timer1.base = SystemClock.elapsedRealtime()
                timer1.start()

                Toast.makeText(
                    applicationContext,
                    "${applicationContext.getString(R.string.isShufflingPlaying)}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            R.id.startTimer -> {
                timer1.base = SystemClock.elapsedRealtime()
                //timer1.format = "Tim %s"
                timer1.start()
                Toast.makeText(
                    applicationContext,
                    "${applicationContext.getString(R.string.timingBegins)}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            R.id.startDictation -> {
                var intent = Intent(this, DictationActivity::class.java)
                var pokerBundle = Bundle()
                for (i in 0..53) {
                    pokerBundle.putString("$i", pokers[i])
                }
                Log.i("use", pokerBundle.toString())
                intent.putExtra("pokerBundle", pokerBundle)

                // 储存时间
                timer1.stop()
                var rememberTimeStr = timer1.text.toString()
                intent.putExtra("RememberTime", rememberTimeStr)
                Log.i(TAG, "所用时间： $rememberTimeStr")
                startActivity(intent)

                /**
                Toast.makeText(applicationContext, "开始背诵", Toast.LENGTH_SHORT)
                .show()
                 */
            }
            R.id.history -> {
                timer1.base = SystemClock.elapsedRealtime()
                timer1.stop()

                var intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
                Log.i(TAG, "跳转历史")
                //Toast.makeText(applicationContext, "查看背诵历史", Toast.LENGTH_SHORT)
                //    .show()
            }
        }
    }

    fun refresh() {
        finish()
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}