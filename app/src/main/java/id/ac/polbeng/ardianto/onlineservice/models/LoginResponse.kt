package id.ac.polbeng.ardianto.onlineservice.models

data class LoginResponse(
    val message: String,
    val error: Boolean,
    val data: User
)
