package com.wangce.Base;

 data  class BaseResponse<T>(var code:Int,var data:T,var  message:String,var success:Boolean) {


}
