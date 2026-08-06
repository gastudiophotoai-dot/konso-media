package com.example.ui.navigation

sealed class Screen(val route: String, val titleEN: String, val titleAM: String) {
    object Home : Screen("home", "Home", "የቤት ገጽ")
    object Dashboard : Screen("dashboard", "Dashboard & Wallet", "ዳሽቦርድ እና ዋሌት")
    object ClientCampaigns : Screen("campaigns", "Campaigns", "ዘመቻዎች (Campaigns)")
    object AdminPanel : Screen("admin", "Admin Panel", "የAdmin Panel")
    object Security : Screen("security", "Security & VPN", "የደህንነት ሲስተም")
    object Legal : Screen("legal", "Legal & AML", "የህግ ገጾች (Legal)")
    object Auth : Screen("auth", "Log In / Register", "መግቢያ / መመዝገቢያ")
    object Profile : Screen("profile", "Profile", "ፕሮፋይል")
}
