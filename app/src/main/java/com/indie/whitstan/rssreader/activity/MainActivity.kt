package com.indie.whitstan.rssreader.activity

import android.os.Bundle
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.get
import androidx.navigation.ui.NavigationUI

import kotlinx.android.synthetic.main.activity_main.*

import com.indie.whitstan.rssreader.R
import com.indie.whitstan.rssreader.viewmodel.ItemViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    val itemViewModel : ItemViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        setupBottomNav(navController)
        setupActionBar(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return (Navigation.findNavController(this, R.id.nav_host_fragment_container).navigateUp()
                || super.onSupportNavigateUp())
    }

    private fun setupActionBar(navController: NavController){
        val toolbar = findViewById<Toolbar>(R.id.toolbar_main)
        toolbar.setTitle(R.string.title_main)
        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    private fun setupBottomNav(navController: NavController) {
        bottom_nav?.let {
            NavigationUI.setupWithNavController(it, navController)
        }
        bottom_nav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.favorites_destination -> {
                    if (navController.currentDestination == navController.graph[R.id.list_destination]) {
                        navController.navigate(R.id.navigate_to_favorites_from_list)
                    }
                    else if (navController.currentDestination == navController.graph[R.id.details_destination]){
                        navController.navigate(R.id.navigate_to_favorites_from_details)
                    }
                }
                R.id.list_destination -> {
                    if (navController.currentDestination == navController.graph[R.id.favorites_destination]){
                        navController.navigate(R.id.navigate_to_list_from_favorites)
                    }
                    else if (navController.currentDestination == navController.graph[R.id.details_destination]){
                        navController.navigate(R.id.navigate_to_list_from_details)
                    }

                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_container)
        val navigated = NavigationUI.onNavDestinationSelected(item, navController)
        return navigated || super.onOptionsItemSelected(item)
    }
}