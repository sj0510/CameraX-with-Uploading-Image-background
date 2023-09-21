package com.sj.saurabh.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.sj.saurabh.R
import com.sj.saurabh.databinding.FragmentHomeBinding
import com.sj.saurabh.utils.Utils

/**
 **@author Saurabh Kumar 27-03-2023
 */
class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        if (!PermissionsFragment.hasPermissions(requireContext())) {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.navigation_PermissionsFragment)
        }


        binding.captureImgBTN.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.navigation_CameraFragment)
        }

        val utils = Utils()
        binding.showUploadBTN.setOnClickListener {
            if (utils.checkInternetConnection(requireContext())) {
                Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.navigation_GalleryFragment2)
            } else {
                Toast.makeText(requireContext(), "Opps! No Internet\nPlease Connect to Internet", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }


}