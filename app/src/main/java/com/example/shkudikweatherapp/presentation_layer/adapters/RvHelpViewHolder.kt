package com.example.shkudikweatherapp.presentation_layer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.presentation_layer.main_activity.views.BoardImpl
import com.example.shkudikweatherapp.presentation_layer.main_activity.views.RecyclerHelpImpl
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider
import kotlinx.android.synthetic.main.rv_help_item.view.*

class RvHelpViewHolder(inflater: LayoutInflater, parent: ViewGroup
    ) : RecyclerView.ViewHolder(inflater.inflate(R.layout.rv_help_item, parent, false)) {

    fun bind(string: String, position: Int, recyclerHelpImpl: RecyclerHelpImpl, boardImpl: BoardImpl) {

        itemView.apply {

            this.findViewById<TextView>(R.id.tvHelp).text = string

        }

        itemView.imgDeleteHelp.setOnClickListener {

            WeatherProvider.removeHelpCity(position)
            recyclerHelpImpl.update()

        }

        itemView.setOnClickListener {

            boardImpl.setCity(string)

        }


    }

}