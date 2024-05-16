import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {

    public static void main(String[] args) {
        try {
Socket socket = new Socket("192.0.0.4", 8888);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println("Byy  Sanjit.. ");

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
