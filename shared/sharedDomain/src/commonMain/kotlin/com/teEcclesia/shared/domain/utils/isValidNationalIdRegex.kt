package com.teEcclesia.shared.domain.utils

fun isValidNationalIdRegex(nationalId: String): Boolean {
    val regex = "^([23])\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])(01|02|03|04|11|12|13|14|15|16|17|18|19|21|22|23|24|25|26|27|28|29|31|32|33|34|35|88)\\d{5}$".toRegex()
    return regex.matches(nationalId)
}

fun isValidEgyptianNationalId(nationalId: String): Boolean {
    if (nationalId.length != 14 || !nationalId.all { it.isDigit() }) return false

    val centuryChar = nationalId[0]
    val yearStr = nationalId.substring(1, 3)
    val monthStr = nationalId.substring(3, 5)
    val dayStr = nationalId.substring(5, 7)
    val govCodeStr = nationalId.substring(7, 9)

    // 1. Century Check (2 = 1900-1999, 3 = 2000-2099)
    val centuryPrefix = when (centuryChar) {
        '2' -> "19"
        '3' -> "20"
        else -> return false
    }

    // 2. Date of Birth Validation
    val fullYear = (centuryPrefix + yearStr).toIntOrNull() ?: return false
    val month = monthStr.toIntOrNull() ?: return false
    val day = dayStr.toIntOrNull() ?: return false

    if (!isValidDate(fullYear, month, day)) return false

    // 3. Governorate Code Check
    val validGovernorates = setOf(
        "01", "02", "03", "04", "11", "12", "13", "14", "15",
        "16", "17", "18", "19", "21", "22", "23", "24", "25",
        "26", "27", "28", "29", "31", "32", "33", "34", "35", "88"
    )
    if (govCodeStr !in validGovernorates) return false

    return true
}

private fun isValidDate(year: Int, month: Int, day: Int): Boolean {
    if (month !in 1..12 || day !in 1..31) return false

    val daysInMonth = when (month) {
        2 -> if (isLeapYear(year)) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }
    return day <= daysInMonth
}

private fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}