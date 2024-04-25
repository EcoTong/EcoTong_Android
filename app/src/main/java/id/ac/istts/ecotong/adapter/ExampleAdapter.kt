package id.ac.istts.ecotong.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

//class ExampleAdapter() :
//    ListAdapter<Example, ExampleAdapter.ExampleViewHolder>(DIFF_CALLBACK) {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
//        val binding = ItemExampleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ExampleViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
//        val item = getItem(position)
////     Bind view wth item here
//    }
//
//    inner class ExampleViewHolder(val binding: ItemExampleBinding) : ViewHolder(binding.root)
//
//    companion object {
//        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Example>() {
//            override fun areItemsTheSame(oldItem: Example, newItem: Example): Boolean {
//                return oldItem == newItem
//            }
//
//            override fun areContentsTheSame(oldItem: Example, newItem: Example): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//}