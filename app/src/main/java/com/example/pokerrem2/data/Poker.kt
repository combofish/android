package com.example.pokerrem2.data


class Poker {
    val num = listOf<String>("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "k")
    val type = listOf("黑桃", "红桃", "梅花", "方片", "大王", "小王")
    var pokers = mutableListOf<String>()
    var randomPoker = mutableListOf<String>()
        get() {
            field = pokers
            // println(field)
            for (i in 0..53){
                var j = (Math.random()*54).toInt()
                var t = pokers[i]
                field.set(i,pokers.get(j))
                field.set(j,t)
            }
            return field
        }

    init {
        for (n in num) {
            for (t in type.subList(0, 4)) {
                pokers.add(t + n)
            }
        }
        pokers.add(type[4])
        pokers.add(type[5])
    }

}