package com.rafaykalim.quikorderchallenge.LayoutFiles

import android.view.View
import android.widget.TextView
import com.rafaykalim.quikorderchallenge.Models.Employee
import com.rafaykalim.quikorderchallenge.R
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder

// View Holder for the Employee Card (res/layout/card_employee). Called in Controllers/LocationAdapter
class EmployeeViewHolder(itemView: View) : ChildViewHolder(itemView)
{
    private var nameTextViewHolder: TextView

    init {
        nameTextViewHolder = itemView.findViewById(R.id.card_employee_name)
    }

    fun setData(name : String)
    {
        nameTextViewHolder.text = name
    }
}