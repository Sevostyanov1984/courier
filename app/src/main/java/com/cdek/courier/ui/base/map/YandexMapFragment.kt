package com.cdek.courier.ui.base.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.cdek.courier.R
import com.cdek.courier.data.models.entities.task.Task
import com.cdek.courier.ui.base.tasklist.TaskListViewModel
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.map.*
import java.util.ArrayList
import javax.inject.Inject

class YandexMapFragment : DaggerFragment(), UserLocationObjectListener,
    DrivingSession.DrivingRouteListener, View.OnClickListener {


    //private val MAPKIT_API_KEY = "2eea2941-7f00-4924-a10e-95455c43966a" // мой
    //private val MAPKIT_API_KEY = "e0187351-1bc8-4470-a913-43c3d557410c" // мой
    private val MAPKIT_API_KEY = "b1562268-4295-46f9-90e3-7cf17828e8c5" // ванин
    //private val MAPKIT_API_KEY = "2d83dc33-57bf-49f4-b10e-ebad1fe53974" // рабочий(левый)

    private var mapView: MapView? = null
    private var userLocationLayer: UserLocationLayer? = null

    lateinit var mapObjects: MapObjectCollection
    private var drivingRouter: DrivingRouter? = null
    private var drivingSession: DrivingSession? = null

    lateinit var TARGET_LOCATION: Point
    lateinit var START_LOCATION: Point//(55.733330, 85.587649)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val taskListViewModel by lazy {
        ViewModelProviders.of(
            this@YandexMapFragment,
            viewModelFactory
        )[TaskListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(this.context)
        val view = inflater.inflate(R.layout.map, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = view.findViewById<View>(R.id.mapview) as MapView

        current.setOnClickListener(this)
        plus.setOnClickListener(this)
        minus.setOnClickListener(this)

        mapView?.map?.isRotateGesturesEnabled = false
        mapView?.map?.isZoomGesturesEnabled = true
        mapObjects = mapView!!.getMap().getMapObjects()


        //val lat = intent.extras!!.getString("lat") // "ЗАВОДСКАЯ, д.3, оф.-"
        //val lon = intent.extras!!.getString("lon") // "Новосибирск"
        //val taskList: List<Task> = intent.extras!!.getSerializable("taskList") as List<Task> // "Новосибирск"


        //TARGET_LOCATION = Point(lat?.toDouble()!!, lon?.toDouble()!!)


        // если тип карты "list" то выводим все точки
        val lat =
            arguments?.get("mapType")// extras!!.getString("maptype") // "ЗАВОДСКАЯ, д.3, оф.-"

        if ("list".equals(lat)) {
            taskListViewModel.getTasks().observe(this@YandexMapFragment, Observer {
                if (it.isNotEmpty()) {

                    // в список добавляем точки с координатами
                    var pointList: ArrayList<Point> = ArrayList()
                    for (task: Task in it) {
                        val lat = task.receiverAddress?.latitude
                        val lon = task.receiverAddress?.longitude

                        if (lat != null && lon != null) {
                            pointList.add(Point(lat.toDouble(), lon.toDouble()))
                        }
                    }
                    mapObjects.addPlacemarks(
                        pointList,
                        ImageProvider.fromResource(this.context, R.drawable.search_result),
                        IconStyle()
                    )

                    // переносимся к первой точке из списка
                    if (!pointList.isEmpty()) {

                        //mapView?.getMap()?.move(CameraPosition(pointList.get(0), 14f, 0f, 0f))

                        val boundingBox = BoundingBox(
                            pointList.get(0),
                            pointList.get(pointList.size - 1)
                        ) // getting BoundingBox between two points
                        var cameraPosition = mapView!!.map.cameraPosition(boundingBox) // getting cameraPosition
                        cameraPosition = CameraPosition(
                            cameraPosition.target,
                            cameraPosition.zoom - 0.8f,
                            cameraPosition.azimuth,
                            cameraPosition.tilt
                        ) // Zoom 80%
                        mapView!!.map.move(
                            cameraPosition,
                            Animation(Animation.Type.SMOOTH, 0f),
                            null
                        ) // move camera
                    }


                } else {
                    taskListViewModel.getAll()
                }
            })
            // если тип карты "route" то выводим маршрут до переданной точки
        } else {
            TARGET_LOCATION = Point(54.963518, 82.936612)
            START_LOCATION = Point(54.863965, 83.104700)

            mapObjects.addPlacemark(
                TARGET_LOCATION,
                ImageProvider.fromResource(this.context, R.drawable.search_result)
            )

            mapView?.getMap()?.move(CameraPosition(TARGET_LOCATION, 14f, 0f, 0f)
                , Animation(Animation.Type.SMOOTH, 2f)
                , Map.CameraCallback() {
                    val mapKit = MapKitFactory.getInstance()
                    userLocationLayer = mapKit.createUserLocationLayer(mapView?.getMapWindow()!!)
                    userLocationLayer?.isVisible = true
                    userLocationLayer?.setHeadingEnabled(true)
                    userLocationLayer?.setObjectListener(this@YandexMapFragment)
                })
        }

    }

    override fun onStop() {
        // Activity onStop call must be passed to both MapView and MapKit instance.
        mapView!!.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        // Activity onStart call must be passed to both MapView and MapKit instance.
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView!!.onStart()
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
    }

    override fun onObjectRemoved(userLocationView: UserLocationView) {
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {

        mapObjects.addPlacemark(
            START_LOCATION,
            ImageProvider.fromResource(this.context, R.drawable.user_arrow)
        )
        /*userLocationLayer?.setAnchor(
            PointF((mapView?.getWidth()!! * 0.5).toFloat(), (mapView?.getHeight()!! * 0.5).toFloat()),
            PointF((mapView?.getWidth()!! * 0.5).toFloat(), (mapView?.getHeight()!! * 0.83).toFloat())
        )*/

        /*START_LOCATION = Point(userLocationView.getArrow().geometry.latitude
            , userLocationView.getArrow().geometry.longitude)*/


        DirectionsFactory.initialize(this.context)

        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        submitRequest()


        val boundingBox =
            BoundingBox(START_LOCATION, TARGET_LOCATION) // getting BoundingBox between two points
        var cameraPosition = mapView!!.map.cameraPosition(boundingBox) // getting cameraPosition
        cameraPosition = CameraPosition(
            cameraPosition.target,
            cameraPosition.zoom - 0.8f,
            cameraPosition.azimuth,
            cameraPosition.tilt
        ) // Zoom 80%
        mapView!!.map.move(
            cameraPosition,
            Animation(Animation.Type.SMOOTH, 0f),
            null
        ) // move camera
    }

    override fun onDrivingRoutes(routes: List<DrivingRoute>) {
        for (route in routes) {
            mapObjects?.addPolyline(route.geometry)
            break
        }
    }

    override fun onDrivingRoutesError(error: Error) {
        var errorMessage = "1"
        if (error is RemoteError) {
            errorMessage = "2"
        } else if (error is NetworkError) {
            errorMessage = "3"
        }

        Toast.makeText(this.context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun submitRequest() {
        val options = DrivingOptions()
        val requestPoints = ArrayList<RequestPoint>()
        requestPoints.add(
            RequestPoint(
                START_LOCATION,
                RequestPointType.WAYPOINT,
                null
            )
        )
        requestPoints.add(
            RequestPoint(
                TARGET_LOCATION,
                RequestPointType.WAYPOINT, null
            )
        )
        drivingSession = drivingRouter?.requestRoutes(requestPoints, options, this)
    }

    override fun onClick(v: View?) {
        when (v) {
            plus -> {
                mapView!!.getMap().move(
                    CameraPosition(
                        mapView!!.getMap().getCameraPosition().getTarget(),
                        mapView!!.getMap().getCameraPosition().getZoom() + 1, 0.0f, 0.0f
                    ),
                    Animation(Animation.Type.SMOOTH, 0f),
                    null
                )
            }
            minus -> {
                mapView!!.getMap().move(
                    CameraPosition(
                        mapView!!.getMap().getCameraPosition().getTarget(),
                        mapView!!.getMap().getCameraPosition().getZoom() - 1, 0.0f, 0.0f
                    ),
                    Animation(Animation.Type.SMOOTH, 0f),
                    null
                )
            }

            current -> {
                START_LOCATION = Point(54.863965, 83.104700)
                mapView!!.getMap().move(
                    //CameraPosition(userLocationLayer!!.cameraPosition()!!.getTarget(), 15.0f, 0.0f, 0.0f),
                    CameraPosition(START_LOCATION, 15.0f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 0f),
                    null
                )
            }
        }
    }
}