package com.gvg.mustwant

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gvg.mustwant.DataEntities.GoalProgressEntity
import com.gvg.mustwant.databinding.ProgressGoalBinding

class GoalProgressAdapter (private val context: Context):
    ListAdapter<GoalProgressEntity, GoalProgressAdapter.ViewHolder>(DiffUtilCallBack) {
    private var onClickListener: OnClickListener? = null
    var goalColorMe = 0
    var goalColorWork = 0
    var goalColorHome = 0
    var goalColorRelations = 0


    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    class ViewHolder(binding: ProgressGoalBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvGoalProgress = binding.tvGoalProgress
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GoalProgressAdapter.ViewHolder {
        return ViewHolder(
            ProgressGoalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: GoalProgressAdapter.ViewHolder, position: Int) {
        when (getItem(position).goalID) {
            1 -> {
                holder.tvGoalProgress.text = context.getString(R.string.GoalMe)
                goalColorMe = getItem(position).goalProgress
            }
            2 -> {
                holder.tvGoalProgress.text = context.getString(R.string.GoalHome)
                goalColorWork = getItem(position).goalProgress
            }
            3 -> {
                holder.tvGoalProgress.text = context.getString(R.string.GoalWork)
                goalColorHome = getItem(position).goalProgress
            }
            4 -> {
                holder.tvGoalProgress.text = context.getString(R.string.GoalRelations)
                goalColorRelations = getItem(position).goalProgress
            }
        }

        when (getItem(position).goalProgress) {
            0 -> holder.tvGoalProgress.background = ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.bg_goal_progress_equal
            )
            1 -> holder.tvGoalProgress.background =
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_goal_progress_more)
            2 -> holder.tvGoalProgress.background =
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_goal_progress_less)
        }


        holder.tvGoalProgress.setOnClickListener {
            if (onClickListener != null) {
                val stateGoal = updateProgress(holder.tvGoalProgress.text.toString())
                when (stateGoal) {
                    0 -> holder.tvGoalProgress.background = ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.bg_goal_progress_equal
                    )
                    1 -> holder.tvGoalProgress.background = ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.bg_goal_progress_more
                    )
                    2 -> holder.tvGoalProgress.background = ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.bg_goal_progress_less
                    )
                }

                onClickListener!!.onCLick(holder.tvGoalProgress.text.toString(), stateGoal)
            }
        }
    }

    interface OnClickListener {
        fun onCLick(goal: String, stateGoal: Int)
    }


    private fun updateProgress(goalName: String): Int {
        when (goalName) {
            context.getString(R.string.GoalMe) -> {
                goalColorMe++
                if (goalColorMe > 2) goalColorMe = 0
                return goalColorMe
            }
            context.getString(R.string.GoalWork) -> {
                goalColorWork++
                if (goalColorWork > 2) goalColorWork = 0
                return goalColorWork
            }
            context.getString(R.string.GoalHome) -> {
                goalColorHome++
                if (goalColorHome > 2) goalColorHome = 0
                return goalColorHome
            }
            context.getString(R.string.GoalRelations) -> {
                goalColorRelations++
                if (goalColorRelations > 2) goalColorRelations = 0
                return goalColorRelations
            }
            else -> {
                return 0
            }
        }
    }
}

object DiffUtilCallBack : DiffUtil.ItemCallback<GoalProgressEntity>() {
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