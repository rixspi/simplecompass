package com.github.rixspi.simplecompass.ui.compass

import android.content.Context.SENSOR_SERVICE
import android.databinding.DataBindingUtil
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.rixspi.simplecompass.R
import com.github.rixspi.simplecompass.databinding.FragmentCompassBinding
import com.github.rixspi.simplecompass.di.compass.CompassModule
import com.github.rixspi.simplecompass.ui.base.BaseFragment
import javax.inject.Inject
import android.view.animation.Animation
import android.view.animation.RotateAnimation


class CompassFragment : BaseFragment(), CompassViewAccess, SensorEventListener {
    private lateinit var binding: FragmentCompassBinding

    @Inject
    lateinit var viewModel: CompassViewModel

    private lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.plus(CompassModule(this)).inject(this)

        sensorManager = activity.getSystemService(SENSOR_SERVICE) as SensorManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compass, null, false)
        binding.model = viewModel
        binding.viewAccess = this

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //no op
    }

    private var currentDegree: Double = 0.0

    var orientation = FloatArray(3)
    var rMat = FloatArray(9)


    override fun onSensorChanged(event: SensorEvent) {

        // get the angle around the z-axis rotated
        var degree: Double = 0.0



        if( event.sensor.type == Sensor.TYPE_ROTATION_VECTOR ){
            // calculate th rotation matrix
            SensorManager.getRotationMatrixFromVector( rMat, event.values );
            // get the azimuth value (orientation[0]) in degree
            degree =  ( Math.toDegrees( SensorManager.getOrientation( rMat, orientation )[0].toDouble() ) + 360 ) % 360
        }


        // create a rotation animation (reverse turn degree degrees)
        val ra = RotateAnimation(
                currentDegree.toFloat(),
                -degree.toFloat(),
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f)

        // how long the animation will take place
        ra.duration = 210

        // set the animation after the end of the reservation status
        ra.fillAfter = true

        // Start the animation
        binding.needle.startAnimation(ra)
        currentDegree = -degree
    }

}