package com.example.dashboardmobileapp.Activities

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
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
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.majorik.sparklinelibrary.SparkLineLayout
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.Locale

class SearchActivity : AppCompatActivity() {

    private var swipeRefreshLayout: CustomSwipeRefreshLayout? = null
    private var nestedScrollView: NestedScrollView? = null
    private var incomeText: TextView? = null
    private var expenseText: TextView? = null
    private var searchView: LinearLayout? = null
    private var setter: Int? = null
    private var name: TextView? = null
    private var bottomNavigationView: BottomNavigationView? = null
    private var topToolView: MaterialToolbar? = null
    private var searchbuttn: MaterialButton? = null

    var incomeNames = emptyArray<String>()
    var expenseNames = emptyArray<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initView()
        setButtons()
        startAPI3()
        startAPI4()
        callAPI()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@SearchActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    fun initView(){
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationViewSearch)
        topToolView= findViewById(R.id.materialToolbarSearch)
        incomeText = findViewById(R.id.incomeText)
        expenseText = findViewById(R.id.expenseText)
        searchView = findViewById(R.id.searchList)

        name = findViewById(R.id.profilIcon)
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val nameObject = preferences.getString("username","")
        val firstChar = nameObject?.substring(0, 1)?.uppercase(Locale.getDefault())
        val secondChar = nameObject?.substring(1,2)?.lowercase(Locale.getDefault())
        val result = firstChar + secondChar
        name!!.text = "$result"

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutSearch)
        nestedScrollView = findViewById(R.id.nestedScrollViewSearch)
        swipeRefreshLayout?.setNestedScrollView(nestedScrollView)
        swipeRefreshLayout?.setOnRefreshListener {
            refresh()
            val intent = Intent(this@SearchActivity, SearchActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
            swipeRefreshLayout?.isRefreshing = false
        }
    }

    fun callAPI(){
        if(setter == 0){startAPI1()}else{startAPI2()}
    }

    fun setButtons(){
        if (incomeText?.typeface?.isBold == true) {
            setter = 0
        } else {
            setter = 1
        }
        incomeText?.setOnClickListener{
            if (incomeText?.typeface?.isBold == true) {
                callAPI()
            } else {
                setter = 0
                incomeText?.setTypeface(null, Typeface.BOLD)
                incomeText?.setTextColor(Color.parseColor("#FFFFFF"))
                expenseText?.setTypeface(null, Typeface.NORMAL)
                expenseText?.setTextColor(Color.parseColor("#BFBFBF"))
                callAPI()
            }
        }
        expenseText?.setOnClickListener{
            if (incomeText?.typeface?.isBold == true) {
                setter = 1
                incomeText?.setTypeface(null, Typeface.NORMAL)
                incomeText?.setTextColor(Color.parseColor("#BFBFBF"))
                expenseText?.setTypeface(null, Typeface.BOLD)
                expenseText?.setTextColor(Color.parseColor("#FFFFFF"))
                callAPI()
            } else {
                callAPI()
            }
        }
        bottomNavigationView?.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.incomeItem -> {
                    startActivity(
                        Intent(
                            this@SearchActivity,
                            IncomeActivity::class.java
                        ), ActivityOptions.makeSceneTransitionAnimation(this@SearchActivity).toBundle()
                    )
                }

                R.id.homeItem -> {
                    startActivity(
                        Intent(
                            this@SearchActivity,
                            MainActivity::class.java
                        ), ActivityOptions.makeSceneTransitionAnimation(this@SearchActivity).toBundle()
                    )
                }
                R.id.addItem -> {callAPI()}

                R.id.expenseItem -> {
                    startActivity(
                        Intent(
                            this@SearchActivity,
                            ExpenseActivity::class.java
                        ), ActivityOptions.makeSceneTransitionAnimation(this@SearchActivity).toBundle()
                    )
                }
            }
            true

        }
        topToolView?.setOnMenuItemClickListener{item ->
            when(item.itemId){
                R.id.logoutItem -> {
                    val intent = Intent(this@SearchActivity, LoginActivity::class.java)
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
                    this@SearchActivity,
                    ProfileActivity::class.java
                ), ActivityOptions.makeSceneTransitionAnimation(this@SearchActivity).toBundle()
            )
        }
    }

    fun filterFunction(text: String){
        if(text == ""){
            callAPI()
        } else{
            if (setter==0){
                val filteredArray = incomeNames.filter { it.contains(text) }.toTypedArray()
                setfilteredData(filteredArray)
            } else {
                val filteredArray = expenseNames.filter { it.contains(text) }.toTypedArray()
                setfilteredData(filteredArray)
            }
        }
    }

    private fun setfilteredData(filteredArray: Array<String>) {
        //TODO
    }

    fun startAPI1(){
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        //Start API
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this@SearchActivity)
        val url = "https://YOUR-DOMAIN/income/searchpage"
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

    @Throws(JSONException::class)
    fun setMainPageData(array: JSONArray?){
        searchView?.removeAllViews()
        for (i in 0 until array?.length()!!) {
            val jsonObject = array.getJSONObject(i)
            val sumValue = jsonObject.getDouble("sumValue")
            val title = String(jsonObject.getString("title").toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
            var saved = jsonObject.getBoolean("saved")
            val type = jsonObject.getString("typeOfValue")
            val percent = jsonObject.getDouble("percent")
            val lineData = ArrayList<Int>()
            val intArray = jsonObject.getJSONArray("lastThreeValues")
            for (m in 0 until intArray.length()) {
                lineData.add(intArray.getInt(m))
            }
            // Inflate the view for each item
            val inflater = LayoutInflater.from(this)
            val view = inflater.inflate(R.layout.viewholder_searchwallet, searchView, false)

            // Populate the view with data
            val cryptoNameText = view.findViewById<TextView>(R.id.addNameText)
            val cryptoPriceText = view.findViewById<TextView>(R.id.addPriceText)
            val changePercentText = view.findViewById<TextView>(R.id.addchangePercentText)
            val lineChart = view.findViewById<SparkLineLayout>(R.id.addSparkLineLayout)
            val button = view.findViewById<ImageView>(R.id.addView)

            cryptoNameText.text = title

            val symbols = DecimalFormatSymbols(Locale.GERMANY)
            val decimalFormat = DecimalFormat("#,###.00", symbols)
            val formattedNumber = decimalFormat.format(sumValue)
            cryptoPriceText.text = formattedNumber.toString() + "€"

            val formattedPercent = decimalFormat.format(percent)
            changePercentText.text = formattedPercent.toString() + "%"
            when{
                percent > 0 -> changePercentText.setTextColor(Color.GREEN)
                percent.toInt() == 0 -> changePercentText.setTextColor(Color.WHITE)
                percent < 0 -> changePercentText.setTextColor(Color.RED)
            }
            lineChart.setData(lineData)
            if(saved){
                button.setImageResource(R.drawable.check)
            }else{
                button.setImageResource(R.drawable.add)
            }
            button.setOnClickListener{
                if(saved){
                    button.setImageResource(R.drawable.add)
                    saved = false
                    apiCall(type,title)
                } else{
                    button.setImageResource(R.drawable.check)
                    saved = true
                    apiCall(type,title)
                }
            }

            searchView?.addView(view)
        }
    }

    fun apiCall(typeCall:String, titleCall:String) {
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val url = "https://YOUR-DOMAIN/startpage/favourites/change"
        val queue = Volley.newRequestQueue(this@SearchActivity)
        val postRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response: String? -> },
            Response.ErrorListener { error: VolleyError? -> }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = java.util.HashMap()
                headers["Authorization"] = "Bearer " + preferences.getString("token", "")
                return headers
            }
            override fun getBody(): ByteArray {
                val body = "{\"title\": \"$titleCall\"" +","+
                        "\"type\": \"$typeCall\"}"
                return body.toByteArray()
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        queue.add(postRequest)
    }

    fun startAPI2(){
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        //Start API
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this@SearchActivity)
        val url = "https://YOUR-DOMAIN/expenses/searchpage"
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

    fun startAPI3(){
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        //Start API
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this@SearchActivity)
        val url = "https://YOUR-DOMAIN/expenses/names"
        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener<String?> { response ->
                if (response != null) {
                    try {
                        // get Data
                        val arrayObject = JSONObject(response)
                        val array = arrayObject.getJSONArray("title")
                        for (i in 0 until array.length()){
                            expenseNames +=  String(array.get(i).toString().toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
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
    fun startAPI4(){
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        //Start API
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this@SearchActivity)
        val url = "https://YOUR-DOMAIN/income/names"
        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener<String?> { response ->
                if (response != null) {
                    try {
                        // get Data
                        val arrayObject = JSONObject(response)
                        val array = arrayObject.getJSONArray("title")
                        for (i in 0 until array.length()){
                            incomeNames +=  String(array.get(i).toString().toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
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
    private fun refresh() {
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        //Start API
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this@SearchActivity)
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