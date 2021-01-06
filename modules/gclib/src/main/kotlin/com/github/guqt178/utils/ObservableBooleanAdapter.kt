package com.github.guqt178.utils

import android.databinding.ObservableBoolean
import com.google.gson.*
import java.lang.reflect.Type

/**
 * 使用
 * data class Test(
 *    @SerializedName("likeNum")
      @JsonAdapter(ObservableBooleanAdapter::class)
      var likeStatus: ObservableBoolean, //  likeStatus现在可以在xml中响应变化
 * )
 */
class ObservableBooleanAdapter : JsonSerializer<Boolean>, JsonDeserializer<ObservableBoolean> {

    override fun serialize(src: Boolean?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return context?.serialize(ObservableBoolean(src ?: false)) ?: JsonNull()
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ObservableBoolean {
        json?.asBoolean?.let {
            return ObservableBoolean(it)
        }
        return ObservableBoolean(false)
    }


}