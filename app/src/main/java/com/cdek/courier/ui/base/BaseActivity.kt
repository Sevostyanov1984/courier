package com.cdek.courier.ui.base

import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cdek.courier.R
import com.cdek.courier.databinding.ActivityBaseBinding
import com.cdek.courier.ui.start.StartActivity
import com.cdek.courier.utils.SCAN_PVZ_PREF_KEY_TOKEN
import com.cdek.courier.utils.SCAN_PVZ_PREF_KEY_USER_LOGIN
import com.cdek.courier.utils.getPreferenceString
import com.cdek.courier.utils.removePreferenceString
import com.cdek.courier.view.CountDrawable
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_base.*
import javax.inject.Inject

class BaseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this@BaseActivity, viewModelFactory)[BaseViewModel::class.java]
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var toolbarMenu: Menu
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //don't start the app again from icon on launcher
        if (!isTaskRoot) {
            val intentAction = intent.action
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction == Intent.ACTION_MAIN) {
                finish()
                return
            }
        }

        val binding: ActivityBaseBinding = DataBindingUtil.setContentView<ActivityBaseBinding>(
            this@BaseActivity,
            R.layout.activity_base
        ).apply { lifecycleOwner = this@BaseActivity }
        binding.viewModel = viewModel

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.tasklistFragment, R.id.nav_calc
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        fab_map.setOnClickListener(View.OnClickListener {
            val bundle = bundleOf("mapType" to "list")
            navController.navigate(R.id.yandexMap, bundle)

        })

        // обработчик элемента меню "Выход"
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val logoutItem = navigationView.menu.findItem(R.id.nav_exit)
        logoutItem.setOnMenuItemClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            removePreferenceString(SCAN_PVZ_PREF_KEY_USER_LOGIN)
            removePreferenceString(SCAN_PVZ_PREF_KEY_TOKEN)
            finish()
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {

        /*if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }*/

        when (navController.currentDestination?.id) {
            R.id.tasklistFragment -> {
                finish()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        toolbarMenu = menu
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        initObserve()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ic_group -> {
                startNotificationFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setCount(count: String) {
        val menuItem: MenuItem = toolbarMenu.findItem(R.id.ic_group)
        val icon = menuItem.icon as LayerDrawable
        val badge: CountDrawable
        // Reuse drawable if possible
        val reuse = icon.findDrawableByLayerId(R.id.ic_group_count)
        badge = if (reuse != null && reuse is CountDrawable) {
            reuse
        } else {
            CountDrawable(this@BaseActivity)
        }
        badge.setCount(count)
        icon.mutate()
        icon.setDrawableByLayerId(R.id.ic_group_count, badge)
        invalidateOptionsMenu()
    }

    private fun initObserve() {

        viewModel.countNotification.observe(this@BaseActivity, Observer {
            setCount(it.toString())
        })

        viewModel.getNotification().observe(this@BaseActivity, Observer {
            viewModel.handleUpdateNotificationList(it)
        })
    }

    private fun startNotificationFragment() {
        navController.navigate(R.id.notificationFragment)
    }
}