package com.laurens.klasifikasibanana.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.laurens.klasifikasibanana.data.banana
import com.laurens.klasifikasibanana.databinding.ItemCardBinding

class BananaAdapter(private val listPisang: ArrayList<banana>) : RecyclerView.Adapter<BananaAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root)


    open class OnItemClickCallback {
        open fun onItemClicked(data: banana) {}
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listPisang.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val (name, description, photo) = listPisang[position]
        holder.binding.imgItemPhoto.setImageResource(photo)
        holder.binding.tvItemName.text = name
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listPisang[holder.adapterPosition])
        }
    }
}