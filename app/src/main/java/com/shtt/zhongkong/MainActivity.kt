package com.shtt.zhongkong

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import java.io.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myRequetPermission()


    }


    fun   readFile(){

        var  filepath= Environment.getExternalStorageDirectory().absolutePath+"/zhongkong/value.txt"


        var  file=File(filepath)

        if (!file.exists()){
            file.parentFile.mkdirs()
            file.createNewFile()
            var fw=FileWriter(file)
            fw.write(getString(R.string.def))
            fw.close()




        }else{
           var s=  file.readText()




        }







    }

    private fun myRequetPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        } else {
            readFile()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==1){
            if (grantResults[0]==PERMISSION_GRANTED){
                readFile()
            }else{
                Toast.makeText(this, "需要授权才能使用app", Toast.LENGTH_SHORT).show()
            }
        }
    }


}