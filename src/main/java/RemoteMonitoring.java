import com.jcraft.jsch.*;

public class RemoteMonitoring {

    public static void main(String[] args) {
        String host = "44.211.39.215";
        String user = "ubuntu";
        String privateKeyPath = "C:\\Users\\hrchavan\\OneDrive - Capgemini\\Desktop\\Putty\\openSSH"; // Update with your private key file path

        try {
            JSch jsch = new JSch();
            jsch.addIdentity(privateKeyPath);

            Session session = jsch.getSession(user, host, 22);
            session.setConfig("StrictHostKeyChecking", "no"); // Replace with proper host key verification
            session.connect();

            // Command to get CPU usage
            String cpuCommand = "top -b -n 1 | grep '%Cpu'";

            // Command to get memory usage
            String memoryCommand = "free -m";

            // Execute CPU command
            String cpuResult = executeCommand(session, cpuCommand);

            // Execute memory command
            String memoryResult = executeCommand(session, memoryCommand);

            System.out.println("CPU Usage:\n" + cpuResult);
            System.out.println("Memory Usage:\n" + memoryResult);

            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    private static String executeCommand(Session session, String command) throws JSchException {
        try {
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.connect();

            StringBuilder result = new StringBuilder();
            byte[] tmp = new byte[1024];
            while (true) {
                while (channel.getInputStream().available() > 0) {
                    int i = channel.getInputStream().read(tmp, 0, 1024);
                    if (i < 0) break;
                    result.append(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (channel.getExitStatus() == 0) {
                        break;
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            channel.disconnect();
            return result.toString();
        } catch (Exception e) {
            throw new JSchException("Error executing command: " + e.getMessage(), e);
        }
    }
}
