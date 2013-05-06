package com.mcs.client.mina;

import com.mcs.client.android.Env;
import com.mcs.framework.message.mina.Ok;

/**
 * 成功鉴权之后等待客户端发送Ready，收到Ready信号就进入Ready状态
 * @author dbds
 */
public class StateAuth extends StateAdapter{

    public StateAuth() {
        super("Authenticated");
    }

    @Override
    public boolean isState(int type) {
        return type == State.AUTHENTICATED ? true : false;
    }
    
    @Override
    public State onReceiveOk(MinaClient client, Ok ok) {
    	// 在auth状态，第一个收到的ok的description应该包含id信息
    	Env.getInstance().setClientId(ok.getDescription());
    	client.startHeartBeat();
    	return new StateReady();
    }
}
