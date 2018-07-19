package com.lzp.core.manager;

public interface NetworkManager extends Manager {
    boolean isConnect();

    /**
     *
     * @param connect 网络是否连接
     * @return 是否更新了状态
     */
    boolean updateConnectState(boolean connect);
}
