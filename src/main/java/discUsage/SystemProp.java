package discUsage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

public class SystemProp {

	String processName = null;
	int processId = -1;

	public SystemProp() {

	}

	public SystemProp(String processName) throws IOException {
		this.processName = processName;
		processId = pidGetterLinux();
	}

	public double getProcessCpuLoad() throws Exception {

		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
		AttributeList list = mbs.getAttributes(name, new String[] { "ProcessCpuLoad" });

		if (list.isEmpty())
			return Double.NaN;

		Attribute att = (Attribute) list.get(0);
		Double value = (Double) att.getValue();

		// usually takes a couple of seconds before we get real values
		if (value == -1.0)
			return Double.NaN;
		// returns a percentage value with 1 decimal point precision
		return ((int) (value * 1000) / 10.0);
	}

	public int cmdOutputReader(String command) throws IOException {
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		String storeLine = null;
		int count = 0;
		while (true) {
			line = r.readLine();
			if (line != null && line.matches("[0-9 ]+")) {
				storeLine = line.trim();
				count = Integer.parseInt(storeLine);
			}
			if (line == null) {
				break;
			}
			// System.out.println(line);

		}
		return count;
	}

	public int getPid() throws IOException {
		String processName = "\"chrome.exe\"";
		String command = "tasklist | find " + processName;
		// System.out.println(command);
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		String storeLine = "";
		while (true) {
			line = r.readLine();
			if (line != null) {
				storeLine = line;
			}
			if (line == null) {
				break;
			}
			// System.out.println(line);

		}
		String lineSplit[] = storeLine.split(" ");
		int pid = 0, i = 0;
		while (i < lineSplit.length) {
			// System.out.println(lineSplit[i]);
			if (lineSplit[i].matches("[0-9]+")) {
				pid = Integer.parseInt(lineSplit[i]);
				break;
			}
			i++;

		}
		// System.out.println("Chrome pid "+pid);
		return pid;
	}

	public SystemPropertiesRamDetails ramUsage() throws IOException, InstanceNotFoundException,
			AttributeNotFoundException, MalformedObjectNameException, ReflectionException, MBeanException {
		SystemProp iObj = new SystemProp();
		int pid = iObj.getPid();
		// System.out.println("PID "+pid);
		int ramUsage = 0;

		String command = "wmic process where processid=" + (pid) + " get WorkingSetSize ";

		// System.out.println("ramCommand : "+ command);

		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		Object attribute = mBeanServer.getAttribute(new ObjectName("java.lang", "type", "OperatingSystem"),
				"TotalPhysicalMemorySize");
		Object attribute2 = mBeanServer.getAttribute(new ObjectName("java.lang", "type", "OperatingSystem"),
				"FreePhysicalMemorySize");

		// ram object
		SystemPropertiesRamDetails ramDetails = new SystemPropertiesRamDetails();

//		System.out.println("Total memory: " + Long.parseLong(attribute.toString()) / (1024 * 1024) + " MB");		
//		System.out.println("Free  memory: " + Long.parseLong(attribute2.toString()) / (1024 * 1024) + " MB");
//		System.out.println("Consumed memory: " + (Long.parseLong(attribute.toString()) / (1024 * 1024)
//				- Long.parseLong(attribute2.toString()) / (1024 * 1024)) + " MB");

		ramDetails.setTotalMemory(Long.parseLong(attribute.toString()) / (1024 * 1024));
		ramDetails.setFreeMemory(Long.parseLong(attribute2.toString()) / (1024 * 1024));
		ramDetails.setConsumedMemory((Long.parseLong(attribute.toString()) / (1024 * 1024)
				- Long.parseLong(attribute2.toString()) / (1024 * 1024)));

		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		String storeLine = null;
		while (true) {
			line = r.readLine();
			if (line != null && line.matches("[0-9 ]+")) {
				storeLine = line.trim();
				ramUsage = Integer.parseInt(storeLine) / (1024 * 1024);
			}
			if (line == null) {
				break;
			}
			// System.out.println(line);

		}
//		System.out.println("Ram Using by application : " + ramUsage + " MB");
		ramDetails.setApplicationRamUsage(ramUsage);

//		System.out.println(ramDetails.toString());
		return ramDetails;
	}

