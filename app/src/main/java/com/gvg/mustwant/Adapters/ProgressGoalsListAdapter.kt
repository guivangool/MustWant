package com.gvg.mustwant

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gvg.mustwant.DataEntities.GoalProgressEntity
import com.gvg.mustwant.databinding.ProgressGoalListBinding
import java.text.SimpleDateFormat
import java.util.*

class ProgressGoalsListAdapter :
    ListAdapter<GoalProgressEntity, ProgressGoalsListAdapter.ViewHolder>(DiffUtilCallBackProgressGoal) {

    class ViewHolder (binding: ProgressGoalListBinding): RecyclerView.ViewHolder(binding.root) {
        var tvProgressGoal = binding.tvProgress
        var tvProgressGoalDate = binding.tvProgressGoalDate
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProgressGoalsListAdapter.ViewHolder {
        return ViewHolder(
            ProgressGoalListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProgressGoalsListAdapter.ViewHolder, position: Int) {
        holder.tvProgressGoal.text = when (getItem(position).goalProgress)
        {
            0 -> "IGUAL"
            1-> "MEJORA"
            -1-> "RETROCESO"
            else -> ""
        }

        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        holder.tvProgressGoalDate.text = sdf.format(getItem(position).progressDate)

        if(position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#e9ecef"))
        }
    }
}
object DiffUtilCallBackProgressGoal : DiffUtil.ItemCallback<GoalProgressEntity>() {
    override fun areItemsTheSame(
        oldItem: GoalProgressEntity,
        newItem: GoalProgressEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: GoalProgressEntity,
        newItem: GoalProgressEntity
    ): Boolean {
        return oldItem == newItem
    }
}