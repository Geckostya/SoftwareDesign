package hse.nedikov.bash

import java.util.ArrayList

fun list(vararg values: String): ArrayList<String> = ArrayList<String>().apply { addAll(values) }