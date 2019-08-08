package com.example.betterrecognize.network

class Network constructor(private val networkApi: NetworkApi) {

//    suspend fun uploadData(): LiveData<String> {
//        val liveData = MutableLiveData<String>()
//
//        networkApi.postData().enqueue(object : Callback<TicketResponse> {
//            override fun onFailure(call: Call<TicketResponse>, t: Throwable) {
//                t.printStackTrace()
//            }
//
//            override fun onResponse(call: Call<TicketResponse>, response: Response<TicketResponse>) {
//                if (response.isSuccessful) {
//                    liveData.value = response.body()?.msg
//                }
//            }
//
//        })
//        return liveData
//    }

    suspend fun uploadData() = networkApi.postData()
}