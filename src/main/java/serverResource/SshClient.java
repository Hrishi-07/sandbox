package serverResource;

import com.jcraft.jsch.*;

import java.io.InputStream;

public class SshClient {

    public static void main(String[] args) throws JSchException {
        String username = "ubuntu";
        String hostname = "54.89.211.121";  // Use "127.0.0.1" or "localhost" for localhost
        int port = 22;  // The default SSH port

        // Path to your private key file (in PuTTY format for Windows)
        String privateKeyPath = "C:\\Users\\hrchavan\\OneDrive - Capgemini\\Desktop\\Putty\\openSSH";

        JSch jsch = new JSch();
        jsch.setConfig("StrictHostKeyChecking", "no");
        jsch.addIdentity(privateKeyPath);

        Session session = jsch.getSession(username, hostname, port);
        session.connect();

        try {
            // Determine the operating system
            String os ="linux";//= System.getProperty("os.name").toLowerCase();

            // Execute commands based on the operating system
            if (os.contains("win")) {
                executeCommand(session, "systeminfo");
                executeCommand(session, "powershell Get-Counter '\\Processor(_Total)\\% Processor Time'");
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                executeCommand(session, "free -m");
                executeCommand(session, "top -bn1 | grep %Cpu | awk '{print $2}'");
            } else {
                System.out.println("Unsupported operating system");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.disconnect();
        }
    }

    private static void executeCommand(Session session, String command) {
        try {
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);

            InputStream in = channel.getInputStream();
            channel.connect();

            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }

            channel.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
