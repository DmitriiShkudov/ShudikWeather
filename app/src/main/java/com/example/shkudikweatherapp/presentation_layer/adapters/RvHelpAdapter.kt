package com.example.shkudikweatherapp.presentation_layer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shkudikweatherapp.presentation_layer.main_activity.views.BoardImpl
import com.example.shkudikweatherapp.presentation_layer.main_activity.views.RecyclerHelpImpl

class RvHelpAdapter(private val recyclerHelpImpl: RecyclerHelpImpl, private val boardImpl: BoardImpl, private val helpList: ArrayList<String>) :
    RecyclerView.Adapter<RvHelpViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RvHelpViewHolder(LayoutInflater.from(parent.context), parent)


    override fun getItemCount(): Int = helpList.size

    override fun onBindViewHolder(holder: RvHelpViewHolder, position: Int) {

        holder.bind(helpList[position], position, recyclerHelpImpl, boardImpl)

    }
}