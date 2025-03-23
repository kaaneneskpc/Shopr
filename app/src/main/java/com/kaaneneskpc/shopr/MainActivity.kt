package com.kaaneneskpc.shopr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kaaneneskpc.domain.model.UserProfile
import com.kaaneneskpc.shopr.model.UiProductModel
import com.kaaneneskpc.shopr.navigation.AllProductsScreen
import com.kaaneneskpc.shopr.navigation.CartScreen
import com.kaaneneskpc.shopr.navigation.CartSummaryScreen
import com.kaaneneskpc.shopr.navigation.EditProfileRoute
import com.kaaneneskpc.shopr.navigation.HomeScreen
import com.kaaneneskpc.shopr.navigation.LoginScreen
import com.kaaneneskpc.shopr.navigation.OrdersScreen
import com.kaaneneskpc.shopr.navigation.PaymentResultScreen
import com.kaaneneskpc.shopr.navigation.PaymentScreen
import com.kaaneneskpc.shopr.navigation.PaymentVerificationScreen
import com.kaaneneskpc.shopr.navigation.ProductDetails
import com.kaaneneskpc.shopr.navigation.ProfileScreen
import com.kaaneneskpc.shopr.navigation.RegisterScreen
import com.kaaneneskpc.shopr.navigation.UserAddressRoute
import com.kaaneneskpc.shopr.navigation.UserAddressRouteWrapper
import com.kaaneneskpc.shopr.navigation.WishlistsScreen
import com.kaaneneskpc.shopr.navigation.navTypes.productNavType
import com.kaaneneskpc.shopr.navigation.navTypes.userAddressNavType
import com.kaaneneskpc.shopr.navigation.navTypes.userProfileNavType
import com.kaaneneskpc.shopr.ui.feature.account.login.LoginScreen
import com.kaaneneskpc.shopr.ui.feature.account.register.RegisterScreen
import com.kaaneneskpc.shopr.ui.feature.cart.CartScreen
import com.kaaneneskpc.shopr.ui.feature.home.HomeScreen
import com.kaaneneskpc.shopr.ui.feature.orders.OrdersScreen
import com.kaaneneskpc.shopr.ui.feature.payment.PaymentResultScreen
import com.kaaneneskpc.shopr.ui.feature.payment.PaymentScreen
import com.kaaneneskpc.shopr.ui.feature.payment.PaymentVerificationScreen
import com.kaaneneskpc.shopr.ui.feature.productDetails.ProductDetailsScreen
import com.kaaneneskpc.shopr.ui.feature.profile.ProfileScreen
import com.kaaneneskpc.shopr.ui.feature.profile.edit.EditProfileScreen
import com.kaaneneskpc.shopr.ui.feature.summary.CartSummaryScreen
import com.kaaneneskpc.shopr.ui.feature.userAddress.UserAddressScreen
import com.kaaneneskpc.shopr.ui.feature.wishlist.WishlistScreen
import com.kaaneneskpc.shopr.ui.theme.Blue
import com.kaaneneskpc.shopr.ui.theme.ShoprTheme
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoprTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val bottomNavItems = listOf(
        BottomNavItems.Home,
        BottomNavItems.Cart,
        BottomNavItems.Wishlist,
        BottomNavItems.Order,
        BottomNavItems.Profile
    )

    val bottomNavRoutes = bottomNavItems.map { it.route::class.qualifiedName }

    val shouldShowBottomNav = rememberSaveable { mutableStateOf(true) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    shouldShowBottomNav.value = currentDestination?.route?.let { route ->
        bottomNavRoutes.contains(route.substringBefore("?"))
    } ?: false

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(visible = shouldShowBottomNav.value, enter = fadeIn()) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentDestination?.hierarchy?.any {
                            it.route?.substringBefore("?") == item.route::class.qualifiedName
                        } ?: false

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }

                                if (item is BottomNavItems.Cart) {
                                    val cartViewModel: com.kaaneneskpc.shopr.ui.feature.cart.CartViewModel by org.koin.java.KoinJavaComponent.inject(
                                        com.kaaneneskpc.shopr.ui.feature.cart.CartViewModel::class.java
                                    )
                                    cartViewModel.refreshCart()
                                }
                            },
                            label = { Text(text = item.title) },
                            icon = {
                                Image(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(
                                        if (isSelected) Blue else Color.LightGray
                                    )
                                )
                            },
                            colors = NavigationBarItemDefaults.colors().copy(
                                selectedIconColor = Blue,
                                selectedTextColor = Blue,
                                unselectedTextColor = Color.Gray,
                                unselectedIconColor = Color.Gray
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = if (ShoprSession.getUser() != null) {
                    HomeScreen
                } else {
                    LoginScreen
                }
            ) {
                composable<LoginScreen> {
                    shouldShowBottomNav.value = false
                    LoginScreen(navController)
                }
                composable<RegisterScreen> {
                    shouldShowBottomNav.value = false
                    RegisterScreen(navController)
                }
                composable<HomeScreen> {
                    HomeScreen(navController)
                }
                composable<CartScreen> {
                    CartScreen(navController)
                }
                composable<OrdersScreen> {
                    OrdersScreen()
                }
                composable<ProfileScreen> {
                    ProfileScreen(navController)
                }
                composable<CartSummaryScreen> {
                    CartSummaryScreen(navController = navController)
                }
                composable<AllProductsScreen> {
                    com.kaaneneskpc.shopr.ui.feature.allproducts.AllProductsScreen(navController = navController)
                }
                composable<ProductDetails>(
                    typeMap = mapOf(typeOf<UiProductModel>() to productNavType)
                ) { typeMap ->
                    val productRoute = typeMap.toRoute<ProductDetails>()
                    ProductDetailsScreen(navController, productRoute.product)
                }
                composable<UserAddressRoute>(
                    typeMap = mapOf(typeOf<UserAddressRouteWrapper>() to userAddressNavType)
                ) { typeMap ->
                    val userAddressRoute = typeMap.toRoute<UserAddressRoute>()
                    UserAddressScreen(
                        navController = navController,
                        userAddress = userAddressRoute.userAddressWrapper.userAddress
                    )
                }
                composable<EditProfileRoute>(
                    typeMap = mapOf(typeOf<UserProfile>() to userProfileNavType)
                ) { typeMap ->
                    val editProfileRoute = typeMap.toRoute<EditProfileRoute>()
                    EditProfileScreen(
                        navController = navController,
                        userProfile = editProfileRoute.userProfile
                    )
                }
                composable<WishlistsScreen> {
                    WishlistScreen(
                        navController = navController
                    )
                }
                composable<PaymentScreen> {
                    val paymentViewModel =
                        androidx.lifecycle.viewmodel.compose.viewModel<com.kaaneneskpc.shopr.ui.feature.payment.PaymentViewModel>()
                    com.kaaneneskpc.shopr.ui.feature.payment.PaymentScreen(
                        navController = navController,
                        viewModel = paymentViewModel
                    )
                }
                composable<PaymentVerificationScreen> {
                    val paymentViewModel = navController.previousBackStackEntry?.let {
                        androidx.lifecycle.viewmodel.compose.viewModel<com.kaaneneskpc.shopr.ui.feature.payment.PaymentViewModel>(
                            viewModelStoreOwner = it
                        )
                    } ?: androidx.lifecycle.viewmodel.compose.viewModel()

                    PaymentVerificationScreen(
                        navController = navController,
                        viewModel = paymentViewModel
                    )
                }
                composable<PaymentResultScreen> {
                    val paymentViewModel = navController.previousBackStackEntry?.let {
                        androidx.lifecycle.viewmodel.compose.viewModel<com.kaaneneskpc.shopr.ui.feature.payment.PaymentViewModel>(
                            viewModelStoreOwner = it
                        )
                    } ?: androidx.lifecycle.viewmodel.compose.viewModel()

                    PaymentResultScreen(
                        navController = navController,
                        paymentViewModel = paymentViewModel
                    )
                }
            }
        }
    }
}

sealed class BottomNavItems(val route: Any, val title: String, val icon: Int) {
    data object Home : BottomNavItems(HomeScreen, "Home", icon = R.drawable.ic_home)
    data object Cart : BottomNavItems(CartScreen, "Cart", icon = R.drawable.ic_cart)
    data object Wishlist :
        BottomNavItems(WishlistsScreen, "Wishlist", icon = R.drawable.ic_favorite)

    data object Order : BottomNavItems(OrdersScreen, "Orders", icon = R.drawable.ic_orders)
    data object Profile : BottomNavItems(ProfileScreen, "Profile", icon = R.drawable.ic_profile_bn)
}