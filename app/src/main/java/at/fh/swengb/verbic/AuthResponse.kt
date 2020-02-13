package at.fh.swengb.verbic

import com.squareup.moshi.JsonClass

@JsonClass (generateAdapter = true)

class AuthResponse(val token: String)