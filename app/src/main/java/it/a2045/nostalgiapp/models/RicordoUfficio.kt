package it.a2045.nostalgiapp.models

import com.beust.klaxon.Json
import java.net.URL

data class RicordoUfficio(@Json("foto") var foto: URL, @Json("audio") var audio: URL)