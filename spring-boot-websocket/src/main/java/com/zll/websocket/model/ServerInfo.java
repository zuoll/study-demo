package com.zll.websocket.model;

import lombok.Getter;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.Util;

import java.util.Properties;

import static oshi.hardware.CentralProcessor.TickType;

@Getter
public class ServerInfo {

    private static final int OSHI_WAIT_SECOND = 1000;

    private Cpu cpu = new Cpu();

    private Memory memory = new Memory();

    private Jvm jvm = new Jvm();

    private DiskFile disk = new DiskFile();

    private Sys sys = new Sys();

    public ServerInfo() {
        copyTo();
    }

    private void copyTo() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();

        setCpuInfo(hardware.getProcessor());

        setMemInfo(hardware.getMemory());

        setDiskInfo();

        setJvmInfo();

        setDiskInfo();
    }

    /**
     * 设置CPU信息
     */
    public void setCpuInfo(CentralProcessor processor) {
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
        long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        cpu.setCpuNum(processor.getLogicalProcessorCount());
        cpu.setTotal(totalCpu);
        cpu.setSys(cSys);
        cpu.setUsed(user);
        cpu.setWait(iowait);
        cpu.setFree(idle);
    }

    /**
     * 设置内存信息
     */
    public void setMemInfo(GlobalMemory globalMemory) {
        memory.setFree(globalMemory.getAvailable());
        memory.setTotal(globalMemory.getTotal());
        memory.setUsed(globalMemory.getTotal() - globalMemory.getAvailable());
    }


    /**
     * 设置Java 虚拟机的 信息
     */
    public void setJvmInfo() {
        Properties properties = System.getProperties();
        jvm.setFree(Runtime.getRuntime().freeMemory());
        jvm.setTotal(Runtime.getRuntime().totalMemory());
        jvm.setHome(properties.getProperty("java.home"));
        jvm.setVersion(properties.getProperty("java.version"));
        jvm.setMax(Runtime.getRuntime().maxMemory());
        jvm.setStartTime("");
        jvm.setRunTime("");
    }

    /**
     * 设置磁盘信息//TODO
     */
    public void setDiskInfo() {

    }

    /**
     * 设置服务器信息//TODO
     */
    public void setSysInfo() {
    }


    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

}
