package it.a2045.nostalgiapp.models

import com.beust.klaxon.Json
import java.net.URL

data class Collega(
    @Json("nome") var nome: String, @Json("testo") var testo: String, @Json(
        "foto"
    ) var foto: URL, @Json("audio") var audio: URL
)