import com.konloch.ircbot.IRCBot;
import com.konloch.ircbot.server.Room;
import com.konloch.ircbot.server.Server;
import com.konloch.ircbot.server.User;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class TestIRCBot
{
	public static void main(String[] args)
	{
		IRCBot bot = new IRCBot("Test Nick", "Test Client");
		Server server = bot.join("irc.freenode.net");
		
		server.join("#test-channel-1");
		
		server.onJoin(event ->
		{
			Room room = event.getRoom();
			User user = event.getUser();
			
			System.out.println("JOIN: " + room.getName() + "[" + room.getUsers().size() + "] " + user.getNickname());
		});
		
		server.onLeave(event ->
		{
			Room room = event.getRoom();
			User user = event.getUser();
			
			System.out.println("QUIT: " + room.getName() + "["+room.getUsers().size()+"] " + user.getNickname());
		});
		
		server.onRoomMessage(event ->
		{
			Room room = event.getRoom();
			User user = event.getUser();
			String msg = event.getMessage();
			
			System.out.println("MSG:  " + room.getName() + "["+room.getUsers().size()+"] " + user.getNickname() + ": " + msg);
		});
		
		server.onPrivateMessage(event ->
		{
			User user = event.getUser();
			String msg = event.getMessage();
			
			System.out.println("PM:   " + user.getNickname() + ": " + msg);
		});
	}
}
