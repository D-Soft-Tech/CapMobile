package com.keystone.capmobile.data.repository

import com.keystone.capmobile.data.api.RemoteApiServiceImplementation
import javax.inject.Inject

class Repository @Inject constructor(
    val remoteApiServiceImplementation: RemoteApiServiceImplementation
) {
}