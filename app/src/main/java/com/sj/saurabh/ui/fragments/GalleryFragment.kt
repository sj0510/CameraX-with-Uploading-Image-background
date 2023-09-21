package com.sj.saurabh.ui.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sj.saurabh.R
import com.sj.saurabh.databinding.FragmentGalleryBinding
import com.sj.saurabh.utils.*
import kotlinx.coroutines.*
import java.io.File
import java.util.*
/**
 **@author Saurabh Kumar 27-03-2023
 */
class GalleryFragment internal constructor() : Fragment() {

    private var _fragmentGalleryBinding: FragmentGalleryBinding? = null

    private val fragmentGalleryBinding get() = _fragmentGalleryBinding!!


    private var mediaList: MutableList<MediaStoreFile> = mutableListOf()
    private var hasMediaItems = CompletableDeferred<Boolean>()

    // Create a storage reference from our app
    // instance for firebase storage and StorageReference
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null



    /** Adapter class used to present a fragment containing one photo or video as a page */
    inner class MediaPagerAdapter(fm: FragmentManager, private var mediaList: MutableList<MediaStoreFile>) :
        FragmentStateAdapter(fm, lifecycle) {
        override fun getItemCount(): Int = mediaList.size
        override fun createFragment(position: Int): Fragment =
            PhotoFragment.create(mediaList[position])
        override fun getItemId(position: Int): Long {
            return mediaList[position].id
        }
        override fun containsItem(itemId: Long): Boolean {
            return null != mediaList.firstOrNull { it.id == itemId }
        }
        fun setMediaListAndNotify(mediaList: MutableList<MediaStoreFile>) {
            this.mediaList = mediaList
            notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            // Get images this app has access to from MediaStore
            mediaList = MediaStoreUtils(requireContext()).getImages()
            (fragmentGalleryBinding.photoViewPager.adapter as MediaPagerAdapter).setMediaListAndNotify(mediaList)
            hasMediaItems.complete(mediaList.isNotEmpty())

            Log.e("MediaList"," sizeX "+mediaList.size)

            if (mediaList.size >0){
                val builder = android.app.AlertDialog.Builder(requireContext())
                builder.setTitle("Do you want to upload all images to server?")
                builder.setMessage("")
                builder.setPositiveButton("OK") { dialog, _ ->

                    var i = mediaList.size
                    while (i >= 0) {
                        Log.e("MediaList"," i "+i)
                        mediaList.getOrNull(i)?.let { mediaStoreFile ->
                            val mediaFile = mediaStoreFile.file
                            val imageUri = mediaFile.toUri()

//                            requireActivity().startService(Intent(requireContext(),MyServices(imageUri)::class.java))
//                            uploadImage(mediaFile)
                        }
                        i--
                    }

                    dialog.cancel()
                }
                builder.setNegativeButton("CANCEL") { dialog, _ ->
                    Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.navigation_HomeFragment)

                    dialog.cancel()
                }
                builder.show()
            }else{
                Toast.makeText(requireContext(), "No Images Found!",Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.navigation_HomeFragment)
                Log.e("MediaList"," sizeE "+mediaList.size)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _fragmentGalleryBinding = FragmentGalleryBinding.inflate(inflater, container, false)
        return fragmentGalleryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // get the Firebase storage reference
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        lifecycleScope.launch {
            fragmentGalleryBinding.deleteButton.isEnabled = hasMediaItems.await()
            fragmentGalleryBinding.shareButton.isEnabled = hasMediaItems.await()
            fragmentGalleryBinding.uploadButton.isEnabled = hasMediaItems.await()
        }

        // Populate the ViewPager and implement a cache of two media items
        fragmentGalleryBinding.photoViewPager.apply {
            offscreenPageLimit = 2
            adapter = MediaPagerAdapter(childFragmentManager, mediaList)
        }

        // Make sure that the cutout "safe area" avoids the screen notch if any
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Use extension method to pad "inside" view containing UI using display cutout's bounds
            fragmentGalleryBinding.cutoutSafeArea.padWithDisplayCutout()
        }

        // Handle back button press
        fragmentGalleryBinding.homeImgBtn.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.navigation_HomeFragment)
        }

        // Handle share button press
        fragmentGalleryBinding.shareButton.setOnClickListener {

            mediaList.getOrNull(fragmentGalleryBinding.photoViewPager.currentItem)
                ?.let { mediaStoreFile ->
                    val mediaFile = mediaStoreFile.file
                    // Create a sharing intent
                    val intent = Intent().apply {
                        // Infer media type from file extension
                        val mediaType = MimeTypeMap.getSingleton()
                            .getMimeTypeFromExtension(mediaFile.extension)
                        // Set the appropriate intent extra, type, action and flags
                        type = mediaType
                        action = Intent.ACTION_SEND
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        putExtra(Intent.EXTRA_STREAM, mediaStoreFile.uri)
                    }

                    // Launch the intent letting the user choose which app to share with
                    startActivity(Intent.createChooser(intent, getString(R.string.share_hint)))
                }
        }

        // Handle delete button press
        fragmentGalleryBinding.deleteButton.setOnClickListener {

            mediaList.getOrNull(fragmentGalleryBinding.photoViewPager.currentItem)
                ?.let { mediaStoreFile ->
                    val mediaFile = mediaStoreFile.file

                    AlertDialog.Builder(view.context, android.R.style.Theme_Material_Dialog)
                        .setTitle(getString(R.string.delete_title))
                        .setMessage(getString(R.string.delete_dialog))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok) { _, _ ->

                            deleteImage(mediaFile)


                        }

                        .setNegativeButton(android.R.string.cancel, null)
                        .create().showImmersive()
                }
        }

        // Handle Upload button press
        fragmentGalleryBinding.uploadButton.setOnClickListener {
            mediaList.getOrNull(fragmentGalleryBinding.photoViewPager.currentItem)
                ?.let { mediaStoreFile ->
                    val mediaFile = mediaStoreFile.file
                    // getting URI of selected Image
                    uploadImage(mediaFile)



                }



        }
    }

    private fun deleteImage( mediaFile: File) {
        Log.e("MediaList"," sizeDA "+mediaList.size)

        // Delete current photo
        mediaFile.delete()

        // Send relevant broadcast to notify other apps of deletion
        MediaScannerConnection.scanFile(view?.context, arrayOf(mediaFile.absolutePath),null, null)

        // Notify our view pager
        mediaList.removeAt(fragmentGalleryBinding.photoViewPager.currentItem)
        fragmentGalleryBinding.photoViewPager.adapter?.notifyDataSetChanged()

        Log.e("MediaList"," sizeDB "+mediaList.size)

        // If all photos have been deleted, return to camera
        if (mediaList.isEmpty()) {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.navigation_HomeFragment)
        }
    }

    private fun uploadImage(mediaFile: File) {
        val imageUri = mediaFile.toUri()

        if (imageUri != null) {
            val progressDialog = ProgressDialog(requireContext())
            progressDialog.setTitle("Uploading...")
            progressDialog.show()
            // Defining the child of storageReference
            val imageRef = storageReference?.child("images/" + UUID.randomUUID().toString())



            // adding listeners on upload or failure of image
            imageRef?.putFile(imageUri)?.addOnSuccessListener {
                progressDialog.dismiss()
                deleteImage(mediaFile) // delete photo after successful upload
                Toast.makeText(requireContext(), "Uploaded", Toast.LENGTH_SHORT).show()
            }?.addOnFailureListener { e ->
                progressDialog.dismiss()
                Log.e("Firebase",""+e.message)
                Toast.makeText(requireContext(), "Failed " + e.message, Toast.LENGTH_SHORT).show()
            }?.addOnProgressListener { taskSnapshot ->
                val progress: Double =
                    100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                        .getTotalByteCount()
                progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
            }
        }
    }


    override fun onDestroyView() {
        _fragmentGalleryBinding = null
        super.onDestroyView()
    }


}