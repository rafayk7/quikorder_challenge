package com.rafaykalim.quikorderchallenge.LayoutFiles

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.rafaykalim.quikorderchallenge.Controllers.LocationAdapter
import com.rafaykalim.quikorderchallenge.Models.Location
import com.rafaykalim.quikorderchallenge.R
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ExpandableListView
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.rafaykalim.quikorderchallenge.Models.Employee
import com.rafaykalim.quikorderchallenge.Utils.DataUtil


class EmployeeSearchActivity : AppCompatActivity() {
    lateinit var locationAdapter : LocationAdapter
    lateinit var recyclerView : ExpandableListView
    lateinit var searchBar : EditText
    lateinit var locationList : ArrayList<Location>
    lateinit var sendList : ArrayList<Location>
    lateinit var filterListButton : ImageView
    var filterOn = "Location"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var dataUtil = DataUtil(this@EmployeeSearchActivity)

        // Get needed data from Utils/DataUtil class
        var employeeList = dataUtil.getEmployeeList()
        locationList = dataUtil.convertEmployeeListToLocationList(employeeList)

        setContentView(R.layout.activity_employee_search)

        // Initialize Views
        recyclerView = findViewById(R.id.activity_employee_search_location_recycler)
        searchBar = findViewById(R.id.activity_employee_search_bar)
        filterListButton = findViewById(R.id.activity_employee_filter_list)

        // This is the button to filter between employee name and location for list
        filterListButton.setOnClickListener { changeFilterCondition() }

        // sendList is the list which is sent to the LocationAdapter each time. I use the same list for simplicity
        sendList = ArrayList()

        // Just starting up app, show all
        sendList.addAll(locationList)
        sendList = sortList(sendList)

        locationAdapter = LocationAdapter(this@EmployeeSearchActivity, sendList)
        recyclerView.setAdapter(locationAdapter)

        // This is where I implement the search function.
        // I know that this doesn't follow the MVC architecture 100% but I spent a lot of time trying to figure out how
        // To make the ExpandableListView searchable, however I kept getting inconsistent results.
        // Ideally, the LocationAdapter would implement Filterable and here we would just call the Filter.filter
        // Property. If I have more time I will try to make that work, but for now this is a working (though not optimal) solution
        searchBar.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?) {
                // No Text, show all
                if (searchBar.text.isEmpty())
                {

                    // Turn clear text icon to search icon
                    searchBar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.material_search_24, 0)

                    sendList = filterListEmpty()
                    // Re-instantiates locationAdapter to a new LocationAdapter with this updated list
                    updateList(sendList)
                }
                else
                { // Text exists
                    // Empty it and add needed ones.
                    sendList.clear()

                    var currText = searchBar.text.toString().toLowerCase().trim()

                    // Make it the clear text icon
                    searchBar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.material_cancel_24, 0)

                    // In a larger application, would replace this with a class that holds constants
                    // and call it as FilteringOptions.LOCATION (which would be an int)
                    // and FilteringOptions.EMPLOYEE_NAME. But since this is a smaller application,
                    // comparing strings is fine (though worse performance wise)
                    if (filterOn.equals("Location"))
                    {
                        sendList = filterListLocation(currText)
                        updateList(sendList)
                    }
                    else if (filterOn.equals("Employee Name"))
                    {
                        sendList = filterListEmployee(currText)
                        updateList(sendList)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Maybe some more functionality here later?
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Maybe some more functionality here later?
            }

        })

        // This is only for the right end of the searchBar, where the cancel button is
        searchBar.setOnTouchListener { _ , event -> clearTextField(event) }
    }

    // This is how I know the EditText's right drawable is called
    // It gets where the user
    // The horizontal start of the right drawable is the right edge of EditText - the width of drawable
    fun clearTextField(event: MotionEvent?) : Boolean
    {
        if (event != null)
        {
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3

            if (event!!.action == MotionEvent.ACTION_DOWN)
            {
                if (event.rawX >= (searchBar.right - searchBar.compoundDrawables[DRAWABLE_RIGHT].bounds.width()))
                {
                    searchBar.text.clear()
                    return true
                }
            }
        }
        return false
    }

    // Sorts Location List by name. This function was needed as sortedWith returns a List
    // Another optimization : Sort on intialization of the Array (at the beginning) and assign
    // an int based on index in the sorted array
    // For each subsequent sort (on search), sort based on that integer
    fun sortList(arrayList: ArrayList<Location>) : ArrayList<Location>
    {
        var arrayList = arrayList.sortedWith(compareBy { it.name })
        var rArrList = ArrayList<Location>()

        rArrList.addAll(arrayList)
        return rArrList
    }

    fun filterListEmpty () : ArrayList<Location>
    {
        // Update sendList with all locations
        sendList.clear()
        for (location : Location in locationList)
        {
            // Prevent duplicates
            if (location !in sendList)
            {
                sendList.add(location)
            }
        }

        return sendList
    }

    fun filterListLocation (query : String) : ArrayList<Location>
    {
        // Empty list and add new
        sendList.clear()

        var currText = query.toLowerCase().trim()

        for (location: Location in locationList)
        {
            if (location.name.toLowerCase().trim().contains(currText))
            {
                sendList.add(location)
            }
        }

        return sendList
    }

    fun filterListEmployee (query: String) : ArrayList<Location>
    {
        // Empty list and add new
        sendList.clear()

        var currText = query.toLowerCase().trim()
        for (location : Location in locationList)
        {
            for (employee : Employee in location.employeeList)
            {
                if (employee.name.toLowerCase().trim().contains(currText))
                {
                    sendList.add(location)
                }
            }
        }
        return sendList
    }

    // This creates a new LocationAdapter and re-renders the ListView with a new list
    fun updateList(sendList : ArrayList<Location>)
    {
        var sortedList = sortList(sendList)

        locationAdapter = LocationAdapter(this@EmployeeSearchActivity, sortedList)
        recyclerView.setAdapter(locationAdapter)
    }

    // Creates an alert dialog to select the filtering option
    fun changeFilterCondition()
    {
        var dialogBuilder = AlertDialog.Builder(this@EmployeeSearchActivity)
        dialogBuilder.setTitle("How would you like to filter the list?")
            .setSingleChoiceItems(
                getFilteringOptions(), getFilteringOptions().indexOf(filterOn)
            ) { dialog , which ->
                filterOn = getFilteringOptions()[which]

                // This updates the text, which calls afterTextChanged() in the EditText listener,
                // Therefore updating the list
                searchBar.text = searchBar.text
                dialog.dismiss()
            }
            .create()
            .show()
    }

    // Can add job title if needed?
    fun getFilteringOptions() : Array<String>
    {
        return arrayOf("Location", "Employee Name")
    }
}