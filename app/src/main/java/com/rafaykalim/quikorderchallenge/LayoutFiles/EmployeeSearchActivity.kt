package com.rafaykalim.quikorderchallenge.LayoutFiles

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.rafaykalim.quikorderchallenge.R

class EmployeeSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_search)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.employee_search_menu, menu)
        return true
    }

    fun add(x : Int, y : Int) : Int
    {
        return x+y
    }

}
