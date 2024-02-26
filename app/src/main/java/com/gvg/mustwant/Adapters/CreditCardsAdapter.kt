package com.gvg.mustwant.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gvg.mustwant.DataEntities.CreditCard
import com.gvg.mustwant.R
import com.gvg.mustwant.databinding.CreditcardBinding
class CreditCardsAdapter(private val context:Context, val items: ArrayList<CreditCard>) :
    RecyclerView.Adapter<CreditCardsAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    class ViewHolder(binding: CreditcardBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvNumber = binding.tvNumber
        val tvName = binding.tvName
        val tvExpDate = binding.tvExpDate
        val ivType = binding.ivType
        val tvId = binding.tvId
        val llCreditCard = binding.llCreditCard
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CreditcardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvNumber.text = hideCreditCardNumber(items[position].number.toString())
        holder.tvName.text = items[position].fullName
        holder.tvExpDate.text = "${items[position].month}/${items[position].year}"
        holder.tvId.text = items[position].id.toString()

        //Change image
        when (items[position].number[0])
        {
            '3' -> holder.ivType.setImageResource( R.drawable.american)
            '4' -> holder.ivType.setImageResource( R.drawable.visa)
            '5' -> holder.ivType.setImageResource( R.drawable.master)
        }

        holder.llCreditCard.setOnClickListener {
           if (onClickListener != null) {
                onClickListener!!.onCLick(holder.tvId.text.toString())
          }
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }

    private fun hideCreditCardNumber(number:String):String
    {
        val hideNumber = StringBuilder()

        for (i in 0 until number.length - 4) {
            hideNumber.append('X')
        }

        hideNumber.append(number.substring(number.length - 4))

        return hideNumber.toString()
    }

    interface OnClickListener {
        fun onCLick(idCreditCard: String)
    }
}