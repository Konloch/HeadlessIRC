import com.konloch.ircbot.IRCBot;
import com.konloch.ircbot.server.Server;

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
		
		server.onJoin((room, user) ->
		{
			System.out.println("JOIN: " + room.getName() + "["+room.getUsers().size()+"] " + user.getNickname());
		});
		
		server.onLeave((room, user) ->
		{
			System.out.println("QUIT: " + room.getName() + "["+room.getUsers().size()+"] " + user.getNickname());
		});
		
		server.onRoomMessage((room, user, msg) ->
		{
			System.out.println("MSG:  " + room.getName() + "["+room.getUsers().size()+"] " + user.getNickname() + ": " + msg);
		});
		
		server.onPrivateMessage((user, msg) ->
		{
			System.out.println("PM:   " + user.getNickname() + ": " + msg);
		});
	}
}
