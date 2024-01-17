package com.example.dashboardmobileapp.Activities


import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
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
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.Locale


class IncomeActivity : AppCompatActivity() {

    private var swipeRefreshLayout: CustomSwipeRefreshLayout? = null
    private var nestedScrollView: NestedScrollView? = null
    private var name: TextView? = null
    private var bottomNavigationView: BottomNavigationView? = null
    private var topToolView: MaterialToolbar? = null
    private var linearView: LinearLayout? = null
    private var lineChart: LineChart? = null
    private var header: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income)

        initView()
        startAPI()
        startAPI2()
        setButtons()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@IncomeActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    fun initView(){
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationViewIncome)
        topToolView = findViewById(R.id.materialToolbarIncome)
        linearView = findViewById(R.id.dataView)
        lineChart = findViewById(R.id.lineChartIncome)
        name = findViewById(R.id.profilIcon)
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val nameObject = preferences.getString("username","")
        val firstChar = nameObject?.substring(0, 1)?.uppercase(Locale.getDefault())
        val secondChar = nameObject?.substring(1,2)?.lowercase(Locale.getDefault())
        val result = firstChar + secondChar
        name!!.text = "$result"
        header = findViewById(R.id.headerIncome)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutIncome)
        nestedScrollView = findViewById(R.id.nestedScrollViewIncome)
        swipeRefreshLayout?.setNestedScrollView(nestedScrollView)
        swipeRefreshLayout?.setOnRefreshListener {
            refresh()
            val intent = Intent(this@IncomeActivity, IncomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
            swipeRefreshLayout?.isRefreshing = false
        }
    }

    fun setButtons(){
        bottomNavigationView?.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.incomeItem -> {startAPI()}

                R.id.homeItem -> {
                    startActivity(
                        Intent(
                            this@IncomeActivity,
                            MainActivity::class.java
                        ), ActivityOptions.makeSceneTransitionAnimation(this@IncomeActivity).toBundle()
                    )
                }
                R.id.addItem -> {
                    startActivity(
                        Intent(
                            this@IncomeActivity,
                            SearchActivity::class.java
                        ), ActivityOptions.makeSceneTransitionAnimation(this@IncomeActivity).toBundle()
                    )
                }

                R.id.expenseItem -> {
                    startActivity(
                        Intent(
                            this@IncomeActivity,
                            ExpenseActivity::class.java
                        ), ActivityOptions.makeSceneTransitionAnimation(this@IncomeActivity).toBundle()
                    )
                }
            }
            true

        }
        topToolView?.setOnMenuItemClickListener{item ->
            when(item.itemId){
                R.id.logoutItem -> {
                    val intent = Intent(this@IncomeActivity, LoginActivity::class.java)
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

        name?.setOnClickListener{
            startActivity(
                Intent(
                    this@IncomeActivity,
                    ProfileActivity::class.java
                ), ActivityOptions.makeSceneTransitionAnimation(this@IncomeActivity).toBundle()
            )
        }
    }

    fun startAPI() {
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        //Start API
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this@IncomeActivity)
        val url = "https://YOUR-DOMAIN/income"
        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener<String?> { response ->
                if (response != null) {
                    try {
                        // get Data
                        val arrayObject = JSONArray(response)
                        setMainPageData(arrayObject)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                }
            }, Response.ErrorListener { }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String>? {
                val headers: MutableMap<String, String> = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + preferences.getString("token", "")
                return headers
            }
        }
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
        //End API
    }

    fun startAPI2() {
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        //Start API
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this@IncomeActivity)
        val url = "https://YOUR-DOMAIN/sum"
        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener<String?> { response ->
                if (response != null) {
                    try {
                        // get Data
                        val arrayObject = JSONArray(response)
                        for (i in 0 until arrayObject.length()) {
                            val obj = arrayObject.getJSONObject(i)
                            if (String(obj.getString("title").toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8) == "SUMME EINKÜNFTE") {
                                setLineChart(obj)
                            }
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                }
            }, Response.ErrorListener { }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String>? {
                val headers: MutableMap<String, String> = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + preferences.getString("token", "")
                return headers
            }
        }
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
        //End API
    }

    private fun setLineChart(obj: JSONObject?) {
        val intArray: JSONArray = obj?.getJSONArray("yearValues") ?: JSONArray()
        val floatArray = FloatArray(intArray.length())
        for (i in 0 until intArray.length()) {
            floatArray[i] = intArray.getInt(i).toFloat()
        }

        val entries = java.util.ArrayList<Entry>()
        for (i in 0 until intArray.length()) {
            entries.add(Entry(i.toFloat(), floatArray[i]))
        }
        val dataSet = LineDataSet(entries, "Cash")
        dataSet.color = Color.GREEN // Set line color

        dataSet.lineWidth = 1f // Set line width

        dataSet.setCircleColor(Color.GREEN) // Set point color

        dataSet.circleRadius = 4f // Set point radius

        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER // Set line mode

        dataSet.setDrawValues(true) // Show values on points

        dataSet.valueTextColor = Color.WHITE
        val lineData = LineData(dataSet)
        lineChart!!.setTouchEnabled(false)
        lineChart!!.data = lineData
        lineChart!!.description.isEnabled = false
        lineChart!!.legend.isEnabled = false
        lineChart!!.animateX(1000)
        lineChart!!.xAxis.setDrawGridLines(false)
        val xAxis = lineChart!!.xAxis
        val rightYAxis = lineChart!!.axisRight
        val leftYAxis = lineChart!!.axisLeft
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
        leftYAxis.setDrawAxisLine(false)
        rightYAxis.isEnabled = false // Disable the right y-axis

        leftYAxis.textColor = Color.WHITE // Set the color of the left y-axis to white

        leftYAxis.isEnabled = true
        leftYAxis.setDrawLabels(false)
        lineChart!!.description.isEnabled = false // Disable the description label

        lineChart!!.xAxis.position = XAxis.XAxisPosition.BOTTOM // Set x-axis position to bottom

        lineChart!!.xAxis.granularity = 1f // Set the granularity to 1 to show all labels

        lineChart!!.xAxis.labelCount = 12 // Set the number of labels to display

        lineChart!!.invalidate()
    }

    @Throws(JSONException::class)
    fun setMainPageData(array: JSONArray?) {
        linearView?.removeAllViews()
        for (i in 0 until array?.length()!!) {
            val jsonObject = array.getJSONObject(i)
            val income = jsonObject.getDouble("sumValue")
            val percent = jsonObject.getDouble("percent")
            val title = String(jsonObject.getString("title").toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)

            val inflater = LayoutInflater.from(this)
            val view = inflater.inflate(R.layout.viewholder_datawallet, linearView, false)

            val titleView =  view.findViewById<TextView>(R.id.cryptoNameText)
            val valueView =  view.findViewById<TextView>(R.id.cryptoPriceText)
            val changeView =  view.findViewById<TextView>(R.id.changePercentText)

            titleView.text = title

            val symbols = DecimalFormatSymbols(Locale.GERMANY)
            val decimalFormat = DecimalFormat("#,###.00", symbols)
            val formattedNumber = decimalFormat.format(income)
            valueView.text = "$formattedNumber€"

            val formattedPercent = decimalFormat.format(percent)
            changeView.text = formattedPercent.toString()+"%"
            when{
                percent < 0 -> changeView.setTextColor(Color.RED)
                percent.toInt() == 0 -> changeView.setTextColor(Color.WHITE)
                percent > 0 -> changeView.setTextColor(Color.GREEN)
            }

            view.setOnClickListener {
                apiCall(title)
            }

            linearView?.addView(view)
        }

    }

    private fun apiCall(title: String) {
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        //Start API
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this@IncomeActivity)
        val url = "https://YOUR-DOMAIN/income/specific"
        val postRequest: StringRequest = @SuppressLint("ResourceType")
        object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response: String? ->
                try {
                    val jsonResponse = JSONObject(response)
                    setLineChart(jsonResponse)
                } catch (e: JSONException) {
                    throw RuntimeException(e)
                }
            },
            Response.ErrorListener { error: VolleyError? -> }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = java.util.HashMap()
                headers["Authorization"] = "Bearer " + preferences.getString("token", "")
                return headers
            }

            override fun getBody(): ByteArray {
                val body = "{\"income\": \"$title\"}"
                return body.toByteArray()
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        queue.add(postRequest)
    }

    private fun refresh() {
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        //Start API
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this@IncomeActivity)
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