package com.sinaptix.smartsell.shared

import androidx.compose.ui.graphics.Color

// ===== COLORES BASE (Sage Green Palette) - INMUTABLES =====
val SageGreen = Color(0xFF819A91)       // Verde salvia principal
val SoftSageGreen = Color(0xFFA7C1A8)   // Verde salvia suave
val SageMist = Color(0xFFD1D8BE)        // Niebla salvia
val CreameWhite = Color(0xFFEEEFE0)     // Blanco cremoso

// ===== GRISES DERIVADOS DE LA PALETA BASE =====
val GrayLighter = Color(0xFFF8F9F6)     // Derivado de CreameWhite más claro
val Gray = Color(0xFFE8EBDF)            // Derivado de SageMist más gris
val GrayDarker = Color(0xFFD5D9CC)      // Entre SageMist y tonos grises

// ===== COLORES SISTEMA DERIVADOS DE SAGE =====
val Yellowish = Color(0xFFCBD4A0)       // Amarillo derivado de SageMist + tinte cálido
val Orange = Color(0xFFB8A688)          // Naranja derivado de tonos cálidos sage
val White = Color(0xFFFFFFFF)           // Blanco puro
val Black = Color(0xFF2D332F)           // Negro con tinte sage para armonía
val Red = Color(0xFFA67B7B)             // Rojo suavizado con tintes sage

// ===== CATEGORÍAS FUNCIONALES (COLORES ORIGINALES) =====
val CategoryYellow = Color(0xFFFFC738)  // Amarillo vibrante - ofertas/descuentos
val CategoryBlue = Color(0xFF38B3FF)    // Azul brillante - información/tecnología
val CategoryGreen = Color(0xFF19D109)   // Verde intenso - disponible/éxito
val CategoryPurple = Color(0xFF8E5EFF)  // Púrpura - premium/exclusivo
val CategoryRed = Color(0xFFFF5E60)     // Rojo vibrante - urgencia/agotado

// ===== SUPERFICIES =====
val Surface = White
val SurfaceLighter = GrayLighter
val SurfaceDarker = Gray
val SurfaceGreenLighter = SoftSageGreen
val SurfaceBrand = CategoryGreen                  //Yellowish
val SurfaceError = Color(0xFFE8D6D6)        // Rojo muy suave derivado
val SurfaceSecondary = Color(0xFFE2D9CC)    // Naranja muy suave derivado

// ===== BORDES =====
val BorderIdle = GrayDarker
val BorderError = Red
val BorderSecondary = Orange

// ===== TEXTOS =====
val TextPrimary = Black                 // Negro con tinte sage
val TextSecondary = Color(0xFF6B7A6F)   // Derivado de SageGreen más oscuro
val TextTertiary = Color(0xFF8B9590)    // Texto terciario/subtítulos
val TextWhite = White
val TextCreme = CreameWhite
val TextBrand = Color(0xFF5F6E63)       // SageGreen oscurecido para mejor contraste

// ===== BOTONES =====
val ButtonPrimary = SageGreen           // Color base principal
val ButtonSecondary = GrayDarker        // Botón secundario
val ButtonDisabled = GrayDarker         // Botón deshabilitado

// ===== ICONOS =====
val IconPrimary = Black                 // Negro con tinte sage
val IconSecondary = Orange              // Naranja derivado
val IconWhite = White

// ===== NAVEGACIÓN Y TABS =====
val TabSelected = SageGreen             // Tab activo
val TabUnselected = Color(0xFF9FAB9C)   // Tab inactivo
val TabIndicator = SageGreen            // Indicador de tab activo
val BottomNavSelected = SageGreen       // Ícono bottom nav seleccionado
val BottomNavUnselected = Color(0xFF8B9590) // Ícono bottom nav no seleccionado
val NavigationBackground = White        // Fondo de navegación

// ===== INPUTS Y FORMULARIOS =====
val InputBackground = White             // Fondo de input
val InputBorder = GrayDarker           // Borde input normal
val InputBorderFocused = SageGreen     // Borde input enfocado
val InputBorderError = CategoryRed     // Borde input con error
val InputPlaceholder = Color(0xFF8B9590) // Texto placeholder
val InputLabel = TextSecondary         // Label de input
val InputDisabled = Gray               // Fondo input deshabilitado

// ===== SWITCHES Y CHECKBOXES =====
val SwitchTrackActive = SoftSageGreen   // Track del switch activo
val SwitchTrackInactive = GrayDarker    // Track del switch inactivo
val SwitchThumbActive = SageGreen       // Thumb del switch activo
val SwitchThumbInactive = White         // Thumb del switch inactivo
val CheckboxChecked = SageGreen         // Checkbox seleccionado
val CheckboxUnchecked = GrayDarker      // Checkbox no seleccionado
val CheckboxIndeterminate = SoftSageGreen // Checkbox indeterminado

// ===== PROGRESS INDICATORS =====
val ProgressActive = SageGreen          // Progreso completado
val ProgressInactive = Gray             // Progreso pendiente
val ProgressBackground = GrayLighter    // Fondo de barra de progreso
val LoadingSpinner = SageGreen          // Spinner de carga
val SkeletonBase = Gray                 // Color base skeleton loading
val SkeletonHighlight = GrayLighter     // Color highlight skeleton

