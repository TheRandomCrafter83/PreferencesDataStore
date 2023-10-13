package com.coderz.f1.testingdatastore

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.myDataStore by preferencesDataStore(
    "prefs.pb"
)

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

class MainActivity : AppCompatActivity() {

    private lateinit var dataStore: DataStore<Preferences>

    private lateinit var edittext: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edittext = findViewById(R.id.edittext)

        dataStore = myDataStore

        lifecycleScope.launch {
            val value = readData("value")
            edittext.text = (value?:"").toEditable()
        }
    }



    override fun onStop() {
        lifecycleScope.launch{
            saveData("value",edittext.text.toString())
        }
        super.onStop()
    }

    private suspend fun saveData(key:String, value:String){
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit{ settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun readData(key:String):String?{
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

}


