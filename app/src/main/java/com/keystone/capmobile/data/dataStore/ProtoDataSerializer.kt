package com.keystone.capmobile.data.dataStore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.keystone.capmobile.SavedUser
import java.io.InputStream
import java.io.OutputStream

object ProtoDataSerializer : Serializer<SavedUser> {
    override val defaultValue: SavedUser = SavedUser.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): SavedUser {
        try {
            return SavedUser.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: SavedUser, output: OutputStream) = t.writeTo(output)
}