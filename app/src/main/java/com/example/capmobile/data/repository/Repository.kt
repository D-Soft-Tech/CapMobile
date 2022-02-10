package com.example.capmobile.data.repository

import com.example.capmobile.data.api.RemoteApiServiceImplementation
import javax.inject.Inject

class Repository @Inject constructor(
    val remoteApiServiceImplementation: RemoteApiServiceImplementation
) {
}