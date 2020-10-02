package com.example.shkudikweatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shkudikweatherapp.activities.MainActivity

class RvHelpAdapter(val activity: MainActivity, val helpList: ArrayList<String>) : RecyclerView.Adapter<RvHelpViewHolder>() {

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvHelpViewHolder =
        RvHelpViewHolder(context!!, LayoutInflater.from(parent.context), parent)


    override fun getItemCount(): Int = helpList.size

    override fun onBindViewHolder(holder: RvHelpViewHolder, position: Int) {

        holder.bind(helpList[position], position, this.activity)

    }
}