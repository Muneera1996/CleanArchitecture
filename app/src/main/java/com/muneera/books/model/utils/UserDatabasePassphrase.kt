package com.muneera.books.model.utils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import java.io.File
import java.security.SecureRandom


class UserDatabasePassphrase(private val context: Context) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPassphrase(): ByteArray{
        val file = File(context.filesDir, "user_passphrase.bin")
        val encryptedFile = EncryptedFile.Builder(
            file,
            context,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        return if(file.exists()){
            encryptedFile.openFileInput().use { it.readBytes() }
        } else {
            generatePassphrase().also {
                    passPhrase ->
                encryptedFile.openFileOutput().use { it.write(passPhrase) }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generatePassphrase(): ByteArray{
        val random = SecureRandom.getInstanceStrong()
        val result = ByteArray(32)

        random.nextBytes(result)
        while (result.contains(0)){
            random.nextBytes(result)
        }

        return result
    }
}
