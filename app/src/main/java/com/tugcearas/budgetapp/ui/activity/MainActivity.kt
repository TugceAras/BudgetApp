package com.tugcearas.budgetapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.tugcearas.budgetapp.R
import com.tugcearas.budgetapp.databinding.ActivityMainBinding
import com.tugcearas.budgetapp.util.gone
import com.tugcearas.budgetapp.util.visible

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavView,navController)

        navController.addOnDestinationChangedListener{_, destination,_ ->
            when(destination.id){
                R.id.splashScreen -> {
                    window.statusBarColor = ContextCompat.getColor(this,R.color.white)
                    binding.bottomNavView.gone()
                }
                R.id.signinScreen,
                R.id.signupScreen, R.id.detailScreen -> {
                    binding.bottomNavView.gone()
                    window.statusBarColor = ContextCompat.getColor(this,R.color.main_color)
                }
                else -> {
                    binding.bottomNavView.visible()
                    window.statusBarColor = ContextCompat.getColor(this,R.color.main_color)
                }
            }
        }
    }
}