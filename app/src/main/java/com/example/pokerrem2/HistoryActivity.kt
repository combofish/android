package com.example.pokerrem2

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokerrem2.adapter.MyRecycleViewAdapter
import com.example.pokerrem2.adapter.RRRecycleViewAdapter
import com.example.pokerrem2.data.ReciteRecord
import com.example.pokerrem2.utils.DBHandler

class HistoryActivity : AppCompatActivity() {
    private val TAG = "HistoryActivity"
    private lateinit var rrList:List<ReciteRecord>
    private lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rv3)
        initData()

        var linearLayoutManager = LinearLayoutManager(this);
        recyclerView.layoutManager = linearLayoutManager

        var rrRecycleViewAdapter = RRRecycleViewAdapter(rrList, this)
        recyclerView.adapter = rrRecycleViewAdapter


    }

    /**
     * 初始化记录数据
     */
    private fun initData() {
        dbHandler = DBHandler(this)
        rrList = dbHandler.viewReciteRecord()
        Log.i(TAG,"Get all rr: ${rrList.toString()}")

        if (rrList.isEmpty()) {
            Log.i(TAG, "你还没有历史记录")
            Toast.makeText(applicationContext, "${applicationContext.getString(R.string.noHistoryFound)}", Toast.LENGTH_SHORT)
                .show()
        }
    }
}