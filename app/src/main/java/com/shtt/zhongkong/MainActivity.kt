package com.shtt.zhongkong

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.nfc.tech.NfcV
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

    lateinit var dialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myRequetPermission()

        initView()

        dialog = ProgressDialog(this)
        dialog.setMessage("正在请求")


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
            nb.czws = json.czws
            nb.fgcl = json.fgcl
            nb.zss = json.zss
            nb.gyws = json.gyws
            nb.xhjj = json.xhjj
            nb.xhjjcyl = json.xhjjcyl
            nb.xhzl = json.xhzl
            nb.home=json.home


        }

    }

    fun initView() {


        getControlChange("home")
        sw_home.setColseText("锁定")
        sw_home.setOpenText("解锁")
        sw_home.setListener {

            changeControlchange("home", it)
        }


        //锡山区生态环境现状

        getStatus(1, sw_xsq, "baseUrl")


        sw_xsq.setListener {

            openOrClose(it, 1, sw_xsq, "baseUrl")
        }


        //循环溯源

        getStatus(2, sw_xhsy, "baseUrl")

        sw_xhsy.setListener {


            openOrClose(it, 2, sw_xhsy, "baseUrl")

        }


        //循环经济战略优势
        getStatus(0, sw_xhjj, "xhjj")
        sw_xhjj.setListener {


            openOrClose(it, 0, sw_xhjj, "xhjj")
        }
        btn_xhjj.setOnClickListener {
            shotdown("xhjj")
        }


        //灯控总开关
        getStatusLight(sw_dk, "baseUrl")
        sw_dk.setColseText("关")
        sw_dk.setOpenText("开")

        sw_dk.setListener {

            openOrCloseLight(it, 0, sw_dk, "baseUrl")
        }

        //循环经济产业链
        getStatus(0, sw_xhjjcyl, "xhjjcyl")
        sw_xhjjcyl.setListener {
            openOrClose(it, 0, sw_xhjjcyl, "xhjjcyl")
        }
        btn_xhjjcyl.setOnClickListener {
            shotdown("xhjjcyl")
        }


        //再生水

        getStatus(0, sw_zss, "zss")
        sw_zss.setListener {

            openOrClose(it, 0, sw_zss, "zss")
        }
        btn_zss.setOnClickListener {
            shotdown("zss")
        }


        //城镇污水
        getStatus(0, sw_czws, "czws")
        sw_czws.setListener {
            openOrClose(it, 0, sw_czws, "czws")
        }
        btn_czws.setOnClickListener {
            shotdown("czws")
        }

        //工业污水

        getStatus(0, sw_gyws, "gyws")
        sw_gyws.setListener {

            openOrClose(it, 0, sw_gyws, "gyws")
        }
        btn_gyws.setOnClickListener {
            shotdown("gyws")
        }


        //feiguchuli

        getVersion("fgcl", sw_fgcl,)

        sw_fgcl.setOpenText("A")
        sw_fgcl.setColseText("B")
        sw_fgcl.setListener {

            setVersion(it, sw_fgcl, "fgcl")
        }
        btn_fgcl.setOnClickListener {
            shotdown("fgcl")
        }

        //dameixisan


        getStatus(3, sw_dmxs, "baseUrl")


        sw_dmxs.setListener {

            openOrClose(it, 3, sw_dmxs, "baseUrl")

        }


        //xunhuanzhilu

        getCurrentVideo()

        getXhzlProjectorShutterStatus("baseUrl")
        sw_xhzl.setListener {
            changeXhzlProjectorShutterStatus("baseUrl",it)
        }

        sw_xc1.setListener {
            selectVideo(1, "xhzl")

        }
        sw_xc2.setListener {
            selectVideo(2, "xhzl")


        }
        sw_xc3.setListener {
            selectVideo(3, "xhzl")

        }
//        btn_xhzl.setOnClickListener {
//            shotdown("xhzl")
//        }

        btn_stop.setOnClickListener {
            reset("xhzl")
        }


        //PLC开关控制


        getPlcStatus(1, "baseUrl", sw_plc1)
        getPlcStatus(2, "baseUrl", sw_plc2)
        getPlcStatus(3, "baseUrl", sw_plc3)
        sw_plc1.setListener {

            setPlcStatus(it, 1, "baseUrl", sw_plc1)

        }

        sw_plc2.setListener {

            setPlcStatus(it, 2, "baseUrl", sw_plc2)
        }

        sw_plc3.setListener {

            setPlcStatus(it, 3, "baseUrl", sw_plc3)


        }


        btn_all.setOnClickListener {

            var dialog = AlertDialog.Builder(this)
            dialog.setMessage("确定全部关机？")
                .setPositiveButton("确定") { dialogInterface: DialogInterface, i: Int ->

                    shotDownAll("baseUrl")

                }
                .setNegativeButton("取消") { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
                .show()


        }


    }


    fun shotDownAll(head: String) {

        NetWorkManager.getRequest().shutdownAll(head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .subscribe({

                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }, {


            }, {

            }).also { }

    }

    fun getCurrentVideo() {
        iscurrentView(1, "xhzl", sw_xc1)
        iscurrentView(2, "xhzl", sw_xc2)
        iscurrentView(3, "xhzl", sw_xc3)
    }


    fun openOrClose(boolean: Boolean, code: Int, view: MySwitch, head: String) {

        NetWorkManager.getRequest().CloseOrOpen(boolean, code, head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .subscribe({

                view.setOpen(boolean)
            }, {


            }, {

            }).also { }
    }

    fun selectVideo(code: Int, head: String) {

        NetWorkManager.getRequest().CloseOrOpen(false, code, head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .subscribe({
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                getCurrentVideo()
            }, {

                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

            }, {

            }).also { }
    }


    fun getStatus(code: Int, view: MySwitch?, head: String) {
        NetWorkManager.getRequest().getStatus(code, head)
            .compose(SchedulerProvider.getInstance().applySchedulers())

            .subscribe({
                view?.setOpen(it.data.isStatus)
            }, {

                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }).also { }

    }


    fun shotdown(head: String) {
        NetWorkManager.getRequest().shutdown(head)
            .compose(SchedulerProvider.getInstance().applySchedulers())

            .subscribe({
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }).also { }
    }


    fun openOrCloseLight(boolean: Boolean, code: Int, view: MySwitch?, head: String) {
        view?.isClickable = false
        NetWorkManager.getRequest().lightControl(boolean, head)
            .compose(SchedulerProvider.getInstance().applySchedulers())

            .subscribe({
                view?.setOpen(boolean)
            }, {


                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

            }, {
            }).also { }
    }

    fun getStatusLight(view: MySwitch?, head: String) {
        NetWorkManager.getRequest().getLightStatus(head)
            .compose(SchedulerProvider.getInstance().applySchedulers())

            .subscribe({
                view?.setOpen(it.data.isStatus)
            }, {

                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }).also { }

    }


    fun getVersion(head: String, view: MySwitch?) {
        NetWorkManager.getRequest().getCurrentVersion(head)
            .compose(SchedulerProvider.getInstance().applySchedulers())

            .subscribe({
                view?.setOpen(it.data.isStatus)
            }, {

                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }).also { }
    }

    fun setVersion(boolean: Boolean, view: MySwitch?, head: String) {
        NetWorkManager.getRequest().radarSwitch(boolean, head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .subscribe({
                if (it.code == 0) {
                    view?.setOpen(boolean)
                }

            }, {


                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

            }).also { }

    }

    fun setPlcStatus(open: Boolean, code: Int, head: String, view: MySwitch) {
        NetWorkManager.getRequest().plcState(open, code, head)
            .compose(SchedulerProvider.getInstance().applySchedulers())

            .subscribe({
                if (it.success) {
                    view.setOpen(open)
                }

            }, {

            }).also { }
    }


    fun getPlcStatus(code: Int, head: String, view: MySwitch) {

        NetWorkManager.getRequest().getplcState(code, head)
            .compose(SchedulerProvider.getInstance().applySchedulers())

            .subscribe({
                view.setOpen(it.data.isStatus)

            }, {

            }).also { }

    }


    fun iscurrentView(code: Int, head: String, view: MySwitch) {
        NetWorkManager.getRequest().isCurrentVideo(code, head)
            .compose(SchedulerProvider.getInstance().applySchedulers())

            .subscribe({
                view.setOpen(it.data.isStatus)

            }, {

            }).also { }
    }


    fun reset(head: String) {
        NetWorkManager.getRequest().reset(head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .subscribe({
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                getCurrentVideo()
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }).also { }
    }

    fun getControlChange(head: String) {
        NetWorkManager.getRequest().getControlStatus(head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .compose(ResponseTransformer.handleResult())
            .subscribe({
                sw_home.setOpen(it.isStatus)
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }).also { }
    }


    fun changeControlchange(head: String, c: Boolean) {

        NetWorkManager.getRequest().changeControlStatus(head, c)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .subscribe({
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                sw_home.setOpen(c)
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }).also { }

    }




    fun getXhzlProjectorShutterStatus(head: String) {
        NetWorkManager.getRequest().getXhzlProjectorShutterStatus(head)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .compose(ResponseTransformer.handleResult())
            .subscribe({
                sw_xhzl.setOpen(it.isStatus)
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }).also { }
    }


    fun changeXhzlProjectorShutterStatus(head: String, c: Boolean) {

        NetWorkManager.getRequest().changeXhzlProjectorShutterStatus(head, c)
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .subscribe({
                if (it.code==0){
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    sw_xhzl.setOpen(c)
                }

            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }).also { }

    }
    fun showProgressDialog() {

        dialog.show()
    }

    fun dissDialog() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }

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