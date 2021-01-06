package com.github.guqt178.utils

import android.databinding.ObservableInt
import com.google.gson.*
import java.lang.reflect.Type

/**
 * 使用
 * data class Test(
 *    @SerializedName("likeNum")
      @JsonAdapter(ObservableIntAdapter::class)
      var likeNum: ObservableInt, // 4 点赞数  likeNum现在可以在xml中响应变化
 * )
 */
class ObservableIntAdapter : JsonSerializer<Int>, JsonDeserializer<ObservableInt> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ObservableInt {
        json?.asInt?.let {
            return ObservableInt(it)
        }
        return ObservableInt(0)
    }

    override fun serialize(src: Int?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return context?.serialize(ObservableInt(src ?: 0)) ?: JsonNull()
    }


}