package com.rafaykalim.quikorderchallenge.Controllers

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.rafaykalim.quikorderchallenge.Models.Location
import com.rafaykalim.quikorderchallenge.R


class LocationAdapter internal constructor(public val context: Context, public val locationList: ArrayList<Location>) : BaseExpandableListAdapter()
{
    var originalList = ArrayList<Location>()
    var showIndices = ArrayList<Int>()

    init {
        this.originalList.addAll(this.locationList)
    }

    override fun getGroup(groupPosition: Int): Any? {
            return locationList[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View? {

        var view : View
        if (convertView == null)
        {
            var layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.card_location, null)

            var nameBox = view.findViewById<TextView>(R.id.card_location_name)
            var numEmployeeBox = view.findViewById<TextView>(R.id.card_location_num_employees)
            var backgroundImg = view.findViewById<ImageView>(R.id.card_location_background)

            backgroundImg.setImageResource(originalList[groupPosition].backgroundImgId)
            nameBox.text = originalList[groupPosition].name
            numEmployeeBox.text = "${originalList[groupPosition].items.size} Employees"
        }
        else
        {
            return convertView
        }
        Log.d("ADDED", this.originalList.size.toString())

        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return locationList[groupPosition].items.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return locationList[groupPosition].items[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
            return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view : View
        if (convertView == null)
        {
            var layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.card_employee, null)

            var nameBox = view.findViewById<TextView>(R.id.card_employee_name)
            var jobBox = view.findViewById<TextView>(R.id.card_employee_job)

            nameBox.text = originalList[groupPosition].items[childPosition].name
            jobBox.text = originalList[groupPosition].items[childPosition].title
        }
        else
        {
            return convertView
        }
        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return originalList.size
    }

    fun filterData(query : String)
    {
        var query = query.toLowerCase()
        this.showIndices.clear()

        if (query.isEmpty())
        {
            for (i in 1 until this.locationList.size)
            {
                this.showIndices.add(i)
            }
        }
        else
        {
            for (location : Location in this.originalList)
            {
                if (location.name.toLowerCase().contains(query))
                {
                    this.showIndices.add(this.locationList.indexOf(location))
                    Log.d("ADDED", location.name)
                }
            }
        }
        Log.d("ADDED", showIndices.toString())
        notifyDataSetChanged()
    }
}