package com.rafaykalim.quikorderchallenge.LayoutFiles

import android.view.View
import android.widget.TextView
import com.rafaykalim.quikorderchallenge.R
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.card_location.view.*

// View Holder for the Location Card (res/layout/card_location). Called in Controllers/LocationAdapter
class LocationViewHolder(itemView : View) : GroupViewHolder(itemView)
{
    private var nameTextView : TextView

    init {
        nameTextView = itemView.findViewById(R.id.card_location_name)
    }

    fun setData(name : String)
    {
        nameTextView.text = name
    }
}

