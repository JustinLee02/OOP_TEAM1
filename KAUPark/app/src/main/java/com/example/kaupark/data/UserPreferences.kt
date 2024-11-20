package com.example.kaupark.data

import android.content.Context
import com.example.kaupark.model.User

// UserPreferences: SharedPreferences를 관리하는 싱글톤 클래스
class UserPreferences private constructor(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        val editer = sharedPreferences.edit()
        editer.putString("name", user.name)
        editer.putString("studentID", user.studentId)
        editer.putString("password", user.password)
        editer.putString("phoneNum", user.phoneNum)
        editer.putString("email", user.email)
        editer.putString("carNum", user.carNum)
        editer.putInt("deposit", user.deposit)
        editer.apply()
    }

    fun getUser(): User? {
        val name = sharedPreferences.getString("name", null) ?: return null
        val studentId = sharedPreferences.getString("studentId", null) ?: return null
        val password = sharedPreferences.getString("password", null) ?: return null
        val phoneNum = sharedPreferences.getString("phoneNum", null) ?: return null
        val email = sharedPreferences.getString("email", null) ?: return null
        val carNum = sharedPreferences.getString("carNum", null) ?: return null
        val deposit = sharedPreferences.getInt("deposit", 0)
        return User(name, studentId, password, phoneNum, email, carNum, deposit)
    }

    fun clearUser() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        // 싱글톤 인스턴스 (Volatile 키워드로 메모리 동기화)
        @Volatile
        private var INSTANCE: UserPreferences? = null


//         * UserPreferences 싱글톤 인스턴스를 반환
//         * @param context Context 객체
//         * @return UserPreferences 싱글톤 인스턴스

        fun getInstance(context: Context): UserPreferences {
            // 기존에 인스턴스가 없다면 동기화 블록을 통해 생성
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreferences(context).also { INSTANCE = it }
            }
        }
    }

}