	public SystemPropertiesCpuDetails cpuUsage() throws IOException {
		SystemProp iObj = new SystemProp();
		int pid = iObj.getPid();

		String pcommandThreadCount = "wmic process where processid=" + (pid) + " get ThreadCount ";
		String scommandTotalThreads = "powershell \"(Get-Process|Select-Object -ExpandProperty Threads).Count\"";

		String scommandTotalHandles = "powershell \"(get-process|Measure-Object Handles -Sum |% Sum)\"";
		String pcommandHandleCount = "wmic process where processid=" + (pid) + " get HandleCount ";

		String scommandTotalProcesses = "powershell \"(Get-Process).Count\"";

		// System.out.println(pcommandThreadCount);
		int threadCount = iObj.cmdOutputReader(pcommandThreadCount);
		// System.out.println(scommandTotalThreads);
		int totalThreads = iObj.cmdOutputReader(scommandTotalThreads);

		// System.out.println(scommandTotalHandles);
		int totalHandles = iObj.cmdOutputReader(scommandTotalHandles);
		// System.out.println(pcommandHandleCount);
		int handleCount = iObj.cmdOutputReader(pcommandHandleCount);

		// System.out.println(scommandTotalProcesses);
		int totalProcess = iObj.cmdOutputReader(scommandTotalProcesses);

//		System.out.println("Thread Count of Process: " + threadCount);
//		System.out.println("Total Thread Count of System being used: " + totalThreads);
//
//		System.out.println("Handle Count of Process: " + handleCount);
//		System.out.println("Total Handles Count of System being used: " + totalHandles);
//
//		System.out.println("Total Processes in system : " + totalProcess);

		SystemPropertiesCpuDetails cpuDetails = new SystemPropertiesCpuDetails();
		cpuDetails.setThreadCount(threadCount);
		cpuDetails.setTotalThreads(totalThreads);
		cpuDetails.setHandleCount(handleCount);
		cpuDetails.setTotalHandles(totalHandles);
		cpuDetails.setTotalProcess(totalProcess);

//		System.out.println(cpuDetails.toString());
		return cpuDetails;

	}

//	public DiscDetails diskUsage() throws IOException {
//
//		SystemProp iObj = new SystemProp();
//		int pid = iObj.getPid();
//
//		String command = "powershell \"(Get-Process -Id " + (pid) + " | Select-Object path)\"";
//
//		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
//		builder.redirectErrorStream(true);
//		Process p = builder.start();
//		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
//		String line;
//		String storeLine = null;
//		while (true) {
//			line = r.readLine();
//			if (line != null) {
//				if (line.contains(".exe")) {
//					storeLine = line;
//					break;
//				}
//			}
//			if (line == null) {
//				break;
//			}
//			// System.out.println(line);
//
//		}
//
//		File filep = new File(storeLine);
//
//		File file = new File("c:");
//		long totalSpace = file.getTotalSpace(); // total disk space in bytes.
//		long usableSpace = file.getUsableSpace(); /// unallocated / free disk space in bytes.
//		
//		DiscDetails discDetails = new DiscDetails(totalSpace, usableSpace);
//		return discDetails;
////		System.out.println("Total size : " + totalSpace / 1024 / 1024 + " mb");
////		System.out.println("Space free : " + usableSpace / 1024 / 1024 + " mb");
////		System.out.println("Application occupied space : " + (double) filep.length() / (1024 * 1024) + " mb");
//	}

	public String linuxCommandReader(String[] command) throws IOException {
		// System.out.println(command);
		Process p = Runtime.getRuntime().exec(command);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		String lastLine = null;
//		  while (true) {
//	            line = stdInput.readLine();
//	            if(line !=null) {lastLine = line; }
//	            
//	            if (line == null) { break; }
//	        }

		while ((line = stdInput.readLine()) != null) {
			// System.out.println(line);
			if (line != null) {
				lastLine = line;
			}
		}

		return lastLine;
	}

	public DiscDetails diskUsage() {
		try {
			SystemProp iObj = new SystemProp();
			int pid = iObj.getPid();

			String command = "powershell \"(Get-Process -Id " + pid + " | Select-Object path)\"";

			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
			builder.redirectErrorStream(true);

			Process p = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line;
			String storeLine = null;

			while ((line = reader.readLine()) != null) {
				if (line.contains(".exe")) {
					storeLine = line;
					break;
				}
			}

			if (storeLine == null) {
				throw new IOException("Failed to retrieve process path.");
			}

			File filep = new File(storeLine.trim());

			if (!filep.exists()) {
				throw new IOException("Process path does not exist.");
			}

			File file = new File(filep.getParentFile().getAbsolutePath()); // Using the parent directory of the process
																			// path

			long totalSpace = file.getTotalSpace();
			long usableSpace = file.getUsableSpace();

			return new DiscDetails(totalSpace, usableSpace);

		} catch (IOException e) {
			e.printStackTrace();
			return new DiscDetails();
		}

	}

