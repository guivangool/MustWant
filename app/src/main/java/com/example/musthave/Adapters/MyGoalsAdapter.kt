package com.example.musthave.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musthave.DomainEntities.MyGoal
import com.example.musthave.databinding.GoalBinding

class MyGoalsAdapter(val items: ArrayList<MyGoal>) :
    RecyclerView.Adapter<MyGoalsAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    class ViewHolder(binding: GoalBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvGoal = binding.tvGoal
        val tvPercentaje = binding.tvPercentaje
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(GoalBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val descriptionGoal = when (items[position].goalId) {
            1 -> "Yo"
            2 -> "Hogar"
            3 -> "Trabajo"
            4 -> "Relaciones"
            else -> ""
        }
        holder.tvGoal.text = descriptionGoal
        holder.tvPercentaje.text = items[position].goalPercentaje.toString() + "%"

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

