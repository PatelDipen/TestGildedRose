package com.gildedrose

import java.lang.StringBuilder

fun StringBuilder.appendLn(str:String){
    this.append(str)
    this.append("\n")
}