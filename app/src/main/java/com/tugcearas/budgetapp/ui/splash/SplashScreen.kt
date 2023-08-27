package com.tugcearas.budgetapp.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tugcearas.budgetapp.databinding.FragmentSplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment() {

    private lateinit var binding:FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater,container,false)

        Handler(Looper.getMainLooper()).postDelayed({
            val action = SplashScreenDirections.actionSplashScreenToSigninScreen()
            findNavController().navigate(action)
        },2000)

        return binding.root
    }
}