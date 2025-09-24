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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    var screenState: RequestState<Unit> by mutableStateOf(RequestState.Loading)
    var uiState: UIState by mutableStateOf(UIState())
        private set

    val isFormValid: Boolean
        get() = with(uiState) {
            firstName.length in 3..50 &&
                    lastName.length in 3..50 &&
                    city?.length in 3..50 &&
                    zip != null || zip?.toString()?.length in 3..8 &&
                    address?.length in 3..50 &&
                    phoneNumber?.number?.length in 5..30
        }

    init {
        viewModelScope.launch {
            customerRepository.readCustomerFlow().collectLatest { data ->
                if (data.isSuccess()) {
                    val fetchesCustomer = data.getSuccessData()

                    uiState = UIState(
                        id = fetchesCustomer.id,
                        firstName = fetchesCustomer.firstName,
                        lastName = fetchesCustomer.lastName,
                        email = fetchesCustomer.email,
                        city = fetchesCustomer.city,
                        zip = fetchesCustomer.zip,
                        address = fetchesCustomer.address,
                        country = Country.entries.firstOrNull() { country ->
                            country.dialCode == fetchesCustomer.phoneNumber?.dialCode
                        } ?: Country.Mexico,
                        phoneNumber = fetchesCustomer.phoneNumber,
                    )
                    screenState = RequestState.Success(Unit)
                } else if (data.isError()) {
                    screenState = RequestState.Error(message = data.getErrorMessage())
                }
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

    fun setCustomer(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            customerRepository.setCustomer(
                customer = Customer(
                    id = uiState.id,
                    firstName = uiState.firstName,
                    lastName = uiState.lastName,
                    email = uiState.email,
                    city = uiState.city,
                    zip = uiState.zip,
                    address = uiState.address,
                    phoneNumber = uiState.phoneNumber
                ),
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }

    fun updateCustomer(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            customerRepository.updateCustomer(
                customer = Customer(
                    id = uiState.id,
                    firstName = uiState.firstName,
                    lastName = uiState.lastName,
                    email = uiState.email,
                    city = uiState.city,
                    zip = uiState.zip,
                    address = uiState.address,
                    phoneNumber = uiState.phoneNumber
                ),
                onSuccess = onSuccess,
                onError = onError
            )
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