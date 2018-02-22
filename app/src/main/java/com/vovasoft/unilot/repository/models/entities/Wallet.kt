package com.vovasoft.unilot.repository.models.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.vovasoft.unilot.App
import com.vovasoft.unilot.repository.Reactive
import com.vovasoft.unilot.repository.models.GsonModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/***************************************************************************
 * Created by arseniy on 14/10/2017.
 ****************************************************************************/
@Entity(tableName = "results")
data class Wallet(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
                  var id: Int? = null,

                  @ColumnInfo(name = "number")
                  var number: String? = null
) : GsonModel() {


    fun save() {
        App.database.walletsDao().insert(this@Wallet)
    }


    fun saveAsync(callback: Reactive<Unit>) {
        doAsync {
            App.database.walletsDao().insert(this@Wallet)
            uiThread {
                callback.done()
            }
        }
    }


    fun delete() {
        App.database.walletsDao().delete(this@Wallet)
    }


    fun deleteAsync(callback: Reactive<Unit>) {
        doAsync {
            App.database.walletsDao().delete(this@Wallet)
            uiThread {
                callback.done()
            }
        }
    }

}