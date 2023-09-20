package com.gvg.mustwant.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gvg.mustwant.DataEntities.GoalEntity
import com.gvg.mustwant.Enums.GoalTypeEnum
import com.gvg.mustwant.R
import com.gvg.mustwant.databinding.GoalBinding

class MyGoalsAdapter(private val context:Context, val items: ArrayList<GoalEntity>) :
    RecyclerView.Adapter<MyGoalsAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    class ViewHolder(binding: GoalBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvGoal = binding.tvGoal
        val tvPercentaje = binding.tvPercentaje
        val tvPendingDays = binding.tvPendingDays
        val llGoal = binding.llGoal

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(GoalBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvGoal.text = GoalTypeEnum.values().find { it.number == items[position].goalId }?.getText(context)
        holder.tvPercentaje.text = ((items[position].goalPercentaje*100)/items[position].goalDays).toString() + "%"
        holder.tvPendingDays.text = (items[position].goalDays- items[position].goalPercentaje).toString()

        //Change background color
        when (items[position].lastRecord)
        {
            0 -> holder.llGoal.background = ContextCompat.getDrawable(context,R.drawable.bg_goal_equal)
            1 -> holder.llGoal.background = ContextCompat.getDrawable(context,R.drawable.bg_goal_increase)
            -1 -> holder.llGoal.background = ContextCompat.getDrawable(context,R.drawable.bg_goal_decrease)
        }

        holder.tvGoal.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onCLick(holder.tvGoal.text.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    interface OnClickListener {
        fun onCLick(goal: String)
    }
}

