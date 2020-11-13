package com.internshala.foodhub.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.internshala.foodhub.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodhub.adapter.AllRestaurantsAdapter
import com.internshala.foodhub.model.Restaurant
import com.internshala.foodhub.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject


class HomeFragment : Fragment() {

    private lateinit var recyclerRestaurant: RecyclerView
    private lateinit var AllRestaurantsAdapter: AllRestaurantsAdapter
    private var restaurantList = arrayListOf<Restaurant>()
    private lateinit var progressBar: ProgressBar
    private lateinit var progressLayout: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        progressBar = view?.findViewById(R.id.progressBar) as ProgressBar
        progressLayout = view.findViewById(R.id.progressLayout) as RelativeLayout
        progressLayout.visibility = View.VISIBLE

        setUpRecycler(view)

        return view
    }

    private fun setUpRecycler(view: View) {
        recyclerRestaurant = view.findViewById(R.id.recyclerRestaurants) as RecyclerView

        val queue = Volley.newRequestQueue(activity as Context)


        if (ConnectionManager().isNetworkAvailable(activity as Context)) {

            val FETCH_RESTAURANTS = "http://13.235.250.119/v2/restaurants/fetch_result"
            val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET,
                FETCH_RESTAURANTS,
                null,
                Response.Listener<JSONObject> { response ->
                    progressLayout.visibility = View.GONE

                    try {
                        val data = response.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {

                            val resArray = data.getJSONArray("data")
                            for (i in 0 until resArray.length()) {
                                val resObject = resArray.getJSONObject(i)
                                val restaurant = Restaurant(
                                    resObject.getString("id").toInt(),
                                    resObject.getString("name"),
                                    resObject.getString("rating"),
                                    resObject.getString("cost_for_one").toInt(),
                                    resObject.getString("image_url")
                                )
                                restaurantList.add(restaurant)
                                AllRestaurantsAdapter = AllRestaurantsAdapter(restaurantList, activity as Context)
                                val mLayoutManager = LinearLayoutManager(activity)
                                recyclerRestaurant.layoutManager = mLayoutManager
                                recyclerRestaurant.itemAnimator = DefaultItemAnimator()
                                recyclerRestaurant.adapter = AllRestaurantsAdapter
                                recyclerRestaurant.setHasFixedSize(true)


                            }
                        }
                    } catch (e: JSONException) {
                        if(activity != null) {
                            Toast.makeText(
                                activity as Context,
                                "Some unexpected error occurred!!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                Response.ErrorListener { error: VolleyError? ->
                    if(activity != null) {
                        Toast.makeText(activity as Context, error?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }) {

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "76f8f7efe45b29"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)

        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings"){ text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){ text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

    }

}
