package com.example.shkudikweatherapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.activities.MainActivity
import com.example.shkudikweatherapp.providers.WeatherProvider
import kotlinx.android.synthetic.main.rv_help_item.view.*

class RvHelpViewHolder(context: Context, inflater: LayoutInflater, parent: ViewGroup
    ) : RecyclerView.ViewHolder(inflater.inflate(R.layout.rv_help_item, parent, false)) {

    fun bind(string: String, position: Int, activity: MainActivity) {

        itemView.apply {

            this.findViewById<TextView>(R.id.tvHelp).text = string

            this.setOnClickListener { activity.fillTheInput(string) }

        }

        itemView.imgDeleteHelp.setOnClickListener {

            WeatherProvider.removeHelpCity(position)
            activity.updateRv()

        }


    }

}