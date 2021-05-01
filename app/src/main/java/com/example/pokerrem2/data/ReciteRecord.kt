package com.example.pokerrem2.data

class ReciteRecord (val id:Int, val right:Int,val rate:Float,val rememberTime:String,val dictationTime:String){
    override fun toString(): String {
        return "ReciteRecord(id=$id, right=$right, rate=$rate, rememberTime='$rememberTime', dictationTime='$dictationTime')"
    }
}