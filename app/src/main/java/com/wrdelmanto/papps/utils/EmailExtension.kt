package com.wrdelmanto.papps.utils

import android.content.Intent

// Email Extensions variables
// Settings Intent.createChooser(intent, R.string.send_feedback_choose_email_client))
// Send Feedback
const val MAIL_TO = "wrdelmanto@gmail.com"
const val MAIL_SUBJECT = "Papps Feedback"

/**
 * Compose an Email.
 *
 * @param mailTo
 * @param subject
 * @param body
 *
 * @return intent
 */
fun composeEmail(mailTo: String, subject: String, body: String?): Intent {
    val intent = Intent(Intent.ACTION_SEND)

    intent.type = "text/plain"

    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mailTo))
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    if (!body.isNullOrBlank()) intent.putExtra(Intent.EXTRA_TEXT, body)

    // You can also attach multiple items by passing an ArrayList of Uris
    // intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"))

    return intent
}

/**
 * Compose an Email.
 *
 * @param mailTo
 * @param subject
 *
 * @return intent
 */
fun composeEmail(mailTo: String, subject: String): Intent = composeEmail(mailTo, subject, "")