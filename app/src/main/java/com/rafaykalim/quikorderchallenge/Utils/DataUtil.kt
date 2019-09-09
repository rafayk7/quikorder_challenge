package com.rafaykalim.quikorderchallenge.Utils

import android.content.Context
import android.util.Log
import com.rafaykalim.quikorderchallenge.Models.Employee
import com.rafaykalim.quikorderchallenge.Models.Location
import com.rafaykalim.quikorderchallenge.R
import org.json.JSONObject
import java.io.IOException

class DataUtil(val context : Context)
{

    fun getJson(): JSONObject
    {
        var json: String? = null
        try {
            val file = context.assets.open("data.json")
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

    fun getEmployeeList() : ArrayList<Employee> {
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

            for (location : Location in rList)
            {
                location.employeeList = sortEmployeeList(location.employeeList)
            }
        }
        Log.d("LMAO", rList.toString())
        return rList
    }

    fun sortEmployeeList(empList : ArrayList<Employee>) : ArrayList<Employee>
    {
        var returnArray = ArrayList<Employee>()
        var empList = empList.sortedWith(compareBy { it.name })
        returnArray.addAll(empList)

        return returnArray
    }

    // Image follows naming convention "card_background_${city_name}"
    fun getBackgroundImgId(locName : String) : String {
        var locName = locName.toLowerCase().replace("\\s".toRegex(), "")
        return "R.drawable.card_background_$locName"
    }
}
