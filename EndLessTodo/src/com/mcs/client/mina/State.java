package com.mcs.client.mina;

import com.mcs.framework.message.mina.Auth;
import com.mcs.framework.message.mina.Data;
import com.mcs.framework.message.mina.Ok;
import com.mcs.framework.message.mina.Error;

/**
 * 状态机的各种行为的接口
 * @author dbds
 */
public interface State {

    /** 不同的状态 */
    static final int CONNECTED          = 0;
    static final int READY              = 1;
    static final int LEAVING            = 2;
    static final int DISCONNECTED       = 3;
    static final int AUTHENTICATED		= 4;

    /** 确认是否是type类型的State */
    boolean isState(int type);

    /** 对比本状态与state是否相同 */
    boolean deepEqual(State state);

    /** 返回状态的名称 */
    String getName();
    
    State onReceiveOk(MinaClient client, Ok ok);

    State onReceiveError(MinaClient client, Error error);

    State onReceiveData(MinaClient client, Data data);
    
    State onSendAuth(MinaClient client, Auth auth);
    
}