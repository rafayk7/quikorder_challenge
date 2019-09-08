package com.rafaykalim.quikorderchallenge.LayoutFiles

import android.location.LocationListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.rafaykalim.quikorderchallenge.Controllers.LocationAdapter
import com.rafaykalim.quikorderchallenge.Models.Location
import com.rafaykalim.quikorderchallenge.R
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ExpandableListView
import com.rafaykalim.quikorderchallenge.Models.Employee
import org.json.JSONObject
import java.io.IOException


class EmployeeSearchActivity : AppCompatActivity() {
    lateinit var locationAdapter : LocationAdapter
    lateinit var recyclerView : ExpandableListView
    lateinit var searchBar : EditText
    lateinit var locationList : ArrayList<Location>
    lateinit var sendList : ArrayList<Location>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationList = convertEmployeeListToLocationList(getData())
        setContentView(R.layout.activity_employee_search)

        recyclerView = findViewById(R.id.activity_employee_search_location_recycler)
        searchBar = findViewById(R.id.activity_employee_search_bar)
        // Add data here

        Log.d("LMAO-2", locationList.toString())

        sendList = ArrayList()
        sendList.addAll(locationList)
        sendList = sortList(sendList)

        locationAdapter = LocationAdapter(this@EmployeeSearchActivity, sendList)
        recyclerView.setAdapter(locationAdapter)

        searchBar.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?) {
                // No Text, show all
                if (searchBar.text.isEmpty())
                {
                    for (location : Location in locationList)
                    {
                        if (location !in sendList)
                        {
                            sendList.add(location)
                        }
                    }
                    searchBar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.material_search_24, 0)
                    updateList(sendList)
//                    searchBar.isCursorVisible = false
                }
                else
                {
                    sendList.clear()
                    var currText = searchBar.text.toString().toLowerCase().trim()
                    Log.d("LMAO", currText)
                    Log.d("LMAO", locationList.toString())
                    for (location : Location in locationList)
                    {
                        if (location.name.toLowerCase().trim().contains(currText))
                        {
                            sendList.add(location)
                        }
                    }
                    searchBar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.material_cancel_24, 0)
                    updateList(sendList)
//                    searchBar.isCursorVisible = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        searchBar.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3

                if (event!!.action == MotionEvent.ACTION_DOWN)
                {
                    if (event.rawX >= (searchBar.getRight() - searchBar.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
                    {
                        searchBar.text.clear()
                        return true
                    }
                }
                else
                {

                }
                return false
            }
        })

    }

    fun sortList(arrayList: ArrayList<Location>) : ArrayList<Location>
    {
        var arrayList = arrayList.sortedWith(compareBy { it.name })
        var rArrList = ArrayList<Location>()

        rArrList.addAll(arrayList)
        return rArrList
    }
    fun updateList(sendList : ArrayList<Location>)
    {
        var sortedList = sortList(sendList)
        Log.d("LMAO-SEND", sortedList.toString())

        locationAdapter = LocationAdapter(this@EmployeeSearchActivity, sortedList)
        recyclerView.setAdapter(locationAdapter)
    }

    fun getJson(): JSONObject
    {
        var json: String? = null
        try {
            val file = assets.open("data.json")
            val size = file.available()
            val buffer = ByteArray(size)
            file.read(buffer)
            file.close()
            json = String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return JSONObject(json)
    }

    // Split into getJson() and getEmployeeList()
    fun getData() : ArrayList<Employee> {
        var inputJson = getJson()
        var empList = inputJson.getJSONArray("employees")
        var empObjList = ArrayList<Employee>()
        for(i in 0 until empList.length())
        {
            var employee = empList[i] as JSONObject
            var employeeLocations = Array<String>(employee.getJSONArray("locations").length()) {
                employee.getJSONArray("locations").getString(it)
            }

            var employeeObject = Employee(employee.getString("name"), employee.getString("title"), employeeLocations)
            empObjList.add(employeeObject)
        }

        return empObjList

        Log.d("LMAO", empObjList.toString())
    }

    // Convert Employee ArrayList to Locations ArrayList
    fun convertEmployeeListToLocationList(empList : ArrayList<Employee>) : ArrayList<Location>
    {
        // Create a HashMap of Location Name to Employee

        var map = HashMap<String, ArrayList<Employee>>()
        for (employee : Employee in empList)
        {
            for (loc : String in employee.locations)
            {
                if (map.containsKey(loc))
                {
                    Log.d("LMAO", loc)
                    map.get(loc)!!.add(employee)
                }
                else
                {
                    map.put(loc, arrayListOf(employee))
                }
            }
        }

        var rList = ArrayList<Location>()

        // Iterate through HashMap, create ArrayList of Location Objects
        for ((locName, employeeList) in map)
        {
            var backgroundImgId = getBackgroundImgId(locName)
            when (locName.trim().toLowerCase())
            {
                "austin" ->
                {
                    var location = Location(locName, employeeList, R.drawable.card_background_austin_vignette)
                    rList.add(location)
                }
                "chicago" ->
                {
                    var location = Location(locName, employeeList, R.drawable.card_background_chicago_vignette)
                    rList.add(location)
                }
                "new york" ->
                {
                    var location = Location(locName, employeeList, R.drawable.card_background_newyork_vignette)
                    rList.add(location)
                }
            }
        }
        return rList
    }
}

// Image follows naming convention "card_background_${city_name}"
fun getBackgroundImgId(locName : String) : String
{
    var locName = locName.toLowerCase().replace("\\s".toRegex(), "")
    return "R.drawable.card_background_$locName"
}
