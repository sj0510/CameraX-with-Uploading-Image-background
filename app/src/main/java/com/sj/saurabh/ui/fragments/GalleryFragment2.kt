package com.sj.saurabh.ui.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaScannerConnection
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.sj.saurabh.R
import com.sj.saurabh.databinding.FragmentGallery2Binding
import com.sj.saurabh.adapter.ImageAdapter
import com.sj.saurabh.model.MediaModelClass
import com.sj.saurabh.utils.MediaStoreFile
import com.sj.saurabh.utils.MediaStoreUtils
import com.sj.saurabh.services.MyServices
import com.sj.saurabh.utils.Utils
import kotlinx.coroutines.launch

/**
 **@author Saurabh Kumar 27-03-2023
 */
class GalleryFragment2 : Fragment() {


    private var binding: FragmentGallery2Binding? = null

    private val fragmentGallery2Binding get() = binding!!


    private var mediaList: MutableList<MediaStoreFile> = mutableListOf()
    private var mediaList2: MutableList<MediaModelClass> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGallery2Binding.inflate(inflater, container, false)

        // register BroadcastReceiver
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(myBroadcastReceiver, IntentFilter("thisIsForMyFragment"))

        fragmentGallery2Binding.homeBackIMG.setOnClickListener{
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.navigation_HomeFragment)
        }

        return fragmentGallery2Binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            // Get images this app has access to from MediaStore
            mediaList = MediaStoreUtils(requireContext()).getImages()

            val layoutManager = GridLayoutManager(requireContext(), 3)
            binding?.recyclerView?.layoutManager = layoutManager

            val adapter = ImageAdapter(requireContext(),mediaList)
            binding?.recyclerView?.adapter = adapter
            Log.e("MediaList"," sizeX "+mediaList.size)

            if (mediaList.size >0){
                val builder = android.app.AlertDialog.Builder(requireContext())
                builder.setTitle("Do you want to upload all images to server?")
                builder.setMessage("")
                builder.setPositiveButton("OK") { dialog, _ ->

                    var i = mediaList.size
                    if (mediaList.size > 0){
                        mediaList.forEachIndexed { index, e ->
                            Log.e("mediaFile=>> ","$e at $index")


                            val imageUri = e.file.toUri()
                            mediaList2.add(MediaModelClass(imageUri))

                            Log.e("MediaList", " i $i")
                        }

                    }
                    val utils = Utils()
                    if (utils.checkInternetConnection(requireContext())) {

                        if (mediaList2 != null && !mediaList2.isEmpty() ) {

                            // Start Service
                            val intent = Intent(requireContext(), MyServices()::class.java)
                            intent.putParcelableArrayListExtra("mediaList2", ArrayList(mediaList2))
                            requireActivity().startService(intent)

                        }else{
                            Toast.makeText(requireContext(),"Image is null ", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Opps! No Internet\nPlease Connect to Internet!", Toast.LENGTH_SHORT).show()
                    }

                    dialog.cancel()
                }
                builder.setNegativeButton("CANCEL") { dialog, _ ->
                    Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.navigation_HomeFragment)
                    dialog.cancel()
                }
                builder.show()
            }else{
                Toast.makeText(requireContext(), "No Images Found!\nPlease capture images",Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.navigation_HomeFragment)
                Log.e("MediaList"," sizeE "+mediaList.size)
            }
        }
    }


    private val myBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            var index = intent!!.getIntExtra("index",0)
            Log.e("MediaList BR","All images Updated Successfully of size $index")

            deleteImage() // Delete images after Successful Update

        }
    }


    private fun deleteImage() {
        while (mediaList.isNotEmpty()) {
            if(mediaList.get(0).file.isFile){
                val mediaFile = mediaList.get(0).file

                Log.e("MediaList", " sizeDA " + mediaList.size)

                // Delete current photo
                mediaFile.delete()

                // Send relevant broadcast to notify other apps of deletion
                MediaScannerConnection.scanFile(requireActivity(), arrayOf(mediaFile.absolutePath), null, null)

                mediaList.removeAt(0)
                binding?.recyclerView?.adapter?.notifyDataSetChanged()
                Toast.makeText(activity, "Image updated successfully!", Toast.LENGTH_SHORT).show()


                // If all photos have been deleted, return to camera
                if (mediaList.isEmpty()) {
                    binding?.recyclerView?.visibility = View.INVISIBLE
                    binding?.noDataFoundTV?.visibility = View.VISIBLE
                    Toast.makeText(activity, "All images are updated successfully!", Toast.LENGTH_SHORT).show()


                    requireActivity().stopService(Intent(requireContext(), MyServices()::class.java))
                    LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(myBroadcastReceiver);

//                        Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.navigation_HomeFragment)
                }
                Log.e("MediaList", " sizeDB " + mediaList.size)

            }
        }
    }



    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(myBroadcastReceiver, IntentFilter("thisIsForMyFragment"))

    }
    override fun onDestroyView() {
        binding = null
        super.onDestroyView()


    }

    // Unbind from the service in the onDestroy() method of the fragment
    override fun onDestroy() {

        super.onDestroy()
        requireActivity().stopService(Intent(requireContext(), MyServices()::class.java))
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(myBroadcastReceiver);



    }
}