package com.sinaptix.smartsell.data.remote

import com.sinaptix.smartsell.data.dto.AddCartItemRequest
import com.sinaptix.smartsell.data.dto.ApiResponse
import com.sinaptix.smartsell.data.dto.AuthResponse
import com.sinaptix.smartsell.data.dto.CartResponse
import com.sinaptix.smartsell.data.dto.CategoryResponse
import com.sinaptix.smartsell.data.dto.CustomerResponse
import com.sinaptix.smartsell.data.dto.GoogleAuthRequest
import com.sinaptix.smartsell.data.dto.LoginRequest
import com.sinaptix.smartsell.data.dto.OrderResponse
import com.sinaptix.smartsell.data.dto.PaginatedOrdersResponse
import com.sinaptix.smartsell.data.dto.PaginatedProductsResponse
import com.sinaptix.smartsell.data.dto.PlaceOrderRequest
import com.sinaptix.smartsell.data.dto.ProductResponse
import com.sinaptix.smartsell.data.dto.RefreshResponse
import com.sinaptix.smartsell.data.dto.RefreshTokenRequest
import com.sinaptix.smartsell.data.dto.RegisterRequest
import com.sinaptix.smartsell.data.dto.UpdateCartItemRequest
import com.sinaptix.smartsell.data.dto.UpdateCustomerRequest
import com.sinaptix.smartsell.shared.util.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiService(private val tokenStorage: TokenStorage) {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }

    // ─── Auth endpoints (no token required) ───────────────────────────────────

    suspend fun googleAuth(idToken: String): Result<AuthResponse> {
        return try {
            val response = client.post("$BASE_URL/api/auth/google") {
                setBody(GoogleAuthRequest(idToken = idToken))
            }
            val apiResponse = response.body<ApiResponse<AuthResponse>>()
            if (apiResponse.success && apiResponse.data != null) {
                Result.success(apiResponse.data)
            } else {
                Result.failure(Exception(apiResponse.error?.message ?: "Google auth failed"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Google auth error: ${e.message}"))
        }
    }

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = client.post("$BASE_URL/api/auth/login") {
                setBody(LoginRequest(email = email, password = password))
            }
            val apiResponse = response.body<ApiResponse<AuthResponse>>()
            if (apiResponse.success && apiResponse.data != null) {
                Result.success(apiResponse.data)
            } else {
                Result.failure(Exception(apiResponse.error?.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Login error: ${e.message}"))
        }
    }

    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<AuthResponse> {
        return try {
            val response = client.post("$BASE_URL/api/auth/register") {
                setBody(RegisterRequest(firstName = firstName, lastName = lastName, email = email, password = password))
            }
            val apiResponse = response.body<ApiResponse<AuthResponse>>()
            if (apiResponse.success && apiResponse.data != null) {
                Result.success(apiResponse.data)
            } else {
                Result.failure(Exception(apiResponse.error?.message ?: "Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Registration error: ${e.message}"))
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            authenticatedRequest { token ->
                client.post("$BASE_URL/api/auth/logout") {
                    header("Authorization", "Bearer $token")
                }
                Unit
            }
        } catch (e: Exception) {
            Result.failure(Exception("Logout error: ${e.message}"))
        }
    }

    // ─── Authenticated endpoints ───────────────────────────────────────────────

    suspend fun getMyProfile(): Result<CustomerResponse> {
        return authenticatedRequest { token ->
            val response = client.get("$BASE_URL/api/customers/me") {
                header("Authorization", "Bearer $token")
            }
            val apiResponse = response.body<ApiResponse<CustomerResponse>>()
            if (apiResponse.success && apiResponse.data != null) {
                apiResponse.data
            } else {
                throw Exception(apiResponse.error?.message ?: "Failed to fetch profile")
            }
        }
    }

    suspend fun updateMyProfile(request: UpdateCustomerRequest): Result<CustomerResponse> {
        return authenticatedRequest { token ->
            val response = client.put("$BASE_URL/api/customers/me") {
                header("Authorization", "Bearer $token")
                setBody(request)
            }
            val apiResponse = response.body<ApiResponse<CustomerResponse>>()
            if (apiResponse.success && apiResponse.data != null) {
                apiResponse.data
            } else {
                throw Exception(apiResponse.error?.message ?: "Failed to update profile")
            }
        }
    }

    // ─── Products (public - no auth needed) ───────────────────────────────────

    suspend fun getProducts(
        storeId: String,
        page: Int = 1,
        limit: Int = 20,
        categoryId: String? = null,
        search: String? = null,
        isPopular: Boolean? = null,
        isNew: Boolean? = null
    ): Result<ApiResponse<PaginatedProductsResponse>> {
        return try {
            val response = client.get("$BASE_URL/api/stores/$storeId/products") {
                parameter("page", page)
                parameter("limit", limit)
                if (categoryId != null) parameter("category", categoryId)
                if (search != null) parameter("search", search)
                if (isPopular != null) parameter("isPopular", isPopular)
                if (isNew != null) parameter("isNew", isNew)
            }
            Result.success(response.body<ApiResponse<PaginatedProductsResponse>>())
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching products: ${e.message}"))
        }
    }

    suspend fun getProductById(productId: String): Result<ApiResponse<ProductResponse>> {
        return try {
            val response = client.get("$BASE_URL/api/products/$productId")
            Result.success(response.body<ApiResponse<ProductResponse>>())
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching product: ${e.message}"))
        }
    }

    // ─── Categories (public) ──────────────────────────────────────────────────

    suspend fun getCategories(storeId: String): Result<ApiResponse<List<CategoryResponse>>> {
        return try {
            val response = client.get("$BASE_URL/api/stores/$storeId/categories")
            Result.success(response.body<ApiResponse<List<CategoryResponse>>>())
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching categories: ${e.message}"))
        }
    }

    // ─── Cart (authenticated) ─────────────────────────────────────────────────

    suspend fun getCart(storeId: String): Result<ApiResponse<CartResponse>> {
        return authenticatedRequest { token ->
            val response = client.get("$BASE_URL/api/stores/$storeId/cart") {
                header("Authorization", "Bearer $token")
            }
            val apiResponse = response.body<ApiResponse<CartResponse>>()
            if (apiResponse.success) {
                apiResponse
            } else {
                throw Exception(apiResponse.error?.message ?: "Failed to fetch cart")
            }
        }
    }

    suspend fun addCartItem(storeId: String, request: AddCartItemRequest): Result<ApiResponse<CartResponse>> {
        return authenticatedRequest { token ->
            val response = client.post("$BASE_URL/api/stores/$storeId/cart/items") {
                header("Authorization", "Bearer $token")
                setBody(request)
            }
            val apiResponse = response.body<ApiResponse<CartResponse>>()
            if (apiResponse.success) {
                apiResponse
            } else {
                throw Exception(apiResponse.error?.message ?: "Failed to add item to cart")
            }
        }
    }

    suspend fun updateCartItem(storeId: String, itemId: String, request: UpdateCartItemRequest): Result<ApiResponse<CartResponse>> {
        return authenticatedRequest { token ->
            val response = client.put("$BASE_URL/api/stores/$storeId/cart/items/$itemId") {
                header("Authorization", "Bearer $token")
                setBody(request)
            }
            val apiResponse = response.body<ApiResponse<CartResponse>>()
            if (apiResponse.success) {
                apiResponse
            } else {
                throw Exception(apiResponse.error?.message ?: "Failed to update cart item")
            }
        }
    }

    suspend fun removeCartItem(storeId: String, itemId: String): Result<ApiResponse<CartResponse>> {
        return authenticatedRequest { token ->
            val response = client.delete("$BASE_URL/api/stores/$storeId/cart/items/$itemId") {
                header("Authorization", "Bearer $token")
            }
            val apiResponse = response.body<ApiResponse<CartResponse>>()
            if (apiResponse.success) {
                apiResponse
            } else {
                throw Exception(apiResponse.error?.message ?: "Failed to remove cart item")
            }
        }
    }

    suspend fun clearCart(storeId: String): Result<ApiResponse<Unit>> {
        return authenticatedRequest { token ->
            val response = client.delete("$BASE_URL/api/stores/$storeId/cart") {
                header("Authorization", "Bearer $token")
            }
            val apiResponse = response.body<ApiResponse<Unit>>()
            if (apiResponse.success) {
                apiResponse
            } else {
                throw Exception(apiResponse.error?.message ?: "Failed to clear cart")
            }
        }
    }

    // ─── Orders (authenticated) ───────────────────────────────────────────────

    suspend fun placeOrder(request: PlaceOrderRequest): Result<ApiResponse<OrderResponse>> {
        return authenticatedRequest { token ->
            val response = client.post("$BASE_URL/api/orders") {
                header("Authorization", "Bearer $token")
                setBody(request)
            }
            val apiResponse = response.body<ApiResponse<OrderResponse>>()
            if (apiResponse.success) {
                apiResponse
            } else {
                throw Exception(apiResponse.error?.message ?: "Failed to place order")
            }
        }
    }

    suspend fun getMyOrders(page: Int = 1, limit: Int = 10): Result<ApiResponse<PaginatedOrdersResponse>> {
        return authenticatedRequest { token ->
            val response = client.get("$BASE_URL/api/orders/my") {
                header("Authorization", "Bearer $token")
                parameter("page", page)
                parameter("limit", limit)
            }
            val apiResponse = response.body<ApiResponse<PaginatedOrdersResponse>>()
            if (apiResponse.success) {
                apiResponse
            } else {
                throw Exception(apiResponse.error?.message ?: "Failed to fetch orders")
            }
        }
    }

    suspend fun getOrderById(orderId: String): Result<ApiResponse<OrderResponse>> {
        return authenticatedRequest { token ->
            val response = client.get("$BASE_URL/api/orders/$orderId") {
                header("Authorization", "Bearer $token")
            }
            val apiResponse = response.body<ApiResponse<OrderResponse>>()
            if (apiResponse.success) {
                apiResponse
            } else {
                throw Exception(apiResponse.error?.message ?: "Failed to fetch order")
            }
        }
    }

    // ─── Token refresh ─────────────────────────────────────────────────────────

    suspend fun refreshToken(refreshToken: String): Result<RefreshResponse> {
        return try {
            val response = client.post("$BASE_URL/api/auth/refresh") {
                setBody(RefreshTokenRequest(refreshToken = refreshToken))
            }
            val apiResponse = response.body<ApiResponse<RefreshResponse>>()
            if (apiResponse.success && apiResponse.data != null) {
                Result.success(apiResponse.data)
            } else {
                Result.failure(Exception(apiResponse.error?.message ?: "Token refresh failed"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Token refresh error: ${e.message}"))
        }
    }

    // ─── Helper: authenticated request with auto-refresh ──────────────────────

    private suspend fun <T> authenticatedRequest(block: suspend (String) -> T): Result<T> {
        val accessToken = tokenStorage.getAccessToken()
            ?: return Result.failure(Exception("Not authenticated"))

        return try {
            Result.success(block(accessToken))
        } catch (e: Exception) {
            val refreshToken = tokenStorage.getRefreshToken()
                ?: run {
                    tokenStorage.clearTokens()
                    return Result.failure(Exception("Session expired. Please log in again."))
                }

            val refreshResult = refreshToken(refreshToken)
            if (refreshResult.isSuccess) {
                val newTokens = refreshResult.getOrNull()!!
                tokenStorage.saveTokens(newTokens.accessToken, newTokens.refreshToken)
                try {
                    Result.success(block(newTokens.accessToken))
                } catch (retryException: Exception) {
                    tokenStorage.clearTokens()
                    Result.failure(Exception("Session expired. Please log in again."))
                }
            } else {
                tokenStorage.clearTokens()
                Result.failure(Exception("Session expired. Please log in again."))
            }
        }
    }
}