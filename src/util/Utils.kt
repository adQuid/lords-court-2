package util

fun <T> safeSublist(list: List<T>, start: Int, end: Int): List<T>{
    if(start >= list.size){
        return listOf<T>()
    }
    if(end >= list.size){
        return list.subList(start,list.size)
    }
    return list.subList(start,end)
}