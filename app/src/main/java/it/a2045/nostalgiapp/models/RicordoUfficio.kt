package it.a2045.nostalgiapp.models

import com.beust.klaxon.Json

data class RicordoUfficio(@Json("foto") var foto: String, @Json("audio") var audio: String, @Json("video") var video: String, @Json("testo") var testo: String)