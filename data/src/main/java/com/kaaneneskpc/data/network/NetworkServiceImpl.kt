package com.kaaneneskpc.data.network

import com.kaaneneskpc.data.model.request.AddToCartRequest
import com.kaaneneskpc.data.model.request.AddressDataModel
import com.kaaneneskpc.data.model.request.LoginRequest
import com.kaaneneskpc.data.model.request.RegisterRequest
import com.kaaneneskpc.data.model.response.cart.CartResponse
import com.kaaneneskpc.data.model.response.cart.CartSummaryResponse
import com.kaaneneskpc.data.model.response.category.CategoryListResponse
import com.kaaneneskpc.data.model.response.orders.OrdersListResponse
import com.kaaneneskpc.data.model.response.orders.PlaceOrderResponse
import com.kaaneneskpc.data.model.response.product.ProductListResponse
import com.kaaneneskpc.data.model.response.user.UserAuthResponse
import com.kaaneneskpc.data.model.response.user.UserResponse
import com.kaaneneskpc.domain.model.AddressDomainModel
import com.kaaneneskpc.domain.model.CartItemModel
import com.kaaneneskpc.domain.model.CartModel
import com.kaaneneskpc.domain.model.CartSummary
import com.kaaneneskpc.domain.model.CategoryListModel
import com.kaaneneskpc.domain.model.OrdersListModel
import com.kaaneneskpc.domain.model.ProductListModel
import com.kaaneneskpc.domain.model.UserDomainModel
import com.kaaneneskpc.domain.model.request.AddCartRequestModel
import com.kaaneneskpc.domain.network.NetworkService
import com.kaaneneskpc.domain.network.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException

class NetworkServiceImpl(val client: HttpClient) : NetworkService {
    private val baseUrl = "https://ecommerce-ktor-4641e7ff1b63.herokuapp.com/v2"
    override suspend fun getProducts(category: Int?): ResultWrapper<ProductListModel> {
        val url = if (category != null) "$baseUrl/products/category/$category" else "$baseUrl/products"
        return makeWebRequest(url = url,
            method = HttpMethod.Get,
            mapper = { dataModels: ProductListResponse ->
                dataModels.toProductList()
            })
    }

    override suspend fun getCategories(): ResultWrapper<CategoryListModel> {
        val url = "$baseUrl/categories"
        return makeWebRequest(url = url,
            method = HttpMethod.Get,
            mapper = { categories: CategoryListResponse ->
                categories.toCategoryList()
            })
    }

    override suspend fun addProductToCart(request: AddCartRequestModel): ResultWrapper<CartModel> {
        val url = "$baseUrl/cart/1"
        return makeWebRequest(url = url,
            method = HttpMethod.Post,
            body = AddToCartRequest.fromCartRequestModel(request),
            mapper = { cartItem: CartResponse ->
                cartItem.toCartModel()
            })
    }

    override suspend fun getCart(): ResultWrapper<CartModel> {
        val url = "$baseUrl/cart/1"
        return makeWebRequest(url = url,
            method = HttpMethod.Get,
            mapper = { cartItem: CartResponse ->
                cartItem.toCartModel()
            })
    }

    override suspend fun updateQuantity(cartItemModel: CartItemModel): ResultWrapper<CartModel> {
        val url = "$baseUrl/cart/1/${cartItemModel.id}"
        return makeWebRequest(url = url,
            method = HttpMethod.Put,
            body = AddToCartRequest(
                productId = cartItemModel.productId,
                quantity = cartItemModel.quantity
            ),
            mapper = { cartItem: CartResponse ->
                cartItem.toCartModel()
            })
    }

    override suspend fun deleteItem(cartItemId: Int, userId: Int): ResultWrapper<CartModel> {
        val url = "$baseUrl/cart/$userId/$cartItemId"
        return makeWebRequest(url = url,
            method = HttpMethod.Delete,
            mapper = { cartItem: CartResponse ->
                cartItem.toCartModel()
            })
    }

    override suspend fun getCartSummary(userId: Int): ResultWrapper<CartSummary> {
        val url = "$baseUrl/checkout/$userId/summary"
        return makeWebRequest(url = url,
            method = HttpMethod.Get,
            mapper = { cartSummary: CartSummaryResponse ->
                cartSummary.toCartSummary()
            })
    }

    override suspend fun placeOrder(address: AddressDomainModel, userId: Int): ResultWrapper<Long> {
        val dataModel = AddressDataModel.fromDomainAddress(address)
        val url = "$baseUrl/orders/$userId"
        return makeWebRequest(url = url,
            method = HttpMethod.Post,
            body = dataModel,
            mapper = { orderRes: PlaceOrderResponse ->
                orderRes.data.id
            })
    }

    override suspend fun getOrderList(): ResultWrapper<OrdersListModel> {
        val url = "$baseUrl/orders/1"
        return makeWebRequest(url = url,
            method = HttpMethod.Get,
            mapper = { ordersResponse: OrdersListResponse ->
                ordersResponse.toDomainResponse()
            })
    }

    override suspend fun login(email: String, password: String): ResultWrapper<UserDomainModel> {
        val url = "$baseUrl/login"
        return makeWebRequest(url = url,
            method = HttpMethod.Post,
            body = LoginRequest(email, password),
            mapper = { user: UserAuthResponse ->
                user.data.toDomainModel()
            })
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): ResultWrapper<UserDomainModel> {
        val url = "$baseUrl/signup"
        return makeWebRequest(url = url,
            method = HttpMethod.Post,
            body = RegisterRequest(email, password, name),
            mapper = { user: UserAuthResponse ->
                user.data.toDomainModel()
            })
    }


    suspend inline fun <reified T, R> makeWebRequest(
        url: String,
        method: HttpMethod,
        body: Any? = null,
        headers: Map<String, String> = emptyMap(),
        parameters: Map<String, String> = emptyMap(),
        noinline mapper: ((T) -> R)? = null
    ): ResultWrapper<R> {
        return try {
            val response = client.request(url) {
                this.method = method
                url {
                    this.parameters.appendAll(Parameters.build {
                        parameters.forEach { (key, value) ->
                            append(key, value)
                        }
                    })
                }
                headers.forEach { (key, value) ->
                    header(key, value)
                }
                if (body != null) {
                    setBody(body)
                }

                contentType(ContentType.Application.Json)
            }.body<T>()
            val result: R = mapper?.invoke(response) ?: response as R
            ResultWrapper.Success(result)
        } catch (e: ClientRequestException) {
            ResultWrapper.Failure(e)
        } catch (e: ServerResponseException) {
            ResultWrapper.Failure(e)
        } catch (e: IOException) {
            ResultWrapper.Failure(e)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

}