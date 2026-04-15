package com.simats.PowerPulse.repository

import com.simats.PowerPulse.model.*
import com.simats.PowerPulse.network.ApiClient

class AuthRepository {

    suspend fun registerUser(
        fullName: String,
        mandal: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<BasicOkResponse> {
        return try {
            val response = ApiClient.api.registerUser(
                UserRegisterReq(
                    fullName = fullName,
                    mandal = mandal,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword
                )
            )
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(Exception(body?.error ?: "User registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(
        email: String,
        password: String
    ): Result<LoginResponse> {
        return try {
            val response = ApiClient.api.loginUser(
                UserLoginReq(
                    email = email,
                    password = password
                )
            )
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(Exception(body?.error ?: "User login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerAdmin(
        fullName: String,
        orgName: String,
        boardId: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<BasicOkResponse> {
        return try {
            val response = ApiClient.api.registerAdmin(
                AdminRegisterReq(
                    fullName = fullName,
                    orgName = orgName,
                    boardId = boardId,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword
                )
            )
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(Exception(body?.error ?: "Admin registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginAdmin(
        orgName: String,
        boardId: String,
        email: String,
        password: String
    ): Result<LoginResponse> {
        return try {
            val response = ApiClient.api.loginAdmin(
                AdminLoginReq(
                    orgName = orgName,
                    boardId = boardId,
                    email = email,
                    password = password
                )
            )
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(Exception(body?.error ?: "Admin login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun forgotPassword(
        email: String
    ): Result<BasicOkResponse> {
        return try {
            val response = ApiClient.api.forgotPassword(
                ForgotPasswordRequest(email = email)
            )
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(Exception(body?.error ?: "OTP request failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyOtp(
        email: String,
        otp: String
    ): Result<BasicOkResponse> {
        return try {
            val response = ApiClient.api.verifyOtp(
                VerifyResetCodeRequest(
                    email = email,
                    otp = otp
                )
            )
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(Exception(body?.error ?: "OTP verification failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(
        email: String,
        otp: String,
        newPassword: String,
        confirmPassword: String
    ): Result<BasicOkResponse> {
        return try {
            val response = ApiClient.api.resetPassword(
                ResetPasswordRequest(
                    email = email,
                    otp = otp,
                    newPassword = newPassword,
                    confirmPassword = confirmPassword
                )
            )
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(Exception(body?.error ?: "Password reset failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
