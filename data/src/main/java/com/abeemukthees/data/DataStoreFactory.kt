package com.abeemukthees.data

import android.content.Context
import com.abeemukthees.data.dummy.DummyDataStore

class DataStoreFactory(context: Context) {

    val dummyDataStore = DummyDataStore(context)
}