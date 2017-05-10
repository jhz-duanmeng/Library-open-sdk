package com.efrobot.paylibrary;
import com.efrobot.paylibrary.PayResult;
interface ICallBack{
	/**
	*callback of AIDLClient
	*handle by server
	**/
	void handleByServer(in PayResult param);
}