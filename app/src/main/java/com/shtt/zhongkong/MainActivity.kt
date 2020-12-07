package com.shtt.zhongkong

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.google.gson.Gson
import com.shtt.zhongkong.net.ApiException
import com.shtt.zhongkong.net.NetWorkManager
import com.shtt.zhongkong.net.ResponseTransformer
import com.shtt.zhongkong.net.SchedulerProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myRequetPermission()

        initView()


    }


    fun readFile() {

        var filepath =
            Environment.getExternalStorageDirectory().absolutePath + "/zhongkong/value.txt"
        var file = File(filepath)
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
            var fw = FileWriter(file)
            var bean = NetBean.getIntanse()
            fw.write(Gson().toJson(bean))
            fw.close()
        } else {
            var s = file.readText()
            var json = Gson().fromJson<NetBean>(s, NetBean::class.java)
            var nb = NetBean.getIntanse();
            nb.baseUrl = json.baseUrl

        }

    }

    fun initView() {

        //锡山区生态环境现状

        getStatus(1, sw_xsq,"baseUrl")
        sw_xsq.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView.isPressed) {
                return@setOnCheckedChangeListener
            }

            openOrClose(isChecked, 1, sw_xsq,"baseUrl")

        }
        //循环溯源

        getStatus(2, sw_xhsy,"baseUrl")
        sw_xhsy.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView.isPressed) {
                return@setOnCheckedChangeListener
            }

            openOrClose(isChecked, 2, sw_xhsy,"baseUrl")

        }


        //循环经济战略优势
        getStatus(0,sw_xhjj,"xhjj")
        sw_xhjj.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView.isPressed) {
                return@setOnCheckedChangeListener
            }

            openOrClose(isChecked,  0,  sw_xhjj,"xhjj")
        }
        btn_xhjj.setOnClickListener {
            shotdown("xhjj")
        }


        //灯控总开关
        getStatusLight(sw_dk,"baseUrl")
        sw_dk.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView.isPressed) {
                return@setOnCheckedChangeListener
            }

            openOrCloseLight(isChecked,  0,sw_dk,"baseUrl")
        }

        //循环经济产业链
        getStatus(0,sw_xhjjcyl,"xhjjcyl")
        sw_xhjjcyl.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView.isPressed) {
                return@setOnCheckedChangeListener
            }

            openOrClose(isChecked,0,  sw_xhjjcyl,"xhjjcyl")
        }
        sw_xhjjcyl.setOnClickListener {
            shotdown("xhjjcyl")
        }


        //再生水

        getStatus(0,sw_zss,"zss")
        sw_zss.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView.isPressed) {
                return@setOnCheckedChangeListener
            }

            openOrClose(isChecked,0,  sw_zss,"zss")
        }
        sw_zss.setOnClickListener {
            shotdown("zss")
        }


        //城镇污水
        getStatus(0,sw_czws,"czws")
        sw_czws.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView.isPressed) {
                return@setOnCheckedChangeListener
            }

            openOrClose(isChecked,0,  sw_zss,"czws")
        }
        sw_czws.setOnClickListener {
            shotdown("czws")
        }

        //工业污水

        getStatus(0,sw_gyws,"gyws")
        sw_gyws.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView.isPressed) {
                return@setOnCheckedChangeListener
            }

            openOrClose(isChecked,0,  sw_gyws,"gyws")
        }
        sw_gyws.setOnClickListener {
            shotdown("gyws")
        }


        //feiguchuli

        getVersion("fgcl",sw_fgcl,)
        sw_fgcl.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView.isPressed) {
                return@setOnCheckedChangeListener
            }

            setVersion(isChecked,  sw_fgcl,"fgcl")
        }
        sw_fgcl.setOnClickListener {
            shotdown("fgcl")
        }

        //dameixisan
        getStatus(1, sw_dmxs,"baseUrl")
        sw_dmxs.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView.isPressed) {
                return@setOnCheckedChangeListener
            }

            openOrClose(isChecked, 1, sw_dmxs,"baseUrl")

        }




        //xunhuanzhilu

        sw_plc1.











    }


    fun openOrClose(boolean: Boolean, code: Int, view: Switch?,head: String) {
        view?.isClickable = false
        NetWorkManager.getRequest().CloseOrOpen(boolean, code,head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .compose(ResponseTransformer.handleResult())
            .subscribe({

            }, {

                var e = it as ApiException
                Toast.makeText(this, it.displayMessage, Toast.LENGTH_SHORT).show()
                view?.isChecked = !boolean
                view?.isClickable = true


            }, {
                view?.isClickable = true
            }).also { }
    }

    fun getStatus(code: Int, view: Switch?,head: String) {
        NetWorkManager.getRequest().getStatus(code, head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .compose(ResponseTransformer.handleResult())
            .subscribe({
                view?.isChecked = it.isStatus
            }, {
                var e = it as ApiException
                Toast.makeText(this, e.displayMessage, Toast.LENGTH_SHORT).show()
            }).also { }

    }





    fun   shotdown(head:String){
        NetWorkManager.getRequest().shutdown(head)
            .compose(SchedulerProvider.getInstance().applySchedulers())

            .subscribe({
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }).also { }
    }



    fun openOrCloseLight(boolean: Boolean, code: Int, view: Switch?,head: String) {
        view?.isClickable = false
        NetWorkManager.getRequest().lightControl(boolean,head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .compose(ResponseTransformer.handleResult())
            .subscribe({

            }, {

                var e = it as ApiException
                Toast.makeText(this, it.displayMessage, Toast.LENGTH_SHORT).show()
                view?.isChecked = !boolean
                view?.isClickable = true


            }, {
                view?.isClickable = true
            }).also { }
    }

    fun getStatusLight( view: Switch?,head: String) {
        NetWorkManager.getRequest().getLightStatus( head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .compose(ResponseTransformer.handleResult())
            .subscribe({
                view?.isChecked = it.isStatus
            }, {
                var e = it as ApiException
                Toast.makeText(this, e.displayMessage, Toast.LENGTH_SHORT).show()
            }).also { }

    }


      fun getVersion(head: String,view: Switch?){
          NetWorkManager.getRequest().getCurrentVersion( head)
              .compose(SchedulerProvider.getInstance().applySchedulers())
              .compose(ResponseTransformer.handleResult())
              .subscribe({
                  view?.isChecked = it.isStatus
              }, {
                  var e = it as ApiException
                  Toast.makeText(this, e.displayMessage, Toast.LENGTH_SHORT).show()
              }).also { }
      }

    fun setVersion(boolean: Boolean, view: Switch?,head: String) {
        NetWorkManager.getRequest().radarSwitch(boolean, head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .compose(ResponseTransformer.handleResult())
            .subscribe({
                view?.isChecked = it.isStatus
            }, {

                var e = it as ApiException
                Toast.makeText(this, it.displayMessage, Toast.LENGTH_SHORT).show()
                view?.isChecked = !boolean
                view?.isClickable = true
            }).also { }

    }







    private fun myRequetPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        } else {
            readFile()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                readFile()
            } else {
                Toast.makeText(this, "需要授权才能使用app", Toast.LENGTH_SHORT).show()
            }
        }
    }


}