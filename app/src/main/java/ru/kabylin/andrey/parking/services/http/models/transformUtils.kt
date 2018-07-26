package ru.kabylin.andrey.parking.services.http.models

import ru.kabylin.andrey.parking.client.*

inline fun <T> safeTransform(crossinline transform: () -> T): T {
    try {
        return transform()
    } catch (e: Throwable) {
        throw when (e) {
            is AccessError,
            is CredentialsError,
            is SessionError,
            is ValidationErrors,
            is LogicError -> e
            else -> AccessError(AccessErrorReason.BAD_RESPONSE, e)
        }
    }
}

inline fun lala(value: String?, crossinline aa: () -> String?): String? {
    return if (value == null || value.isBlank()) {
        aa()
    } else {
        value
    }
}

inline fun doIfResponseFieldFilled(value: String?, crossinline job: (String) -> Unit) {
    if (value != null && value.isNotBlank())
        job(value)
}

inline fun <T> doIfResponseFieldFilled(value: T?, crossinline job: (T) -> Unit) {
    if (value != null)
        job(value)
}
