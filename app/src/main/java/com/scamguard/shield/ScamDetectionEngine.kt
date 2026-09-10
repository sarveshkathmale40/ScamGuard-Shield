package com.scamguard.shield

import java.util.regex.Pattern

object ScamDetectionEngine {

    private val OFFICIAL_TRUSTED_DOMAINS = listOf(
        "sbi.co.in", "onlinesbi.sbi", "hdfcbank.com", "icicibank.com",
        "axisbank.com", "kotak.com", "bankofbaroda.in", "punjabnationalbank.in",
        "canarabank.com", "unionbankofindia.co.in", "incometax.gov.in",
        "uidai.gov.in", "epfindia.gov.in", "mahadiscom.in"
    )

    private val SUSPICIOUS_INDICATORS = listOf(
        "bit.ly", "tinyurl.com", "ngrok-free.app", "is.gd", "t.co",
        ".xyz", ".top", ".club", ".online", ".site", ".apk", "free-gift", "reward-claim"
    )

    private val PANIC_SCAM_KEYWORDS = listOf(
        "electricity will be disconnected", "power cut tonight", "account blocked today",
        "pan card suspended", "kyc pending immediately", "lottery winner",
        "won cash prize", "claim phonepe reward", "free recharge 3 months"
    )

    private val URL_PATTERN = Pattern.compile(
        "https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
        Pattern.CASE_INSENSITIVE
    )

    data class AnalysisResult(
        val isScam: Boolean,
        val reason: String = "",
        val foundLink: String = ""
    )

    fun inspectText(messageText: String): AnalysisResult {
        val lowerText = messageText.lowercase()

        if (lowerText.contains("otp") && !lowerText.contains("http") && !lowerText.contains("www.")) {
            return AnalysisResult(isScam = false)
        }

        val matcher = URL_PATTERN.matcher(messageText)
        val extractedLink = if (matcher.find()) matcher.group(0) else ""

        if (extractedLink.isNotEmpty()) {
            val lowerLink = extractedLink.lowercase()

            val isOfficialTrusted = OFFICIAL_TRUSTED_DOMAINS.any { lowerLink.contains(it) }
            if (isOfficialTrusted) {
                return AnalysisResult(isScam = false)
            }

            val hasSuspiciousDomain = SUSPICIOUS_INDICATORS.any { lowerLink.contains(it) }
            val hasPanicKeyword = PANIC_SCAM_KEYWORDS.any { lowerText.contains(it) }

            if (hasSuspiciousDomain || hasPanicKeyword) {
                return AnalysisResult(
                    isScam = true,
                    reason = "अनोळखी/धोकादायक लिंक किंवा बँक फसवणुकीचा प्रयत्न आढळला!",
                    foundLink = extractedLink
                )
            }
        }

        return AnalysisResult(isScam = false)
    }
}
