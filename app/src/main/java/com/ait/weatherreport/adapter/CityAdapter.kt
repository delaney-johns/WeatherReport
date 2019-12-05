package com.ait.weatherreport.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ait.weatherreport.DetailsActivity
import com.ait.weatherreport.DetailsActivity.Companion.KEY_DETAILS
import com.ait.weatherreport.R
import com.ait.weatherreport.ScrollingActivity
import com.ait.weatherreport.data.AppDatabase
import com.ait.weatherreport.data.City
import com.ait.weatherreport.touch.CityTouchHelperCallback
import kotlinx.android.synthetic.main.city_row.view.*
import java.util.*

class CityAdapter : RecyclerView.Adapter<CityAdapter.ViewHolder>, CityTouchHelperCallback {

    var cityList = mutableListOf<City>()

    val context: Context

    constructor(context: Context, listCities: List<City>) {
        this.context = context

        cityList.addAll(listCities)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cityRow = LayoutInflater.from(context).inflate(
            R.layout.city_row, parent, false
        )

        return ViewHolder(cityRow)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cityList.get(holder.adapterPosition)

        holder.tvCityName.text = city.name

        holder.btnDelete.setOnClickListener {
            deleteCity(holder.adapterPosition)
        }


        holder.btnInfo.setOnClickListener {
            val intentDetails = Intent()
            intentDetails.setClass(this.context, DetailsActivity::class.java)
            intentDetails.putExtra(KEY_DETAILS, holder.tvCityName.text.toString())
            context.startActivity(intentDetails)
        }
    }


    fun deleteCity(index: Int) {
        Thread {
            AppDatabase.getInstance(context).cityDao().deleteCity(cityList[index])
            (context as ScrollingActivity).runOnUiThread {
                cityList.removeAt(index)
                notifyItemRemoved(index)
            }
        }.start()
    }


    fun deleteAllCity() {
        Thread {
            AppDatabase.getInstance(context).cityDao().deleteAllCity()

            (context as ScrollingActivity).runOnUiThread {
                cityList.clear()
                notifyDataSetChanged()
            }
        }.start()
    }

    fun addCity(city: City) {
        cityList.add(city)
        notifyItemInserted(cityList.lastIndex)
    }

    override fun onDismissed(position: Int) {
        deleteCity(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(cityList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCityName = itemView.tvCity
        val btnDelete = itemView.btnDelete
        val btnInfo = itemView.btnInfo
    }
}

