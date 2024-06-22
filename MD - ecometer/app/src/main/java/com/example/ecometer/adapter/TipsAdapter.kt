package com.example.ecometer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecometer.databinding.ItemTipBinding

class TipsAdapter(private val tips: List<Pair<String, String>>) : RecyclerView.Adapter<TipsAdapter.TipViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
        val binding = ItemTipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        holder.bind(tips[position])
    }

    override fun getItemCount(): Int = tips.size

    inner class TipViewHolder(private val binding: ItemTipBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tip: Pair<String, String>) {
            binding.tip = tip
            binding.executePendingBindings()
        }
    }
}
