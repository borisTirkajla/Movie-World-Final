package com.example.movieworld.util

enum class GenresEnum(val genre: String) {
    Action("Action"),
    Adventure("Adventure"),
    Animation("Animation"),
    Biography("Biography"),
    Comedy("Comedy"),
    Crime("Crime"),
    Documentary("Documentary"),
    Drama("Drama"),
    Family("Family"),
    Fantasy("Fantasy"),
    History("History"),
    Horror("Horror"),
    Music("Music"),
    Musical("Musical"),
    Mystery("Mystery"),
    Romance("Romance"),
    SciFi("Sci - Fi"),
    Sport("Sport"),
    Thriller("Thriller"),
    War("War"),
    Western("Western");

    companion object {
        fun getValues(): Array<String> {
            val list = mutableListOf<String>()
            values().forEach {
                list.add(it.name)
            }
            return list.toTypedArray()
        }
    }
}
