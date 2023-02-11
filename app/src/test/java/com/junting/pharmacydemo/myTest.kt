package com.junting.pharmacydemo

var apple : String = "apple"
var greeting = {
    name: String, age: Int -> "$name, $age"
}


fun main() {
    var apple : String = apple
    print(greeting("Junting",18))
}


interface OnClickListener {
    fun onClick(dialog: String, which: Int)
}
