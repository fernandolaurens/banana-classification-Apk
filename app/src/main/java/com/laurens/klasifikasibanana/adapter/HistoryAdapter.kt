package com.laurens.klasifikasibanana.adapter

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.laurens.klasifikasibanana.database.AnalysisResult
import com.laurens.klasifikasibanana.databinding.ItemHistoryBinding
import com.laurens.klasifikasibanana.view.history.DetailActivity
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter(
    private var items: List<AnalysisResult>,
    private val onDeleteClick: (AnalysisResult) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AnalysisResult) {
            binding.apply {
                imageView.load(item.imageUri)
                tvLabel.text = item.label
                tvScore.text = "Score: ${(item.score * 100).toInt()}%"

                btndelete.setOnClickListener {
                    AlertDialog.Builder(root.context)
                        .setTitle("Delete Confirmation")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Yes") { dialog, which ->
                            onDeleteClick(item)
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
                root.setOnClickListener {
                    val context = root.context
                    val intent = Intent(context, DetailActivity::class.java).apply {
                        putExtra("imageUri", item.imageUri)
                        putExtra("label", item.label)
                        putExtra("score", item.score)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<AnalysisResult>) {
        items = newItems.reversed() // Ensure the newest item is on top
        notifyDataSetChanged()
    }


}
