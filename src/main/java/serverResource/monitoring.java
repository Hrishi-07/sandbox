package serverResource;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;

public class monitoring {
	public static void main(String args[]) {
		printUsage();
	}

//	private static void printUsage() {
//
//		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
//		for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
//			method.setAccessible(true);
//			if (method.getName().startsWith("get") && Modifier.isPublic(method.getModifiers())) {
//				Object value;
//				try {
//					value = method.invoke(operatingSystemMXBean);
//				} catch (Exception e) {
//					value = e;
//				} // try
//				System.out.println(method.getName() + " = " + value);
//			} // if
//		} // for
//	}

	private static void printUsage() {
		OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

		try {
			Method method = osBean.getClass().getMethod("getCommittedVirtualMemorySize");
			Object value = method.invoke(osBean);
			System.out.println("CommittedVirtualMemorySize = " + value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
