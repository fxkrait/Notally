package com.omgodse.notally.recyclerview.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omgodse.notally.databinding.RecyclerBaseNoteBinding
import com.omgodse.notally.databinding.RecyclerHeaderBinding
import com.omgodse.notally.recyclerview.ItemListener
import com.omgodse.notally.recyclerview.viewholders.BaseNoteVH
import com.omgodse.notally.recyclerview.viewholders.HeaderVH
import com.omgodse.notally.room.BaseNote
import com.omgodse.notally.room.Header
import com.omgodse.notally.room.Item
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat

class BaseNoteAdapter(
    private val dateFormat: String,
    private val textSize: String,
    private val maxItems: Int,
    private val maxLines: Int,
    private val formatter: SimpleDateFormat,
    private val listener: ItemListener
) : ListAdapter<Item, RecyclerView.ViewHolder>(DiffCallback) {

    private val prettyTime = PrettyTime()

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Header -> 0
            is BaseNote -> 1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is Header -> (holder as HeaderVH).bind(item)
            is BaseNote -> (holder as BaseNoteVH).bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> {
                val binding = RecyclerHeaderBinding.inflate(inflater, parent, false)
                HeaderVH(binding)
            }
            else -> {
                val binding = RecyclerBaseNoteBinding.inflate(inflater, parent, false)
                BaseNoteVH(binding, dateFormat, textSize, maxItems, maxLines, listener, prettyTime, formatter)
            }
        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<Item>() {

        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return when (oldItem) {
                is BaseNote -> if (newItem is BaseNote) {
                    oldItem.id == newItem.id
                } else false
                is Header -> if (newItem is Header) {
                    oldItem.label == newItem.label
                } else false
            }
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return when (oldItem) {
                is BaseNote -> if (newItem is BaseNote) {
                    oldItem == newItem
                } else false
                is Header -> if (newItem is Header) {
                    oldItem.label == newItem.label
                } else false
            }
        }
    }
}