	public int pidGetterLinux() throws IOException {
		int processPID;
		// String processName = "'jps'";
		String command = "jps -lv";
		Process p = Runtime.getRuntime().exec(command);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		String lastLine = null;
		while ((line = stdInput.readLine()) != null) {

			if (line.indexOf(processName) != -1) {
				lastLine = line;
				break;
			}
		}
		if (lastLine == null)
			return -1;

		String[] splitLastLine = lastLine.split(" ");
		processPID = Integer.parseInt(splitLastLine[0]);

		return processPID;

	}

	public String ramUsageLinux(String processName) throws IOException, InstanceNotFoundException,
			AttributeNotFoundException, MalformedObjectNameException, ReflectionException, MBeanException {

		String[] command = { "/bin/sh", "-c", "sudo pmap " + (processId) + " | grep total" };

		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		Object attribute = mBeanServer.getAttribute(new ObjectName("java.lang", "type", "OperatingSystem"),
				"TotalPhysicalMemorySize");
		Object attribute2 = mBeanServer.getAttribute(new ObjectName("java.lang", "type", "OperatingSystem"),
				"FreePhysicalMemorySize");
//		System.out.println("Total memory: " + Long.parseLong(attribute.toString()) / (1024 * 1024) + " MB");
//		System.out.println("Free  memory: " + Long.parseLong(attribute2.toString()) / (1024 * 1024) + " MB");
//		System.out.println("Consumed memory: " + (Long.parseLong(attribute.toString()) / (1024 * 1024)
//				- Long.parseLong(attribute2.toString()) / (1024 * 1024)) + " MB");

		SystemPropertiesRamDetails ramDetails = new SystemPropertiesRamDetails();
		ramDetails.setTotalMemory(Long.parseLong(attribute.toString()) / (1024 * 1024));
		ramDetails.setFreeMemory(Long.parseLong(attribute2.toString()) / (1024 * 1024));
		ramDetails.setApplicationRamUsage((Long.parseLong(attribute.toString()) / (1024 * 1024)
				- Long.parseLong(attribute2.toString()) / (1024 * 1024)));

		SystemProp iobj = new SystemProp(processName);
		String res = iobj.linuxCommandReader(command);
//		System.out.println(res);

		String ramDetailsString = "totalMemory:" + Long.parseLong(attribute.toString()) / (1024 * 1024)
				+ " | freeMemory:" + Long.parseLong(attribute2.toString()) / (1024 * 1024) + " | consumedMemory:"
				+ (Long.parseLong(attribute.toString()) / (1024 * 1024)
						- Long.parseLong(attribute2.toString()) / (1024 * 1024))
				+ " | applicationRamUsage:" + ((Long.parseLong(attribute.toString()) / (1024 * 1024)
						- Long.parseLong(attribute2.toString()) / (1024 * 1024)));

		return ramDetailsString;
	}

	public long returnRamOnPing(String processName) throws IOException, InstanceNotFoundException,
			AttributeNotFoundException, MalformedObjectNameException, ReflectionException, MBeanException {

		String[] command = { "/bin/sh", "-c", "sudo pmap " + (processId) + " | grep total" };

		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

		Object attribute = mBeanServer.getAttribute(new ObjectName("java.lang", "type", "OperatingSystem"),
				"TotalPhysicalMemorySize");

		return Long.parseLong(attribute.toString()) / (1024 * 1024);

	}

	public String cpuUsageLinux(String processName) throws NumberFormatException, Exception {

		String[] sTotalThreads = { "/bin/sh", "-c", "cat /proc/sys/kernel/threads-max" };

		String[] sMaxProcesses = { "/bin/sh", "-c", "cat /proc/sys/kernel/pid_max" };

		String[] cmdThreadCount = { "/bin/sh", "-c", "cat /proc/" + (processId) + "/status | grep Threads" };

		SystemProp iobj = new SystemProp(processName);

//		System.out.println("Maximum Processes on the system: " + iobj.linuxCommandReader(sMaxProcesses));

//		System.out.println("Total Threads in the system: " + iobj.linuxCommandReader(sTotalThreads));
		String res = iobj.linuxCommandReader(cmdThreadCount);
//		System.out.println(res);

		String[] fileHandlesTotal = { "/bin/sh", "-c", "lsof | wc -l" };

//		System.out.println("Total file handles open : " + Integer.parseInt(iobj.linuxCommandReader(fileHandlesTotal)));

		String[] cmdHandleCount = { "/bin/sh", "-c", "ls /proc/" + (processId) + "/fd | wc -l" };
//		System.out
//				.println("Number of file handles open : " + Integer.parseInt(iobj.linuxCommandReader(cmdHandleCount)));

		String cpuDetails = "threadCount:" + iobj.linuxCommandReader(cmdThreadCount) + " | totalThreads:"
				+ iobj.linuxCommandReader(sTotalThreads) + " | handleCount:"
				+ Integer.parseInt(iobj.linuxCommandReader(cmdHandleCount)) + " | totalHandleCount:"
				+ Integer.parseInt(iobj.linuxCommandReader(fileHandlesTotal)) + " | totalProcess:"
				+ iobj.linuxCommandReader(sMaxProcesses) + " | cpuUtilization:" + (iobj.getProcessCpuLoad() * 100);

		return cpuDetails;
	}

