package com.ait.weatherreport

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.ait.weatherreport.adapter.CityAdapter
import com.ait.weatherreport.data.AppDatabase
import com.ait.weatherreport.data.City
import com.ait.weatherreport.touch.WeatherReyclerTouchCallback
import kotlinx.android.synthetic.main.activity_scrolling.*

class ScrollingActivity : AppCompatActivity(), CityDialog.CityHandler {

    companion object {
        const val TAG_DIALOG = "TAG_DIALOG"
    }

    lateinit var cityAdapter: CityAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)

        initRecyclerView()

        fab.setOnClickListener {
            showAddCityDialog()
        }
        fabDeleteAll.setOnClickListener {
            deleteAllCity()
        }

    }


    private fun initRecyclerView() {
        Thread {

            val cityList =
                AppDatabase.getInstance(this@ScrollingActivity).cityDao().getAllCity()

            runOnUiThread {
                cityAdapter = CityAdapter(this, cityList)
                recyclerCity.adapter = cityAdapter

                val itemDecoration = DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
                recyclerCity.addItemDecoration(itemDecoration)


                val callback = WeatherReyclerTouchCallback(cityAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerCity)
            }
        }.start()
    }

    private fun showAddCityDialog() {
        CityDialog().show(supportFragmentManager, TAG_DIALOG)
    }

    override fun cityCreated(item: City) {
        saveCity(item)
    }

    private fun deleteAllCity() {
        cityAdapter.deleteAllCity()
    }

    private fun saveCity(city: City) {
        Thread {
            val newId = AppDatabase.getInstance(this).cityDao().insertCity(
                city
            )
            city.cityId = newId

            runOnUiThread {
                cityAdapter.addCity(city)
            }
        }.start()
    }
}
