package com.ait.weatherreport

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ait.weatherreport.api.WeatherAPI
import com.ait.weatherreport.data.Base
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val KEY_DETAILS = "KEY_DETAILS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val data = intent.getStringExtra(KEY_DETAILS)

        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val weatherAPI = retrofit.create(WeatherAPI::class.java)
        val call = weatherAPI.getWeatherDetails(
            data!!,
            getString(R.string.metric),
            getString(R.string.app_id)
        )

        call.enqueue(object : Callback<Base> {
            override fun onFailure(call: Call<Base>, t: Throwable) {
                tvTemp.text = t.message
            }

            override fun onResponse(
                call: Call<Base>,
                response: Response<Base>
            ) {
                if (response.isSuccessful) {
                    tvCityName.text = response.body()?.name
                    tvTemp.text = getString(R.string.current_temp, response.body()?.main?.temp)
                    tvDescr.text = getString(
                        R.string.description,
                        response.body()?.weather?.get(0)?.description
                    )
                    tvMinTemp.text = getString(R.string.min_temp, response.body()?.main?.temp_min)
                    tvMaxTemp.text = getString(R.string.max_temp, response.body()?.main?.temp_max)

                    val sunriseTime = Date(response.body()!!.sys!!.sunrise!!.toLong() * 1000)
                    tvSunrise.text = getString(R.string.sunrise, sunriseTime)

                    val sunsetTime = Date(response.body()!!.sys!!.sunset!!.toLong() * 1000)
                    tvSunset.text = getString(R.string.sunset, sunsetTime)

                    tvHumidity.text = getString(R.string.humidity, response.body()?.main?.humidity)
                    tvPressure.text = getString(R.string.pressure, response.body()?.main?.pressure)

                    Glide.with(this@DetailsActivity)
                        .load(
                            (getString(R.string.img_url) +
                                    response.body()?.weather?.get(0)?.icon
                                    + getString(R.string.png))
                        )
                        .into(ivWeatherIcon)
                } else {
                    ivWeatherIcon.visibility = View.GONE
                    tvCityName.text = getString(R.string.city_not_found_text)
                }
            }
        })
    }

}
