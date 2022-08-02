package com.hfad.geoapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.IBinder
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.hfad.geoapp.databinding.FragmentMapsBinding
import com.hfad.geoapp.viewmodels.PointViewModel
import com.hfad.geoapp.viewmodels.PointViewModelFactory
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


class MapsFragment : Fragment() {

    companion object {
        var LATITUDE: String? = "latitude"
        var LONGITUDE: String? = "longitude"
        const val NOTIFICATION_CHANNEL_ID = "6"
    }

    private var latitude = 0.0F
    private var longitude = 0.0F
    private var myLatitude = 0.0
    private var myLongitude = 0.0
    private lateinit var locationService: LocationService
    //private lateinit var connection: ServiceConnection
    private var mBound: Boolean = false
    private var isServiceRunning = false
    private lateinit var serviceIntent: Intent
    private var done = false

    private val viewModel: PointViewModel by activityViewModels {
        PointViewModelFactory(
            (activity?.application as GeoApplication).database.pointDao()
            , (activity?.application as GeoApplication).database.settingsDao()
        )
    }

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private var marker: Marker? = null

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(53.0,25.0),
            5F))
        googleMap.setOnMapClickListener { point ->
            googleMap.clear()
            marker = googleMap.addMarker(MarkerOptions().position(point).draggable(true))
        }
        googleMap.setOnCameraMoveStartedListener {
           // checkDistance()
        }
        if (latitude != 0.0F && longitude  != 0.0F) {
            val coordinates = LatLng(latitude.toDouble(), longitude.toDouble())
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15F))
            marker = googleMap.addMarker(MarkerOptions()
                .position(coordinates)
                .draggable(true)
                .title("Point")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
        }

    }

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.forEach { actionMap ->
                if(actionMap.key == Manifest.permission.ACCESS_COARSE_LOCATION ||
                    actionMap.key == Manifest.permission.ACCESS_FINE_LOCATION ||
                    actionMap.key == Manifest.permission.ACCESS_BACKGROUND_LOCATION) {
                    //Toast.makeText(context,"permission succeeded",Toast.LENGTH_SHORT).show()
                } else {
                    Log.i("DEBUG", "permission denied")
                    Toast.makeText(context,"permission denied",Toast.LENGTH_SHORT).show()
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
        return false
    }

    private fun showDialog(latitude: Double, longitude: Double){
        val builder: AlertDialog.Builder? = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle("New Point")
        val input = EditText(context)
        input.hint = "Enter name of the point"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder?.setView(input)
        builder?.setPositiveButton("OK") { _, _ ->
            val name = input.text.toString()
            if (viewModel.getId(name) == null) {
                val geocoder = Geocoder(context, Locale.getDefault())
                try {
                    val addresses = geocoder.getFromLocation(latitude,longitude, 1)
                    val address: String = addresses[0].getAddressLine(0)
                    done = if (addresses[0].getAddressLine(0).isEmpty()) {
                        val action = MapsFragmentDirections.actionMapsFragmentToPointsFragment()
                        view?.findNavController()?.navigate(action)
                        viewModel.insertPoint(name, latitude, longitude, "$latitude\n$longitude")
                        true
                    } else {
                        val action = MapsFragmentDirections.actionMapsFragmentToPointsFragment()
                        view?.findNavController()?.navigate(action)
                        viewModel.insertPoint(name, latitude, longitude, address)
                        true
                    }
                }
                catch (e:Exception){
                   Toast.makeText(context,"Choose another point",
                       Toast.LENGTH_SHORT)
                        .show()
                }

            }
            else {
                Toast.makeText(context,"Point $name is already exits",
                    Toast.LENGTH_SHORT).show()
            }
        }
        builder?.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder?.show()

    }

    private fun createNotification(name: String, lat: Double, lon: Double) {
        val notificationManager =
            activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "My Notifications",
                NotificationManager.IMPORTANCE_MAX
            )
            notificationChannel.description = "Geo Channel"
            notificationChannel.enableLights(true)
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager!!.createNotificationChannel(notificationChannel)
        }
        val resultIntent = Intent(context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .putExtra("latitude",lat.toFloat())
            .putExtra("longitude",lon.toFloat())
        val resultPendingIntent = PendingIntent.getActivity(
            context, 0, resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context!!, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.baseline_place_black_24)
            .setContentTitle("You're reaching point $name")
            .setContentText("Tap to open the map")
            .setContentIntent(resultPendingIntent)
            notificationManager!!.notify(1, notificationBuilder.build())


    }

    private fun distanceInMetres(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(deg2rad(theta))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515 * 1000 * 1.609344
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    private fun checkDistance(){
        val frequency = viewModel.getFrequency().toLong()*60000
        if (mBound){
            locationService.LocalBinder().setFrequency(frequency)
        }
        object : CountDownTimer(Long.MAX_VALUE,frequency) {
            override fun onTick(millisUntilFinished: Long) {
                if (mBound) {
                    myLatitude = locationService.LocalBinder().getLat()
                    myLongitude = locationService.LocalBinder().getLon()
                }
                lifecycle.coroutineScope.launch {
                    viewModel.allPoints().collect { list ->
                        val requiredDistance = viewModel.getDistance()
                        val distance = FloatArray(list.size) { 0.0F }
                        val distMap = mutableMapOf<String, Float>()
                        var i = 0
                        list.forEach {
                            distance[i] = distanceInMetres(
                                it.latitude, it.longitude,
                                myLatitude, myLongitude
                            ).toFloat()
                            distMap[it.name] = distance[i]
                            i++
                        }
                        for (d in distMap) {
                            if (d.value <= requiredDistance!!.toFloat()) {
                                createNotification(
                                    d.key,
                                    viewModel.getCoordinates(d.key).latitude,
                                    viewModel.getCoordinates(d.key).longitude
                                )

                                break
                            }
                        }
                    }
                }
            }
            override fun onFinish() {
                Toast.makeText(context,"Open GeoApp",Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun stopService(){
        locationService = LocationService()
        serviceIntent = Intent(context, locationService.javaClass)
        if (isServiceRunning) {
            isServiceRunning = false
            activity?.stopService(serviceIntent)
            //Toast.makeText(context, "service stopped", Toast.LENGTH_SHORT).show()
        } else {
            //Toast.makeText(context, "service is already stopped", Toast.LENGTH_SHORT).show()
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as LocationService.LocalBinder
            locationService = binder.getService()
            mBound = true
            locationService.LocalBinder().setFrequency(viewModel.getFrequency().toLong()*60000)
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latitude = it.getFloat(LATITUDE)
            longitude = it.getFloat(LONGITUDE)
        }
        requestPermissions.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        )
        done = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.getDistance() == null)
            viewModel.addSettings(100,1)
        (activity as AppCompatActivity).supportActionBar?.title = "Map"
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        binding.bottomNavigation.selectedItemId = R.id.map_item
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.map_item -> {
                    //activity?.startService(Intent(context,LocationService::class.java))
                    true
                }
                R.id.points_item -> {
                    val action = MapsFragmentDirections.actionMapsFragmentToPointsFragment()
                    view.findNavController().navigate(action)
                    true
                }
                R.id.settings_item -> {
                    val action = MapsFragmentDirections.actionMapsFragmentToSettingsFragment()
                    view.findNavController().navigate(action)
                    true
                }
                else -> false
            }
        }
        binding.addPointButton.setOnClickListener{
            val latitude = marker?.position?.latitude
            val longitude = marker?.position?.longitude
            if (marker == null) {
                Toast.makeText(context,"Choose point on the map",
                    Toast.LENGTH_SHORT).show()
            } else{
                lifecycle.coroutineScope.launch {
                    viewModel.allPoints().collect { list ->
                        val requiredDistance = viewModel.getDistance()
                        val distance = FloatArray(list.size) { 0.0F }
                        val distMap = mutableMapOf<String, Float>()
                        var i = 0
                        list.forEach{
                            distance[i] = distanceInMetres(it.latitude,it.longitude,
                                latitude!!,longitude!!).toFloat()
                            distMap[it.name] = distance[i]
                            i++
                        }
                        i = 0
                        for(d in distMap){
                            if (d.value <= requiredDistance!!.toFloat())
                                i++
                        }
                        if (!done){
                            if (i > 0)
                                Toast.makeText(context,"Too close to another point",
                                    Toast.LENGTH_SHORT)
                                    .show()
                            else
                                showDialog(latitude!!, longitude!!)
                        }
                    }
                }
            }


        }
        if (context?.let { it1 -> isOnline(it1) } == false) {
            Toast.makeText(context,"Check your internet connection",
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(context, LocationService::class.java).also { intent ->
            context?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        checkDistance()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}