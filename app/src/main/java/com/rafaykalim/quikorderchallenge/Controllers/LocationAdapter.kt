package com.rafaykalim.quikorderchallenge.Controllers

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.rafaykalim.quikorderchallenge.Models.Employee
import com.rafaykalim.quikorderchallenge.Models.Location
import com.rafaykalim.quikorderchallenge.R


class LocationAdapter internal constructor(public val context: Context, public val locationList: ArrayList<Location>) : BaseExpandableListAdapter()
{

    // Boilerplate
    override fun getGroup(groupPosition: Int): Any? {
            return locationList[groupPosition]
    }
    // Boilerplate
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }
    // Boilerplate
    override fun hasStableIds(): Boolean {
        return true
    }

    // ideally, create a view holder like a RecyclerView
    // For a larger, more complex application that would be ideal
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View? {

        var view : View
        var currGroup = locationList[groupPosition]
        if (convertView == null)
        {
            // Inflate view and set appropriate values
            var layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.card_location, null)

            var nameBox = view.findViewById<TextView>(R.id.card_location_name)
            var numEmployeeBox = view.findViewById<TextView>(R.id.card_location_num_employees)
            var backgroundImg = view.findViewById<ImageView>(R.id.card_location_background)

            // If there is no image provided (for example a new location)
            // The default "no image available" image is used R.drawable.card_backhround_no_image.png
            // The color for that is white, so text color is default black
            if (currGroup.backgroundImgId != null)
            {
                backgroundImg.setImageResource(currGroup.backgroundImgId!!)
                nameBox.setTextColor(ContextCompat.getColor(context, R.color.white))
                numEmployeeBox.setTextColor(ContextCompat.getColor(context, R.color.white))
            }

            nameBox.text = currGroup.name
            numEmployeeBox.text = context.getString(R.string.num_employees_text, currGroup.employeeList.size.toString())
        }
        else
        {
            return convertView
        }

        return view
    }

    // Boilerplate
    override fun getChildrenCount(groupPosition: Int): Int {
        return locationList[groupPosition].employeeList.size
    }

    // Boilerplate
    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return locationList[groupPosition].employeeList[childPosition]
    }

    // Boilerplate
    override fun getGroupId(groupPosition: Int): Long {
            return groupPosition.toLong()
    }

    // sets required data.
    // ideally, create a view holder like a RecyclerView
    // For a larger, more complex application that would be ideal
    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view : View
        var currEmployee = locationList[groupPosition].employeeList[childPosition]
        if (convertView == null)
        {
            // Inflate view and set values
            var layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.card_employee, null)

            var nameBox = view.findViewById<TextView>(R.id.card_employee_name)
            var jobBox = view.findViewById<TextView>(R.id.card_employee_job)

            nameBox.text = currEmployee.name
            jobBox.text = currEmployee.title
        }
        else
        {
            return convertView
        }
        return view
    }

    // Boilerplate
    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    // Boilerplate
    override fun getGroupCount(): Int {
        return locationList.size
    }

    // NOT BEING USED!
    // This is what I tried to use to do the searching, but it gave me weird inconsistent results
    // TODO : Debug this
//    fun filterData(query : String)
//    {
//        var query = query.toLowerCase()
//        this.showIndices.clear()
//
//        if (query.isEmpty())
//        {
//            for (i in 1 until this.locationList.size)
//            {
//                this.showIndices.add(i)
//            }
//        }
//        else
//        {
//            for (location : Location in this.originalList)
//            {
//                if (location.name.toLowerCase().contains(query))
//                {
//                    this.showIndices.add(this.locationList.indexOf(location))
//                    Log.d("ADDED", location.name)
//                }
//            }
//        }
//        Log.d("ADDED", showIndices.toString())
//        notifyDataSetChanged()
//    }
}