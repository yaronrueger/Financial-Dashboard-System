package com.example.dashboardmobileapp.Activities

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import android.icu.text.NumberFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dashboardmobileapp.Adapter.CustomSwipeRefreshLayout
import com.example.dashboardmobileapp.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.majorik.sparklinelibrary.SparkLineLayout
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private var swipeRefreshLayout: CustomSwipeRefreshLayout? = null
    private var nestedScrollView: NestedScrollView? = null
    private var favoritesList: LinearLayout? = null
    private var bottomNavigationView: BottomNavigationView? = null
    private var topToolView: MaterialToolbar? = null
    private var name: TextView? = null
    private var cashYear: TextView? = null
    private var refreshButtn: ImageView? = null
    var textReload: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        startAPI()
        startAPI2()
        startAPI3()
        setButtons()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.remove("token")
        editor.remove("username")
        editor.apply()
        startActivity(intent)
        finish()
    }

    private fun startAPI3() {
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            //Start API
            // Instantiate the RequestQueue
            val queue = Volley.newRequestQueue(this@MainActivity)
            val url = "https://YOUR-DOMAIN/startpage/favourites"
            // Request a string response from the provided URL.
            val stringRequest: StringRequest = object : StringRequest(
                Method.GET, url,
                Response.Listener<String?> { response ->
                    if (response != null) {
                        try {
                            // get Data
                            val jsonArray = JSONArray(response)
                            setFavorites(jsonArray)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                    }
                }, Response.ErrorListener { }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): MutableMap<String, String>? {
                    val headers: HashMap<String, String> = HashMap<String, String>()
                    headers["Authorization"] = "Bearer " + preferences.getString("token", "")
                    return headers
                }
            }
            // Add the request to the RequestQueue.
            queue.add(stringRequest)
            //End API
    }

    fun setFavorites(array: JSONArray?){
        favoritesList?.removeAllViews()
        for(i in 0 until array?.length()!!){
            val jsonObject = array.getJSONObject(i)
            val type = jsonObject.getString("typeOfValue")
            val title = String(jsonObject.getString("title").toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
            val sumValue = jsonObject.getDouble("sumValue")
            val percent = jsonObject.getDouble("percent")
            val lineData = ArrayList<Int>()
            val intArray = jsonObject.getJSONArray("lastThreeValues")
            for (m in 0 until intArray.length()) {
                lineData.add(intArray.getInt(m))
            }

            // Inflate the view for each item
            val inflater = LayoutInflater.from(this)
            val view = inflater.inflate(R.layout.viewholder_wallet, favoritesList, false)

            // Populate the view with data
            val cryptoNameText = view.findViewById<TextView>(R.id.cryptoNameText)
            val cryptoPriceText = view.findViewById<TextView>(R.id.cryptoPriceText)
            val changePercentText = view.findViewById<TextView>(R.id.changePercentText)
            val lineChart = view.findViewById<SparkLineLayout>(R.id.sparkLineLayout)
            val logo = view.findViewById<ImageView>(R.id.logoImg)

            cryptoNameText.text = title


            val symbols = DecimalFormatSymbols(Locale.GERMANY)
            val decimalFormat = DecimalFormat("#,###.00", symbols)
            val formattedNumber = decimalFormat.format(sumValue)
            cryptoPriceText.text = formattedNumber.toString() + "€"
            when{
                percent < 0 -> changePercentText.setTextColor(Color.RED)
                percent.toInt() == 0 -> changePercentText.setTextColor(Color.WHITE)
                percent > 0 -> changePercentText.setTextColor(Color.GREEN)
            }
            val formattedPercent = decimalFormat.format(percent)
            changePercentText.text = formattedPercent.toString() + "%"
            // Set data to LineChart
            lineChart.setData(lineData)
            if(type == "income"){
                logo.setImageResource(R.drawable.income)
            }else{
                logo.setImageResource(R.drawable.expense)
            }
            // Add the view to the LinearLayout
            favoritesList?.addView(view)

        }
    }

    private fun setButtons() {
        bottomNavigationView?.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.incomeItem -> {
                    startActivity(
                        Intent(
                            this@MainActivity,
                            IncomeActivity::class.java
                        ), ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle()
                    )
                }

                R.id.homeItem -> {startAPI()}
                R.id.addItem -> {
                    startActivity(
                        Intent(
                            this@MainActivity,
                            SearchActivity::class.java
                        ), ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle()
                    )
                }

                R.id.expenseItem -> {
                    startActivity(
                        Intent(
                            this@MainActivity,
                            ExpenseActivity::class.java
                        ), ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle()
                    )
                }
            }
            true

        }
        topToolView?.setOnMenuItemClickListener{item ->
            when(item.itemId){
                R.id.logoutItem -> {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    val editor = preferences.edit()
                    editor.remove("token")
                    editor.remove("username")
                    editor.apply()
                    startActivity(intent)
                    finish()
                }
            }
            true
        }
        swipeRefreshLayout?.setOnRefreshListener {
            // Your code to refresh the content goes here
            refresh()
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
            // Once the refresh is complete, call this method to hide the progress indicator
            swipeRefreshLayout?.isRefreshing = false
        }

        name?.setOnClickListener{
            startActivity(
                Intent(
                    this@MainActivity,
                    ProfileActivity::class.java
                ), ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle()
            )
        }
    }

    fun startAPI() {
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        //Start API
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this@MainActivity)
        val url = "https://YOUR-DOMAIN/startpage"
        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener<String?> { response ->
                if (response != null) {
                    try {
                        // get Data
                        val jsonObject = JSONObject(response)
                        setMainPageData(jsonObject)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                }
            }, Response.ErrorListener { }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String>? {
                val headers: HashMap<String, String> = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + preferences.getString("token", "")
                return headers
            }
        }
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
        //End API
    }

    @Throws(JSONException::class)
    fun setLineChart(`object`: JSONObject) {
        val intArray = `object`.getJSONArray("yearValues")
        val floatArray = FloatArray(intArray.length())

        for (i in 0 until intArray.length()) {
            floatArray[i] = intArray.getInt(i).toFloat()// Convert each integer to float and store in the float array
        }

        val lineChart = findViewById<LineChart>(R.id.lineChart)
        val entries = ArrayList<Entry>()
        for (i in 0 until intArray.length()) {
            entries.add(Entry(i.toFloat(), floatArray[i]))
        }

        // Add more entries as needed
        val dataSet = LineDataSet(entries, "Cash")
        dataSet.color = Color.GREEN // Set line color
        dataSet.lineWidth = 1f // Set line width
        dataSet.setCircleColor(Color.GREEN) // Set point color
        dataSet.circleRadius = 4f // Set point radius
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER // Set line mode
        dataSet.setDrawValues(true) // Show values on points
        dataSet.valueTextColor = Color.WHITE
        val lineDataL = LineData(dataSet)
        lineChart.setTouchEnabled(false)
        lineChart.data = lineDataL
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.animateX(1000)
        lineChart.xAxis.setDrawGridLines(false)
        val xAxis = lineChart.xAxis
        val rightYAxis = lineChart.axisRight
        val leftYAxis = lineChart.axisLeft
        xAxis.valueFormatter = IndexAxisValueFormatter(
            arrayOf(
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec"
            )
        ) // Set custom labels for the x-axis
        xAxis.textColor = Color.WHITE // Set the color of the x-axis to white
        xAxis.setDrawGridLines(false)
        //xAxis.setDrawLabels(false);
        leftYAxis.setDrawAxisLine(false)
        rightYAxis.isEnabled = false // Disable the right y-axis
        leftYAxis.textColor = Color.WHITE // Set the color of the left y-axis to white
        leftYAxis.isEnabled = true
        leftYAxis.setDrawLabels(false)
        lineChart.description.isEnabled = false // Disable the description label
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM // Set x-axis position to bottom
        lineChart.xAxis.granularity = 1f // Set the granularity to 1 to show all labels
        lineChart.xAxis.labelCount = 12 // Set the number of labels to display
        lineChart.invalidate()
    }

    @Throws(JSONException::class)
    fun setMainPageData(`object`: JSONObject) {
        val number = `object`.getDouble("cashYear")
        val symbols = DecimalFormatSymbols(Locale.GERMANY)
        val decimalFormat = DecimalFormat("#,###.00", symbols)
        val formattedNumber = decimalFormat.format(number)
        cashYear!!.text = "" + formattedNumber + "€"
    }

    fun startAPI2() {
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        //Start API
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this@MainActivity)
        val url = "https://YOUR-DOMAIN/cash/yearValues"
        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener<String?> { response ->
                if (response != null) {
                    try {
                        // get Data
                        val jsonObject = JSONObject(response)
                        setLineChart(jsonObject)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                }
            }, Response.ErrorListener { }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String>? {
                val headers: HashMap<String, String> = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + preferences.getString("token", "")
                return headers
            }
        }
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
        //End API
    }


    private fun initView() {
        name = findViewById(R.id.profilIcon)
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationViewMain)
        topToolView= findViewById(R.id.materialToolbarMain)
        favoritesList = findViewById(R.id.favoritesList)
        cashYear = findViewById(R.id.cashYear)
        textReload = findViewById(R.id.favoritesText)
        refreshButtn = findViewById(R.id.refreshButton)

        textReload?.setOnClickListener(){
            startAPI3()
        }
        refreshButtn?.setOnClickListener(){
            startAPI3()
        }
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val nameObject = preferences.getString("username","")
        val firstChar = nameObject?.substring(0, 1)?.uppercase(Locale.getDefault())
        val secondChar = nameObject?.substring(1,2)?.lowercase(Locale.getDefault())
        val result = firstChar + secondChar
        name!!.text = "$result"

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutMain)
        nestedScrollView = findViewById(R.id.nestedScrollViewMain)
        swipeRefreshLayout?.setNestedScrollView(nestedScrollView)

    }

    private fun refresh() {
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        //Start API
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this@MainActivity)
        val url = "https://YOUR-DOMAIN/refresh"
        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener<String?> { response ->
                if (response != null) {
                    try {
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                }
            }, Response.ErrorListener { }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String>? {
                val headers: HashMap<String, String> = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + preferences.getString("token", "")
                return headers
            }
        }
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
        //End API
    }
}
