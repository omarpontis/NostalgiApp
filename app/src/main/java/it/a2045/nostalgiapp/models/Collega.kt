package it.a2045.nostalgiapp.models

import com.beust.klaxon.Json

data class Collega(
    @Json("nome") var nome: String, @Json("testo") var testo: String, @Json("foto") var foto: String, @Json("audio") var audio: String
)