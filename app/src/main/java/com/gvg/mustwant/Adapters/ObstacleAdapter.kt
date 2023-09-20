package com.gvg.mustwant

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gvg.mustwant.DataEntities.ObstacleEntity
import com.gvg.mustwant.databinding.ObstacleBinding
import java.text.SimpleDateFormat
import java.util.*

class ObstacleAdapter :
    ListAdapter<ObstacleEntity, ObstacleAdapter.ViewHolder>(DiffUtilCallBackObstacle) {
    private var onClickListener: ObstacleAdapter.OnClickListener? = null

    class ViewHolder (binding: ObstacleBinding): RecyclerView.ViewHolder(binding.root) {
        var tvObstacleDescription = binding.tvObstacleDescription
        var tvObstacleDate = binding.tvObstacleDate
        var rbPendingState = binding.rbPendingState
        var rbCompletedState = binding.rbCompletedState
        var rgObstacleState = binding.rgObstacleStatus
    }

    fun setOnClickListener(onClickListener: ObstacleAdapter.OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onCLick(id:Int,status:Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ObstacleAdapter.ViewHolder {
        return ViewHolder(
            ObstacleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ObstacleAdapter.ViewHolder, position: Int) {
        holder.tvObstacleDescription.text = getItem(position).description
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        holder.tvObstacleDate.text = sdf.format(getItem(position).planedDate)
        if (getItem(position).status == 0) {
            holder.rbPendingState.isChecked = true
        }
        else if (getItem(position).status == 1){
            holder.rbCompletedState.isChecked = true
        }

        holder.rgObstacleState.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                var status = 0
               if (checkedId == R.id.rbCompletedState) status = 1
                onClickListener!!.onCLick(getItem(position).id!!,status)
            }
        })
        }
}
object DiffUtilCallBackObstacle : DiffUtil.ItemCallback<ObstacleEntity>() {
    override fun areItemsTheSame(
        oldItem: ObstacleEntity,
        newItem: ObstacleEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ObstacleEntity,
        newItem: ObstacleEntity
    ): Boolean {
        return oldItem == newItem
    }
}