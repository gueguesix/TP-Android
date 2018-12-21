package com.android.florent.tpandroid18

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NewsLiveData : MutableLiveData<MutableList<News>>() {
    override fun onActive() {
        FirebaseDatabase.getInstance().getReference("")
                .addValueEventListener(object :ValueEventListener {

                    override fun onDataChange(data:DataSnapshot) {

                        val news = mutableListOf<News>()

                        for (item in data.children) {
                            if(item.child("title").getValue(String::class.java) != null) {
                                news.addNonNull(item.getValue(News::class.java))
                            }
                        }

                        postValue(news)
                    }

                    override fun onCancelled(p0:DatabaseError) {
                    }


                })
    }

    override fun onInactive() {
    }
}