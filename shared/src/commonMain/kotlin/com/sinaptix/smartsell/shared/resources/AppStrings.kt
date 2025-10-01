package com.sinaptix.smartsell.shared.resources

import smartsell.shared.generated.resources.Res
import smartsell.shared.generated.resources.advice_update_success
import smartsell.shared.generated.resources.app_name
import smartsell.shared.generated.resources.app_name_first
import smartsell.shared.generated.resources.app_name_phrase
import smartsell.shared.generated.resources.app_name_second
import smartsell.shared.generated.resources.auth_continue
import smartsell.shared.generated.resources.auth_logging_in_google
import smartsell.shared.generated.resources.auth_success
import smartsell.shared.generated.resources.auth_waith
import smartsell.shared.generated.resources.button_update
import smartsell.shared.generated.resources.descript_icon_add
import smartsell.shared.generated.resources.descript_icon_back_arrow
import smartsell.shared.generated.resources.descript_icon_button
import smartsell.shared.generated.resources.descript_icon_checkmark
import smartsell.shared.generated.resources.descript_icon_close
import smartsell.shared.generated.resources.descript_icon_drawer_icon
import smartsell.shared.generated.resources.descript_icon_error
import smartsell.shared.generated.resources.descript_icon_google_logo
import smartsell.shared.generated.resources.descript_icon_menu
import smartsell.shared.generated.resources.descript_img_flag
import smartsell.shared.generated.resources.error_dial_code_not_found
import smartsell.shared.generated.resources.error_generic
import smartsell.shared.generated.resources.error_network
import smartsell.shared.generated.resources.error_not_connection
import smartsell.shared.generated.resources.error_not_firebase_token
import smartsell.shared.generated.resources.error_signin_cancel
import smartsell.shared.generated.resources.error_unknow
import smartsell.shared.generated.resources.error_user_not_found
import smartsell.shared.generated.resources.error_user_unavailable
import smartsell.shared.generated.resources.nav_admin_panel
import smartsell.shared.generated.resources.nav_blog
import smartsell.shared.generated.resources.nav_categories
import smartsell.shared.generated.resources.nav_contact
import smartsell.shared.generated.resources.nav_home
import smartsell.shared.generated.resources.nav_location
import smartsell.shared.generated.resources.nav_products
import smartsell.shared.generated.resources.nav_profile
import smartsell.shared.generated.resources.nav_sales
import smartsell.shared.generated.resources.nav_settings
import smartsell.shared.generated.resources.nav_sign_out
import smartsell.shared.generated.resources.pick_alert_button_cancel
import smartsell.shared.generated.resources.pick_alert_button_confirm
import smartsell.shared.generated.resources.pick_alert_title
import smartsell.shared.generated.resources.placeholder_address
import smartsell.shared.generated.resources.placeholder_city
import smartsell.shared.generated.resources.placeholder_dial_code
import smartsell.shared.generated.resources.placeholder_email
import smartsell.shared.generated.resources.placeholder_firstname
import smartsell.shared.generated.resources.placeholder_lastname
import smartsell.shared.generated.resources.placeholder_phone_number
import smartsell.shared.generated.resources.placeholder_zip
import smartsell.shared.generated.resources.title_admin_panel
import smartsell.shared.generated.resources.title_cart
import smartsell.shared.generated.resources.title_categories
import smartsell.shared.generated.resources.title_home
import smartsell.shared.generated.resources.title_manage_product_edit
import smartsell.shared.generated.resources.title_manage_product_new
import smartsell.shared.generated.resources.title_my_profile

object AppStrings {
    object AppName {
        val appName = Res.string.app_name
        val appNameFirst = Res.string.app_name_first
        val appNameSecond = Res.string.app_name_second
        val appNamePhrase = Res.string.app_name_phrase
    }

    object Navigation {
        val home = Res.string.nav_home
        val products = Res.string.nav_products
        val sales = Res.string.nav_sales
        val profile = Res.string.nav_profile
        val settings = Res.string.nav_settings
        val categories = Res.string.nav_categories
        val blog = Res.string.nav_blog
        val location = Res.string.nav_location
        val contact = Res.string.nav_contact
        val signOut = Res.string.nav_sign_out
        val adminPanel = Res.string.nav_admin_panel
    }

    object Auth {
        val authSuccess = Res.string.auth_success
        val authLoggingInGoogle = Res.string.auth_logging_in_google
        val authWaith = Res.string.auth_waith
        val authContinue = Res.string.auth_continue
    }

    object Titles {
        val titleHome = Res.string.title_home
        val titleCategorie = Res.string.title_categories
        val titleCart = Res.string.title_cart
        val titleMyProfile = Res.string.title_my_profile
        val titleAdminPanel = Res.string.title_admin_panel
        val titleManageProductEdit = Res.string.title_manage_product_edit
        val titleManageProductNew = Res.string.title_manage_product_new
    }

    object Descriptions {
        val descriptIconClose = Res.string.descript_icon_close
        val descriptIconMenu = Res.string.descript_icon_menu
        val descriptIconBackArrow = Res.string.descript_icon_back_arrow
        val descriptIconDrawerIcon = Res.string.descript_icon_drawer_icon
        val descriptIconGoogleLogo = Res.string.descript_icon_google_logo
        val descriptIconButton = Res.string.descript_icon_button
        val descriptIconCheckmark = Res.string.descript_icon_checkmark
        val descriptImgFlag = Res.string.descript_img_flag
        val descriptIconError = Res.string.descript_icon_error
        val descriptIconAdd = Res.string.descript_icon_add
    }

    object Errors {
        val errorNetwork = Res.string.error_network
        val errorNotConnection = Res.string.error_not_connection
        val errorNotFirebaseToken = Res.string.error_not_firebase_token
        val errorSigninCancel = Res.string.error_signin_cancel
        val errorUnknow = Res.string.error_unknow
        val errorDialCodeNotFound = Res.string.error_dial_code_not_found
        val errorUserNotFound = Res.string.error_user_not_found
        val errorUserUnavailable = Res.string.error_user_unavailable
        val errorGeneric = Res.string.error_generic
    }

    object Advices {
        val adviceUpdateSuccess = Res.string.advice_update_success
    }

    object PlaceHolder {
        val placeholderFirstName = Res.string.placeholder_firstname
        val placeholderLastName = Res.string.placeholder_lastname
        val placeholderEmail = Res.string.placeholder_email
        val placeholderCity = Res.string.placeholder_city
        val placeholderZip = Res.string.placeholder_zip
        val placeholderAddress = Res.string.placeholder_address
        val placeholderPhoneNumber = Res.string.placeholder_phone_number
        val placeholderDialCode = Res.string.placeholder_dial_code
    }

    object PickAlert {
        val pickAlertTitle = Res.string.pick_alert_title
        val pickAlertButtonConfirm = Res.string.pick_alert_button_confirm
        val pickAlertButtonCancel = Res.string.pick_alert_button_cancel
    }

    object Buttons {
        val buttonUpdate = Res.string.button_update
    }
}
