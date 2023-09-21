package com.sj.saurabh.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sj.saurabh.model.MediaModelClass
import java.util.*


class MyServices : Service() {

    private var mediaList3: MutableList<MediaModelClass> = arrayListOf()
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // get the Firebase storage reference
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        if (intent != null) {
            mediaList3 = intent.getParcelableArrayListExtra<MediaModelClass>("mediaList2") as ArrayList<MediaModelClass>
        }

        if (mediaList3.size > 0){
            uploadImage(mediaList3) // Upload images to the firebase
        }

        return START_STICKY
    }



    override fun onDestroy() {
        super.onDestroy()
    }


    private fun uploadImage(list: MutableList<MediaModelClass>) {

        var count =0
        if (list.size> 0) {
            list.forEachIndexed { index, mediaModelClass ->

                val imageUri = list.get(index).imgUri
                var imgPath = imageUri.path
                imgPath = imgPath.toString().substring(47) // this will save only name of file in firebase

                val imageRef = storageReference?.child("images/$imgPath")
                Log.e("Service MediaList2", "$count list.size ${list.size} at $index")

                // adding listeners on upload or failure of image
                imageRef?.putFile(imageUri)?.addOnSuccessListener {
                    count++
                    if (count == list.size){
                        Log.e("Service MediaList CC", "$count list.size ${list.size} at $index")

                        val filter = "thisIsForMyFragment"
                        val intent = Intent(filter)
                        intent.putExtra("index",count)
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//                        stopService()
//                        Log.e("Service MediaList CC2", "$count list.size ${list.size} at $index")
                    }
//                Toast.makeText(requireContext(), "Uploaded", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener { e ->
                    Log.e("Firebase", "" + e.message)
//                Toast.makeText(requireContext(), "Failed " + e.message, Toast.LENGTH_SHORT).show()
                }?.addOnProgressListener { taskSnapshot ->
                    val progress: Double =
                        100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()


                    Log.e("Service: MediaList3 ", "at $index  Uploaded " + progress.toInt() + "%")
                }
            }

        }else{
            Log.e("Service: MediaList33 EMPTY", "at ${list.size}")

        }

    }
    fun stopService() {
        stopSelf()
    }
}