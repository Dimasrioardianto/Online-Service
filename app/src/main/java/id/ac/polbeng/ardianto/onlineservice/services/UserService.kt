package id.ac.polbeng.ardianto.onlineservice.services

import id.ac.polbeng.ardianto.onlineservice.models.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @GET("login")
    fun loginUser(
        @QueryMap filter: HashMap<String, String>
    ): Call<LoginResponse>
}