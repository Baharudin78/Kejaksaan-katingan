package com.baharudin.pengaduanapp.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.baharudin.pengaduanapp.R
import com.baharudin.pengaduanapp.databinding.ItemCategoryBinding
import com.baharudin.pengaduanapp.domain.model.CategoryModel
import com.baharudin.pengaduanapp.util.withDelay

class CategoryAdapter(
    private val letterKinds : List<CategoryModel>
) : RecyclerView.Adapter<CategoryAdapter.LetterKindHolder>(){

    private var selectedPosition = -1

    interface OnItemClickListener{
        fun onClick(letterKinds: CategoryModel)
    }

    fun setItemClickListener(clickInterface : OnItemClickListener) {
        onClickListener = clickInterface
    }

    private var onClickListener : OnItemClickListener? = null

    inner class LetterKindHolder(
        val binding : ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun binItem(item : CategoryModel){
            binding.apply {
                binding.tvCategory.text = item.nama
            }
            if (item.isSelected){
                binding.layoutCard.background =
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.border_selected
                    )
                binding.tvCategory.setTextColor(Color.WHITE)
            }
            binding.root.setOnClickListener {
                onClickListener?.onClick(item)
                selectItem(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterKindHolder {
        val inflater = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LetterKindHolder(inflater)
    }

    override fun onBindViewHolder(holder: LetterKindHolder, position: Int) {
        holder.binItem(letterKinds[position])
    }

    override fun getItemCount(): Int {
        return letterKinds.size
    }

    private fun selectItem(position: Int) {
        if(position != selectedPosition) {
            if(selectedPosition > -1) {
                letterKinds[selectedPosition].isSelected = false
                notifyItemChanged(selectedPosition)
            }

            selectedPosition = position
            letterKinds[position].isSelected = true

            withDelay {
                notifyItemChanged(position)
            }
        }
    }
}