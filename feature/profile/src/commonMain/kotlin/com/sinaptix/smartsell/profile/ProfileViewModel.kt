package com.sinaptix.smartsell.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinaptix.smartsell.data.domain.CustomerRepository
import com.sinaptix.smartsell.shared.domain.Country
import com.sinaptix.smartsell.shared.domain.Customer
import com.sinaptix.smartsell.shared.domain.PhoneNumber
import com.sinaptix.smartsell.shared.util.RequestState
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    var screenState: RequestState<Unit> by mutableStateOf(RequestState.Loading)
        private set
    var uiState: UIState by mutableStateOf(UIState())
        private set

    val isFormValid: Boolean
        get() = with(uiState) {
            firstName.length in 3..50 &&
                    lastName.length in 3..50 &&
                    (city == null || city.length in 3..50) &&
                    (zip == null || zip.toString().length in 3..8) &&
                    (address == null || address.length in 3..50) &&
                    (phoneNumber == null || phoneNumber.number.length in 5..30)
        }

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            screenState = RequestState.Loading
            val result = customerRepository.getMyProfile()
            if (result.isSuccess()) {
                val fetchedCustomer = result.getSuccessData()
                uiState = UIState(
                    id = fetchedCustomer.id,
                    firstName = fetchedCustomer.firstName,
                    lastName = fetchedCustomer.lastName,
                    email = fetchedCustomer.email,
                    city = fetchedCustomer.city,
                    zip = fetchedCustomer.zip,
                    address = fetchedCustomer.address,
                    country = Country.entries.firstOrNull { country ->
                        country.dialCode == fetchedCustomer.phoneNumber?.dialCode
                    } ?: Country.Mexico,
                    phoneNumber = fetchedCustomer.phoneNumber,
                )
                screenState = RequestState.Success(Unit)
            } else if (result.isError()) {
                screenState = RequestState.Error(message = result.getErrorMessage())
            }
        }
    }

    fun setFirstName(value: String) {
        uiState = uiState.copy(firstName = value)
    }

    fun setLastName(value: String) {
        uiState = uiState.copy(lastName = value)
    }

    fun setCity(value: String) {
        uiState = uiState.copy(city = value)
    }

    fun setZip(value: Int?) {
        uiState = uiState.copy(zip = value)
    }

    fun setAddress(value: String) {
        uiState = uiState.copy(address = value)
    }

    fun setCountry(value: Country) {
        uiState = uiState.copy(
            country = value,
            phoneNumber = uiState.phoneNumber?.copy(
                dialCode = value.dialCode
            )
        )
    }

    fun setPhoneNumber(value: String) {
        uiState = uiState.copy(
            phoneNumber = PhoneNumber(
                dialCode = uiState.country.dialCode,
                number = value
            )
        )
    }

    fun updateProfile(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            val result = customerRepository.updateProfile(
                customer = Customer(
                    id = uiState.id,
                    firstName = uiState.firstName,
                    lastName = uiState.lastName,
                    email = uiState.email,
                    city = uiState.city,
                    zip = uiState.zip,
                    address = uiState.address,
                    phoneNumber = uiState.phoneNumber
                )
            )
            if (result.isSuccess()) {
                onSuccess()
            } else {
                onError(result.getErrorMessage())
            }
        }
    }
}

data class UIState(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val city: String? = null,
    val zip: Int? = null,
    val address: String? = null,
    val country: Country = Country.Mexico,
    val phoneNumber: PhoneNumber? = null,
)