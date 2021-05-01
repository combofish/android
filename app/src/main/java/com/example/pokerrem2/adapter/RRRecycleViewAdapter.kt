package com.example.pokerrem2.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pokerrem2.HistoryActivity
import com.example.pokerrem2.R

import com.example.pokerrem2.data.ReciteRecord
import com.example.pokerrem2.utils.DBHandler


@Suppress("DEPRECATION")
class RRRecycleViewAdapter(private var rr: List<ReciteRecord>, private val context: Context) :
    RecyclerView.Adapter<RRRecycleViewAdapter.RRViewHolder>() {

    private var dbHandler = DBHandler(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RRViewHolder {
        val view = View.inflate(context, R.layout.rr_item_rv, null)
        return RRViewHolder(view)
    }

    override fun onBindViewHolder(holder: RRViewHolder, position: Int) {
        Log.i("TAG", "getPosition: $position")
        Log.i("TAG", "position: ${rr[position].rememberTime}")

        holder.innerPosition = rr[position].id
        holder.tvRight.setText("${rr[position].right}")
        holder.tvWrong.setText("${(54 - rr[position].right)}")
        holder.tvRate.setText("${String.format("%.2f", rr[position].rate)}")
        holder.tvRememberTime.setText("${rr[position].rememberTime}")
        holder.tvDictationTime.setText("${rr[position].dictationTime}")

        /**
        holder.tvRight.setText("正确: ${rr[position].right}")
        holder.tvWrong.setText("错误: ${(54 - rr[position].right)}")
        holder.tvRate.setText("正确率: ${String.format("%.2f", rr[position].rate)}")
        holder.tvRememberTime.setText("背诵时间: ${rr[position].rememberTime}")
        holder.tvDictationTime.setText("回忆时间: ${rr[position].dictationTime}")
         */
    }

    override fun getItemCount(): Int {
        return rr.size
    }

    inner class RRViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var innerPosition: Int = 0
        val tvRight: TextView
        val tvWrong: TextView
        val tvRate: TextView
        val tvRememberTime: TextView
        val tvDictationTime: TextView
        val btn: Button

        init {
            tvRight = itemView.findViewById(R.id.tv_right_rr)
            tvWrong = itemView.findViewById(R.id.tv_wrong_rr)
            tvRate = itemView.findViewById(R.id.tv_correctRate_rr)
            tvRememberTime = itemView.findViewById(R.id.rememberTime_rr)
            tvDictationTime = itemView.findViewById(R.id.dictationTime_rr)
            btn = itemView.findViewById(R.id.deleteRecord)

            btn.setOnClickListener(this)

            itemView.setOnClickListener {
                mOnItemCLickListener?.onRecycleItemClick(adapterPosition)
            }
        }

        override fun onClick(v: View?) {
            Log.i("TAG", "del button press,position: ")
            val reciteRecord = ReciteRecord(
                innerPosition,
                2,
                2.2f,
                "",
                ""
            )
            Log.i("TAG", "Want to delete:${reciteRecord}")
            dbHandler.deleteReciteRecord(reciteRecord)
            var intent = Intent(context, HistoryActivity::class.java)
            context.startActivity(intent)
        }

    }

    var mOnItemCLickListener: OnRecycleItemClickListener? =
        null

    fun setRecycleItemClickListener(listener: OnRecycleItemClickListener?) {
        mOnItemCLickListener = listener
    }

    interface OnRecycleItemClickListener {
        fun onRecycleItemClick(position: Int)
    }

}