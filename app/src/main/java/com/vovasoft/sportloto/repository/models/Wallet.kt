package com.vovasoft.sportloto.repository.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.vovasoft.sportloto.App
import com.vovasoft.sportloto.repository.RepositoryCallback
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/***************************************************************************
 * Created by arseniy on 14/10/2017.
 ****************************************************************************/
@Entity(tableName = "wallets")
data class Wallet(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
                  var id: Int? = null,

                  @ColumnInfo(name = "number")
                  var number: String? = null
) {


    fun save() {
        App.database.walletsDao().insert(this@Wallet)
    }


    fun saveAsync(callback: RepositoryCallback<Unit>) {
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


    fun deleteAsync(callback: RepositoryCallback<Unit>) {
        doAsync {
            App.database.walletsDao().delete(this@Wallet)
            uiThread {
                callback.done()
            }
        }
    }

}