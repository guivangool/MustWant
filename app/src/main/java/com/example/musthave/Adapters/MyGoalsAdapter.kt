package com.example.musthave.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musthave.DataEntities.GoalEntity
import com.example.musthave.Enums.GoalTypeEnum
import com.example.musthave.databinding.GoalBinding
import kotlin.math.round

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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(GoalBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvGoal.text = GoalTypeEnum.values().find { it.number == items[position].goalId }?.getText(context)
        holder.tvPercentaje.text = ((items[position].goalPercentaje*100)/items[position].goalDays).toString() + "%"
        holder.tvPendingDays.text = (items[position].goalDays- items[position].goalPercentaje).toString()

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

