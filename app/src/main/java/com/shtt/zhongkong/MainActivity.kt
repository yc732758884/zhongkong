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

        sw_xsq.setListener {

            openOrClose(it, 2, sw_xsq,"baseUrl")
        }


        //循环溯源

        getStatus(2, sw_xhsy,"baseUrl")
        sw_xhsy.setListener {


            openOrClose(it, 2, sw_xhsy,"baseUrl")

        }


        //循环经济战略优势
        getStatus(0,sw_xhjj,"xhjj")
        sw_xhjj.setListener {


            openOrClose(it,  0,  sw_xhjj,"xhjj")
        }
        btn_xhjj.setOnClickListener {
            shotdown("xhjj")
        }


        //灯控总开关
        getStatusLight(sw_dk,"baseUrl")
        sw_dk.setListener {

            openOrCloseLight(it,  0,sw_dk,"baseUrl")
        }

        //循环经济产业链
        getStatus(0,sw_xhjjcyl,"xhjjcyl")
        sw_xhjjcyl.setListener {
            openOrClose(it,0,  sw_xhjjcyl,"xhjjcyl")
        }
        sw_xhjjcyl.setOnClickListener {
            shotdown("xhjjcyl")
        }


        //再生水

        getStatus(0,sw_zss,"zss")
        sw_zss.setListener {

            openOrClose(it,0,  sw_zss,"zss")
        }
        sw_zss.setOnClickListener {
            shotdown("zss")
        }


        //城镇污水
        getStatus(0,sw_czws,"czws")
        sw_czws.setListener {

            openOrClose(it,0,  sw_zss,"czws")
        }
        sw_czws.setOnClickListener {
            shotdown("czws")
        }

        //工业污水

        getStatus(0,sw_gyws,"gyws")
        sw_gyws.setListener {

            openOrClose(it,0,  sw_gyws,"gyws")
        }
        sw_gyws.setOnClickListener {
            shotdown("gyws")
        }


        //feiguchuli

        getVersion("fgcl",sw_fgcl,)
        sw_fgcl.setListener {

            setVersion(it,  sw_fgcl,"fgcl")
        }
        sw_fgcl.setOnClickListener {
            shotdown("fgcl")
        }

        //dameixisan
        getStatus(1, sw_dmxs,"baseUrl")
        sw_dmxs.setListener {

            openOrClose(it, 1, sw_dmxs,"baseUrl")

        }




        //xunhuanzhilu




           //PLC开关控制

        sw_plc1.setListener {


        }







    }





    fun openOrClose(boolean: Boolean, code: Int, view: MySwitch?,head: String) {

        NetWorkManager.getRequest().CloseOrOpen(boolean, code,head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .compose(ResponseTransformer.handleResult())
            .subscribe({
                    view?.setOpen(boolean)
            }, {
                it as ApiException
                Toast.makeText(this, it.displayMessage, Toast.LENGTH_SHORT).show()



            }, {

            }).also { }
    }



    fun getStatus(code: Int, view: MySwitch?,head: String) {
        NetWorkManager.getRequest().getStatus(code, head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .compose(ResponseTransformer.handleResult())
            .subscribe({
                view?.open = it.isStatus
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



    fun openOrCloseLight(boolean: Boolean, code: Int, view: MySwitch?,head: String) {
        view?.isClickable = false
        NetWorkManager.getRequest().lightControl(boolean,head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .compose(ResponseTransformer.handleResult())
            .subscribe({
                     view?.setOpen(boolean)
            }, {

                var e = it as ApiException
                Toast.makeText(this, it.displayMessage, Toast.LENGTH_SHORT).show()

            }, {
            }).also { }
    }

    fun getStatusLight( view: MySwitch?,head: String) {
        NetWorkManager.getRequest().getLightStatus( head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .compose(ResponseTransformer.handleResult())
            .subscribe({
                view?.setOpen(it.isStatus)
            }, {
                var e = it as ApiException
                Toast.makeText(this, e.displayMessage, Toast.LENGTH_SHORT).show()
            }).also { }

    }


      fun getVersion(head: String,view: MySwitch?){
          NetWorkManager.getRequest().getCurrentVersion( head)
              .compose(SchedulerProvider.getInstance().applySchedulers())
              .compose(ResponseTransformer.handleResult())
              .subscribe({
                  view?.setOpen(it.isStatus)
              }, {
                  var e = it as ApiException
                  Toast.makeText(this, e.displayMessage, Toast.LENGTH_SHORT).show()
              }).also { }
      }

    fun setVersion(boolean: Boolean, view: MySwitch?,head: String) {
        NetWorkManager.getRequest().radarSwitch(boolean, head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .compose(ResponseTransformer.handleResult())
            .subscribe({
                view?.setOpen(it.isStatus)
            }, {

                var e = it as ApiException
                Toast.makeText(this, it.displayMessage, Toast.LENGTH_SHORT).show()

            }).also { }

    }

    fun  setPlcStatus(open:Boolean,code:Int,head:String){
        NetWorkManager.getRequest().plcState(open, code, head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .compose(ResponseTransformer.handleResult())
            .subscribe({

            },{

            })
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