// ===== CARDS Y CONTENEDORES =====
val CardBackground = White              // Fondo de cards
val CardBorder = Color(0x1A819A91)      // Borde sutil de cards (SageGreen 10% opacidad)
val CardShadow = Color(0x0F000000)      // Sombra de cards
val CardPressed = Color(0x0A819A91)     // Estado pressed de cards
val SectionBackground = GrayLighter     // Fondo de secciones
val SectionDivider = GrayDarker         // Divisor entre secciones

// ===== RATINGS Y REVIEWS =====
val StarFilled = Color(0xFFFFB800)      // Estrella completa (dorado estándar)
val StarHalf = Color(0xFFFFD700)        // Estrella media
val StarEmpty = GrayDarker              // Estrella vacía
val RatingExcellent = CategoryGreen     // Rating 4.5-5 estrellas
val RatingGood = CategoryYellow         // Rating 3.5-4.4 estrellas
val RatingPoor = CategoryRed            // Rating <3.5 estrellas

// ===== PRECIO Y COMERCIO =====
val PriceRegular = TextPrimary          // Precio normal
val PriceDiscount = CategoryRed         // Precio con descuento
val PriceOriginal = Color(0xFF8B9590)   // Precio original tachado
val PriceFree = CategoryGreen           // Precio "Gratis"
val CurrencySymbol = TextSecondary      // Símbolo de moneda
val SavingsAmount = CategoryGreen       // Cantidad ahorrada
val TaxAmount = TextTertiary            // Impuestos

// ===== ESTADO DE PRODUCTOS =====
val InStock = CategoryGreen             // En inventario
val LowStock = CategoryYellow           // Poco inventario (alerta)
val OutOfStock = CategoryRed            // Sin inventario
val PreOrder = CategoryBlue             // Pre-orden disponible
val Discontinued = Color(0xFF8B9590)    // Descontinuado
val NewProduct = CategoryBlue           // Producto nuevo
val FeaturedProduct = SageGreen         // Producto destacado

// ===== NOTIFICACIONES Y BADGES =====
val NotificationDot = CategoryRed       // Punto de notificación
val BadgeBackground = CategoryRed       // Fondo de badge numérico
val BadgeText = White                   // Texto en badge
val BadgeNew = CategoryBlue             // Badge "Nuevo"
val BadgeSale = CategoryRed             // Badge "Oferta"
val BadgeFeatured = SageGreen           // Badge "Destacado"
val BadgePopular = CategoryPurple       // Badge "Popular"
val BadgeBestSeller = CategoryGreen     // Badge "Más vendido"
val AlertBackground = Color(0xFFFFF3E0) // Fondo de alerta (amarillo muy claro)
val AlertBorder = CategoryYellow        // Borde de alerta
val ToastSuccess = CategoryGreen        // Toast de éxito
val ToastError = CategoryRed            // Toast de error
val ToastWarning = CategoryYellow       // Toast de advertencia
val ToastInfo = CategoryBlue            // Toast informativo

// ===== CARRITOS Y CHECKOUT =====
val CartItemCount = CategoryRed         // Contador de items en carrito
val CartTotal = TextPrimary             // Total del carrito
val CartSubtotal = TextSecondary        // Subtotal
val CheckoutPrimary = SageGreen         // Botón principal checkout
val PaymentSecure = CategoryGreen       // Indicador pago seguro
val ShippingFree = CategoryGreen        // Envío gratis
val ShippingPaid = TextSecondary        // Envío pagado

// ===== FILTROS Y BÚSQUEDA =====
val FilterActive = SageGreen            // Filtro aplicado
val FilterInactive = GrayDarker         // Filtro disponible
val FilterChipSelected = SoftSageGreen  // Chip de filtro seleccionado
val FilterChipUnselected = Gray         // Chip de filtro no seleccionado
val SearchHighlight = Color(0xFFFFE082) // Texto resaltado en búsqueda
val SearchSuggestion = Color(0xFF6B7A6F) // Sugerencias de búsqueda

// ===== ESTADOS DE CONEXIÓN =====
val OnlineIndicator = CategoryGreen     // Conectado
val OfflineIndicator = Color(0xFF8B9590) // Sin conexión
val SyncInProgress = CategoryBlue       // Sincronizando
val SyncComplete = CategoryGreen        // Sincronización completa
val SyncError = CategoryRed             // Error de sincronización

// ===== CALENDARIO Y FECHAS =====
val CalendarToday = SageGreen           // Día actual
val CalendarSelected = SoftSageGreen    // Día seleccionado
val CalendarAvailable = TextPrimary     // Días disponibles
val CalendarUnavailable = Color(0xFF8B9590) // Días no disponibles
val CalendarWeekend = TextSecondary     // Fines de semana

// ===== CHAT Y SOPORTE =====
val MessageSent = SoftSageGreen         // Mensaje enviado
val MessageReceived = Gray              // Mensaje recibido
val MessageTimestamp = TextTertiary     // Timestamp de mensaje
val TypingIndicator = SageGreen         // Indicador "escribiendo"
val OnlineStatus = CategoryGreen        // Usuario en línea
val OfflineStatus = Color(0xFF8B9590)   // Usuario desconectado

// ===== TEMAS Y MODOS =====
val AccentPrimary = SageGreen           // Acento principal
val AccentSecondary = SoftSageGreen     // Acento secundario
val BrandPrimary = SageGreen            // Color marca principal
val BrandSecondary = SageMist           // Color marca secundario
val ThemeBackground = White             // Fondo general del tema
val ThemeSurface = GrayLighter          // Superficie del tema