	public void diskUsageLinux() throws IOException {

		File file = new File("/");
		long totalSpace = file.getTotalSpace(); // total disk space in bytes.
		long usableSpace = file.getUsableSpace(); /// unallocated / free disk space in bytes.
		System.out.println("--------------------------------------");
		System.out.println("Total size : " + totalSpace / 1024 / 1024 + " mb");
		System.out.println("Space free : " + usableSpace / 1024 / 1024 + " mb");
		System.out.println("--------------------------------------");
		SystemProp iobj = new SystemProp(processName);
		String[] command = { "/bin/sh", "-c", "pwdx " + (processId) };
		String commandResult = iobj.linuxCommandReader(command);

		String filePath[] = commandResult.split(" ");
		File folder = new File(filePath[1]);
		File[] processLoc = folder.listFiles();
		long length = 0;
		for (int i = 0; i < processLoc.length; i++) {
			if (processLoc[i].isFile()) {
				length = length + processLoc[i].length();
			}
		}
		System.out.println("Application occupied space : " + (double) length / (1024 * 1024) + "mb");
		System.out.println("--------------------------------------");
		String[] command2 = { "/bin/sh", "-c", "df -k / | awk 'NR==2{print $3,$2}'" };

		try {
			Process process = new ProcessBuilder(command).start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line = reader.readLine();

			if (line != null) {
				String[] parts = line.split("\\s+");
				long usedSpace = Long.parseLong(parts[0]) * 1024; // Convert to bytes
				long totalSpace1 = Long.parseLong(parts[1]) * 1024; // Convert to bytes

				System.out.println("Used Disk Space: " + usedSpace / (1024 * 1024) + " MB");
				System.out.println("Total Disk Space: " + totalSpace1 / (1024 * 1024) + " MB");
				System.out.println("--------------------------------------");
				
			} else {
				throw new IOException("Failed to retrieve disk usage information.");
			}

		} catch (Exception e) {
			e.printStackTrace(); // Log or handle the exception according to your requirements
			throw new IOException("Error retrieving disk usage", e);
		}

	}

	public static void main(String[] args) throws IOException, InstanceNotFoundException, AttributeNotFoundException,
			MalformedObjectNameException, ReflectionException, MBeanException {
//
//		System.out.print("Operating System: ");
//		String OS = System.getProperty("os.name");
//		System.out.println(OS);
//
//		if (OS.matches(".*Windows.*")) {
//
//			System.out.println("windows");
//			SystemProp eObj = new SystemProp();
//			System.out.println("-----------RAM USAGE----------");
//			eObj.ramUsage();
//			System.out.println("-----------CPU USAGE----------");
//			eObj.cpuUsage();
//			System.out.println("----------DISK USAGE----------");
//			eObj.diskUsage();
//		} else {

//			System.out.println("Linux");

//			String processName = "remote.Agent";
//			SystemProp eObj = new SystemProp(processName);
//			System.out.println("-----------RAM USAGE----------");
//			eObj.ramUsageLinux(processName);
//			System.out.println("-----------CPU USAGE----------");
//			eObj.cpuUsageLinux(processName);
//			System.out.println("----------DISK USAGE----------");
//			eObj.diskUsageLinux(processName);
//		}

		SystemProp monitor = new SystemProp();
//		DiscDetails discDetails = monitor.diskUsage();
//
//		System.out.println("Total size: " + discDetails.getTotalSpace() / 1024 / 1024 + " MB");
//		System.out.println("Usable space: " + discDetails.getUsableSpace() / 1024 / 1024 + " MB");

		monitor.diskUsageLinux();
	}

}
