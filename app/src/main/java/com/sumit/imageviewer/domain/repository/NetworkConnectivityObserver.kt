package com.sumit.imageviewer.domain.repository

import com.sumit.imageviewer.domain.model.NetworkStatus
import kotlinx.coroutines.flow.StateFlow

interface NetworkConnectivityObserver {
    var networkStatus: StateFlow<NetworkStatus>
}