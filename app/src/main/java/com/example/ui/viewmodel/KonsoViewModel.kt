package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.KonsoDatabase
import com.example.data.model.CampaignEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserEntity
import com.example.data.repository.KonsoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class KonsoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: KonsoRepository

    val currentUser: StateFlow<UserEntity?>
    val transactions: StateFlow<List<TransactionEntity>>
    val campaigns: StateFlow<List<CampaignEntity>>
    val notifications: StateFlow<List<NotificationEntity>>

    // Auth & User Session State
    private val _isLoggedIn = MutableStateFlow(true) // Default logged in for smooth preview
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _authEmailOrPhone = MutableStateFlow("gelegezusha@gmail.com") // Official Admin
    val authEmailOrPhone: StateFlow<String> = _authEmailOrPhone.asStateFlow()

    private val _isAdminVerified = MutableStateFlow(true)
    val isAdminVerified: StateFlow<Boolean> = _isAdminVerified.asStateFlow()

    // UI Local State
    private val _currentLanguage = MutableStateFlow("EN") // "EN" or "AM" (Amharic)
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private val _activeRole = MutableStateFlow("USER") // "USER", "CLIENT", "ADMIN"
    val activeRole: StateFlow<String> = _activeRole.asStateFlow()

    private val _isVpnDetected = MutableStateFlow(false)
    val isVpnDetected: StateFlow<Boolean> = _isVpnDetected.asStateFlow()

    private val _isDeviceTrusted = MutableStateFlow(true)
    val isDeviceTrusted: StateFlow<Boolean> = _isDeviceTrusted.asStateFlow()

    private val _showCaptchaModal = MutableStateFlow(false)
    val showCaptchaModal: StateFlow<Boolean> = _showCaptchaModal.asStateFlow()

    private val _showOtpModal = MutableStateFlow(false)
    val showOtpModal: StateFlow<Boolean> = _showOtpModal.asStateFlow()

    private val _pendingActionName = MutableStateFlow("")
    val pendingActionName: StateFlow<String> = _pendingActionName.asStateFlow()

    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage.asStateFlow()

    init {
        val db = KonsoDatabase.getDatabase(application)
        repository = KonsoRepository(db)

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }

        currentUser = repository.getUser("USR-001")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

        transactions = repository.getAllTransactions()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        campaigns = repository.getAllCampaigns()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        notifications = repository.getAllNotifications()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == "EN") "AM" else "EN"
    }

    fun switchRole(role: String) {
        if (role == "ADMIN") {
            val inputClean = _authEmailOrPhone.value.trim().lowercase().replace(" ", "").replace("+251", "0")
            val isOfficialAdmin = inputClean == "0912702062" || inputClean == "gelegezusha@gmail.com"
            if (!_isAdminVerified.value && !isOfficialAdmin) {
                _uiMessage.value = "⛔ የአስተዳዳሪ ገጽ መግባት አልተፈቀደም! እባክዎን በ 0912702062 ወይም gelegezusha@gmail.com ይግቡ።"
                return
            }
            _isAdminVerified.value = true
        }
        _activeRole.value = role
    }

    fun toggleVpnSimulation() {
        _isVpnDetected.value = !_isVpnDetected.value
        if (_isVpnDetected.value) {
            viewModelScope.launch {
                repository.addNotification(
                    NotificationEntity(
                        title = "Security Alert: VPN/Proxy Detected 🛡️",
                        message = "Suspicious IP routing detected. Some automatic withdrawals may require manual verification.",
                        type = "SECURITY"
                    )
                )
            }
        }
    }

    fun triggerActionWithSecurity(actionName: String, onVerified: () -> Unit) {
        _pendingActionName.value = actionName
        _showCaptchaModal.value = true
    }

    fun onCaptchaSolved() {
        _showCaptchaModal.value = false
        _showOtpModal.value = true
    }

    fun dismissCaptcha() {
        _showCaptchaModal.value = false
    }

    fun onOtpVerified() {
        _showOtpModal.value = false
        _uiMessage.value = "Security verification passed for ${_pendingActionName.value}! ✅"
    }

    fun dismissOtp() {
        _showOtpModal.value = false
    }

    fun clearUiMessage() {
        _uiMessage.value = null
    }

    fun depositMoney(method: String, amount: Double, referenceTxId: String) {
        viewModelScope.launch {
            val tx = TransactionEntity(
                userId = "USR-001",
                type = "DEPOSIT",
                paymentMethod = method,
                amount = amount,
                fee = 0.0,
                status = "COMPLETED",
                referenceId = referenceTxId.ifBlank { "DEP-${System.currentTimeMillis() % 100000}" },
                notes = "Deposit via $method"
            )
            repository.addTransaction(tx)
            _uiMessage.value = "Successfully deposited ${String.format("%.2f", amount)} ETB via $method!"
        }
    }

    fun withdrawMoney(method: String, amount: Double, accountNumber: String) {
        val currentBal = currentUser.value?.balance ?: 0.0
        val fee = when (method) {
            "Telebirr" -> 3.0
            "CBE Birr" -> 5.0
            "M-PESA" -> 4.0
            else -> 10.0
        }
        if (currentBal < (amount + fee)) {
            _uiMessage.value = "Insufficient wallet balance! Minimum balance required including fee."
            return
        }

        viewModelScope.launch {
            val tx = TransactionEntity(
                userId = "USR-001",
                type = "WITHDRAWAL",
                paymentMethod = method,
                amount = amount,
                fee = fee,
                status = "PENDING", // PENDING for admin approval or instant processing
                referenceId = "WTH-${System.currentTimeMillis() % 100000}",
                notes = "Withdrawal request to $accountNumber ($method)"
            )
            repository.addTransaction(tx)
            _uiMessage.value = "Withdrawal request of ${String.format("%.2f", amount)} ETB submitted for processing!"
        }
    }

    fun createCampaign(
        clientName: String,
        platform: String,
        actionType: String,
        link: String,
        quantity: Int,
        pricePerUnit: Double,
        country: String
    ) {
        val totalCost = quantity * pricePerUnit
        val currentBal = currentUser.value?.balance ?: 0.0

        if (currentBal < totalCost) {
            _uiMessage.value = "Insufficient balance! Please deposit to launch this campaign."
            return
        }

        viewModelScope.launch {
            val campaign = CampaignEntity(
                clientName = clientName.ifBlank { "Konso Advertiser" },
                platform = platform,
                actionType = actionType,
                targetLink = link,
                targetQuantity = quantity,
                currentQuantity = 0,
                pricePerUnit = pricePerUnit,
                totalBudget = totalCost,
                targetCountry = country,
                status = "ACTIVE"
            )
            repository.createCampaign(campaign, "USR-001")
            _uiMessage.value = "Campaign launched successfully for $platform ($quantity $actionType)!"
        }
    }

    fun completeTask(campaign: CampaignEntity) {
        val reward = campaign.pricePerUnit * 0.70 // 70% share to earner
        viewModelScope.launch {
            repository.completeTask(campaign.id, "USR-001", reward)
            _uiMessage.value = "Task completed! Earned +${String.format("%.2f", reward)} ETB!"
        }
    }

    // Admin Actions
    fun approveTransaction(txId: Long) {
        viewModelScope.launch {
            repository.updateTransactionStatus(txId, "COMPLETED")
            _uiMessage.value = "Transaction #$txId approved successfully!"
        }
    }

    fun rejectTransaction(txId: Long) {
        viewModelScope.launch {
            repository.updateTransactionStatus(txId, "REJECTED")
            _uiMessage.value = "Transaction #$txId rejected."
        }
    }

    fun updateCampaignStatus(campaignId: Long, status: String) {
        viewModelScope.launch {
            repository.updateCampaignStatus(campaignId, status)
            _uiMessage.value = "Campaign #$campaignId set to $status."
        }
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }

    // Authentication Actions
    fun login(emailOrPhone: String, role: String) {
        val inputClean = emailOrPhone.trim().lowercase().replace(" ", "").replace("+251", "0")
        val isOfficialAdmin = inputClean == "0912702062" || inputClean == "gelegezusha@gmail.com"

        _authEmailOrPhone.value = emailOrPhone
        _activeRole.value = role
        _isLoggedIn.value = true
        _isAdminVerified.value = (role == "ADMIN" && isOfficialAdmin) || isOfficialAdmin

        if (role == "ADMIN") {
            _uiMessage.value = "Welcome Admin owner! Access granted to Konso Media Control Center 👑"
        } else {
            _uiMessage.value = "Welcome back! Logged in as $role mode ✅"
        }
    }

    fun register(fullName: String, emailOrPhone: String, role: String) {
        val inputClean = emailOrPhone.trim().lowercase().replace(" ", "").replace("+251", "0")
        val isOfficialAdmin = inputClean == "0912702062" || inputClean == "gelegezusha@gmail.com"

        _authEmailOrPhone.value = emailOrPhone
        _activeRole.value = role
        _isLoggedIn.value = true
        _isAdminVerified.value = (role == "ADMIN" && isOfficialAdmin) || isOfficialAdmin

        _uiMessage.value = "Account created successfully for $fullName! Welcome to Konso Media ✅"
    }

    fun logout() {
        _isLoggedIn.value = false
        _isAdminVerified.value = false
        _activeRole.value = "USER"
        _uiMessage.value = "Logged out successfully (በስኬት ወጥተዋል) 👋"
    }

    fun updateUserProfile(
        name: String,
        email: String,
        phone: String,
        service: String,
        payout: String,
        biometricsEnabled: Boolean
    ) {
        _authEmailOrPhone.value = email.ifBlank { phone }
        val current = currentUser.value
        if (current != null) {
            val updated = current.copy(
                name = name.ifBlank { current.name },
                email = email.ifBlank { current.email },
                phone = phone.ifBlank { current.phone }
            )
            viewModelScope.launch {
                repository.createOrUpdateUser(updated)
            }
        }
        _uiMessage.value = "Profile updated successfully! (ፕሮፋይልዎ በስኬት ተስተካክሏል) ✅"
    }
}
