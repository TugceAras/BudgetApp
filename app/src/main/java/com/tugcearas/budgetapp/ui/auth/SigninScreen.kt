package com.tugcearas.budgetapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tugcearas.budgetapp.R
import com.tugcearas.budgetapp.databinding.FragmentSigninScreenBinding
import com.tugcearas.budgetapp.util.click
import com.tugcearas.budgetapp.util.toastMessage

class SigninScreen : Fragment() {

    private lateinit var binding: FragmentSigninScreenBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        auth.currentUser?.let {
            findNavController().navigate(R.id.action_signinScreen_to_summaryScreen)
        }

        binding.tvSignupClick.click {
            findNavController().navigate(R.id.action_signinScreen_to_signupScreen)
        }

        checkUserInfo()
    }

    private fun checkUserInfo(){
        with(binding){
            btnSignin.click {
                val getEmail = tvSigninMail.text.toString()
                val getPassword = tvSigninPassword.text.toString()

                if (getEmail.isNotEmpty() && getPassword.isNotEmpty()) signIn(getEmail,getPassword)
                else requireContext().toastMessage(getString(R.string.fill_in_the_blank))
            }
        }
    }

    private fun signIn(email:String, password:String){
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
            findNavController().navigate(R.id.action_signinScreen_to_summaryScreen)
            requireContext().toastMessage(getString(R.string.signin_successfully))
        }.addOnFailureListener {
            requireContext().toastMessage(it.message.orEmpty())
        }
    }
}