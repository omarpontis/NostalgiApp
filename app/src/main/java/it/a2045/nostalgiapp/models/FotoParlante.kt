package it.a2045.nostalgiapp.models

import com.beust.klaxon.Json

data class FotoParlante(@Json("foto") var foto: String, @Json("audio") var audio: String)