import cn.hutool.core.lang.Dict;
import com.zll.websocket.model.ServerInfo;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

public class Main {
    public static void main(String[] args) {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();

        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setCpuInfo(hardware.getProcessor());

        System.out.println(serverInfo.getCpu());

        Dict dict = Dict.create().parseBean(serverInfo.getCpu());
        System.out.println(dict);
    }
}
