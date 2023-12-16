import com.konloch.ircbot.IRCBot;
import com.konloch.ircbot.server.Channel;
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
		
		Channel joinedChannel = server.join("#test-channel-1");
		
		server.onJoin(event ->
		{
			Channel channel = event.getChannel();
			User user = event.getUser();
			
			System.out.println("JOIN: " + channel.getName() + "[" + channel.getUsers().size() + "] " + user.getNickname());
		});
		
		server.onLeave(event ->
		{
			Channel channel = event.getChannel();
			User user = event.getUser();
			
			System.out.println("QUIT: " + channel.getName() + "["+ channel.getUsers().size()+"] " + user.getNickname());
		});
		
		server.onChannelMessage(event ->
		{
			Channel channel = event.getChannel();
			User user = event.getUser();
			String msg = event.getMessage();
			
			System.out.println("MSG:  " + channel.getName() + "["+ channel.getUsers().size()+"] " + user.getNickname() + ": " + msg);
		});
		
		server.onPrivateMessage(event ->
		{
			User user = event.getUser();
			String msg = event.getMessage();
			
			System.out.println("PM:   " + user.getNickname() + ": " + msg);
		});
	}
}
