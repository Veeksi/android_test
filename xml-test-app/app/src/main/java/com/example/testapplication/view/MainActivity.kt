package com.example.testapplication.view

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.get
import androidx.navigation.ui.*
import com.example.testapplication.R
import com.example.testapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        onSupportNavigateUp()

        with(binding) {
            bottomNav.setupWithNavController(navController)
            bottomNav.setOnItemReselectedListener(navController::popToRoot)
        }
    }

    override fun onBackPressed() {
        with(binding) {
            bottomNav.run {
                if (handle(navController)) {
                    selectedItemId = menu[0].itemId
                } else {
                    super.onBackPressed()
                }
            }
        }
    }

}

private fun BottomNavigationView.handle(navController: NavController): Boolean {
    val firstItemId = menu[0].itemId
    // On the root, left it normally
    if (selectedItemId == firstItemId) return false

    val prevDes = navController.previousBackStackEntry?.destination ?: return false
    val prevId = prevDes.parent?.id ?: prevDes.id

    return prevId == firstItemId
}

private fun NavController.popToRoot(menuItem: MenuItem) {
    val graph = graph[menuItem.itemId] as? NavGraph ?: return

    popBackStack(
        graph.startDestinationId,
        false,
    )
}
