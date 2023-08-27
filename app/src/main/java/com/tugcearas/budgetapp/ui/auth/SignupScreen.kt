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
import com.tugcearas.budgetapp.databinding.FragmentSignupScreenBinding
import com.tugcearas.budgetapp.util.click
import com.tugcearas.budgetapp.util.toastMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignupScreen : Fragment() {

    private lateinit var binding : FragmentSignupScreenBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var userDataStore: UserDataStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        userDataStore = UserDataStore(requireContext())

        binding.tvSigninClick.click {
            findNavController().navigate(R.id.action_signupScreen_to_signinScreen)
        }

        checkUserInfo()
    }

    private fun checkUserInfo(){
        with(binding){
            btnSignup.click {
                val getEmail = tvSignupMail.text.toString()
                val getPassword = tvSignupPassword.text.toString()
                val getNameSurname = binding.tvNameSurname.text.toString()

                if (getEmail.isNotEmpty() && getPassword.isNotEmpty() && getNameSurname.isNotEmpty())
                    signup(getEmail,getPassword,getNameSurname)

                else requireContext().toastMessage(getString(R.string.fill_in_the_blank))
            }
        }
    }

    private fun signup(email:String, password:String, nameSurname:String){
        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {

            CoroutineScope(Dispatchers.IO).launch {
                userDataStore.saveNameSurname(nameSurname)
            }

            findNavController().navigate(R.id.action_signupScreen_to_summaryScreen)
            requireContext().toastMessage(getString(R.string.signup_successfully))
        }.addOnFailureListener {
            requireContext().toastMessage(it.message.orEmpty())
        }
    }
}