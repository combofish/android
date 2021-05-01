package com.example.pokerrem2.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.example.pokerrem2.R
import com.example.pokerrem2.adapter.ResRecycleViewAdapter.ResViewHolder


class ResRecycleViewAdapter(
    private var pokers: MutableList<String>,
    private var dictationPokers: MutableList<String>,
    private val context: Context
) :
    RecyclerView.Adapter<ResViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResViewHolder {
        val view = View.inflate(context, R.layout.res_item_rv, null)
        return ResViewHolder(view)
    }

    fun changePokers(newPoker: MutableList<String>) {
        pokers = newPoker
        // notifyItemMoved(0,5)
        notifyItemRangeChanged(0, newPoker.size)
    }

    override fun onBindViewHolder(
        holder: ResViewHolder,
        position: Int
    ) {
        val poker = pokers[position]
        val selectString = poker.substring(0, 1)
        when (selectString) {
            "大" -> {
                holder.tv.setText("")
                holder.iv.setImageResource(R.drawable.poker_big_y)
            }
            "小" -> {
                holder.tv.setText("")
                holder.iv.setImageResource(R.drawable.poker_little_y)
            }
            "黑" -> {
                holder.tv.setText(poker.substring(2))
                holder.iv.setImageResource(R.drawable.poker_black_spades)
            }
            "红" -> {
                holder.tv.setText(poker.substring(2))
                holder.iv.setImageResource(R.drawable.poker_red_shade)
            }
            "梅" -> {
                holder.tv.setText(poker.substring(2))
                holder.iv.setImageResource(R.drawable.poker_three_shadow)
            }
            "方" -> {
                holder.tv.setText(poker.substring(2))
                holder.iv.setImageResource(R.drawable.poker_rand)
            }
        }
        if (pokers[position] != dictationPokers[position]) {
            // 设置边框
            holder.run { llBackground.setStroke(1, ContextCompat.getColor(context,R.color.red))}

        }else{
            holder.run { llBackground.setStroke(1, ContextCompat.getColor(context,R.color.white))}
        }
        Log.i("TAG", "getPosition: $position")
    }

    override fun getItemCount(): Int {
        return pokers.size
    }

    inner class ResViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv: ImageView
        val tv: TextView
        val btn: Button? = null
        var linearLayout: LinearLayout
        var llBackground: GradientDrawable

        init {
            tv = itemView.findViewById(R.id.tv1_res)
            iv = itemView.findViewById(R.id.iv1_res)
            linearLayout = itemView.findViewById(R.id.ll_res)
            llBackground = linearLayout.background as GradientDrawable

            // btn = itemView.findViewById(R.id.btn1);
            itemView.setOnClickListener {
                mOnItemCLickListener?.onRecycleItemClick(adapterPosition)
            }
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